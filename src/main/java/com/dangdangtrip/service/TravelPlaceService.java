package com.dangdangtrip.service;

import com.dangdangtrip.dto.place.TravelPlaceDetailResponse;
import com.dangdangtrip.dto.place.TravelPlacePageResponse;
import com.dangdangtrip.dto.place.TravelPlaceSummaryResponse;
import com.dangdangtrip.dto.tourism.*;
import com.dangdangtrip.external.tourism.TourismApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TravelPlaceService {

    private final TourismApiClient tourismApiClient;

    public TravelPlacePageResponse getPlaces(
            Integer areaCode,
            Integer sigunguCode,
            Integer contentTypeId,
            String keyword,
            int page,
            int size
    ) {
        TourApiResponse<TourPlaceItem> apiResponse;

        if (StringUtils.hasText(keyword)) {
            apiResponse = tourismApiClient.searchPlaces(keyword, contentTypeId, page, size);
        } else if (areaCode != null) {
            apiResponse = tourismApiClient.getAreaPlaces(areaCode, sigunguCode, contentTypeId, page, size);
        } else {
            apiResponse = tourismApiClient.getDefaultPlaces(contentTypeId, page, size);
        }

        List<TourPlaceItem> items = extractItems(apiResponse);

        List<TravelPlaceSummaryResponse> content = items.stream()
                .map(this::toSummaryResponse)
                .toList();

        int totalCount = extractTotalCount(apiResponse);

        return TravelPlacePageResponse.builder()
                .page(page)
                .size(size)
                .totalCount(totalCount)
                .content(content)
                .build();
    }


    private int extractTotalCount(TourApiResponse<TourPlaceItem> apiResponse) {
        if (apiResponse == null
                || apiResponse.getResponse() == null
                || apiResponse.getResponse().getBody() == null
                || apiResponse.getResponse().getBody().getTotalCount() == null) {
            return 0;
        }

        return apiResponse.getResponse().getBody().getTotalCount();
    }

    private TravelPlaceSummaryResponse toSummaryResponse(TourPlaceItem item) {
        return TravelPlaceSummaryResponse.builder()
                .contentId(item.getContentid())
                .title(item.getTitle())
                .addr1(item.getAddr1())
                .addr2(item.getAddr2())
                .areaCode(parseInteger(item.getAreacode()))
                .sigunguCode(parseInteger(item.getSigungucode()))
                .firstImage(item.getFirstimage())
                .tel(item.getTel())
                .cat1(item.getCat1())
                .cat2(item.getCat2())
                .cat3(item.getCat3())
                .build();
    }

    private Integer parseInteger(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public TravelPlaceDetailResponse getPlaceDetail(String contentId) {
        TourApiResponse<DetailCommonItem> commonResponse = tourismApiClient.getPlaceCommonDetail(contentId);
        DetailCommonItem commonItem = extractSingleItem(commonResponse);

        if (commonItem == null) {
            throw new IllegalArgumentException("여행지 상세 정보를 찾을 수 없습니다. contentId=" + contentId);
        }

        String contentTypeId = commonItem.getContenttypeid();

        // intro, image, petTour 병렬 호출
        reactor.core.publisher.Mono<TourApiResponse<DetailIntroItem>> introMono =
                StringUtils.hasText(contentTypeId)
                        ? tourismApiClient.getPlaceIntroDetailAsync(contentId, contentTypeId)
                        : reactor.core.publisher.Mono.empty();

        var results = reactor.core.publisher.Mono.zip(
                introMono.onErrorResume(e -> reactor.core.publisher.Mono.empty()),
                tourismApiClient.getPlaceImagesAsync(contentId).onErrorResume(e -> reactor.core.publisher.Mono.empty()),
                tourismApiClient.getPlacePetTourDetailAsync(contentId).onErrorResume(e -> reactor.core.publisher.Mono.empty())
        ).block();

        DetailIntroItem introItem = results != null ? extractSingleItem(results.getT1()) : null;
        List<DetailImageItem> imageItems = results != null ? extractItems(results.getT2()) : List.of();
        PetTourItem petTourItem = results != null ? extractSingleItem(results.getT3()) : null;

        return TravelPlaceDetailResponse.builder()
                .contentId(commonItem.getContentid())
                .contentTypeId(commonItem.getContenttypeid())
                .title(commonItem.getTitle())
                .addr1(commonItem.getAddr1())
                .addr2(commonItem.getAddr2())
                .tel(commonItem.getTel())
                .homepage(extractUrl(commonItem.getHomepage()))
                .overview(commonItem.getOverview())
                .firstImage(commonItem.getFirstimage())
                .images(buildImageList(commonItem.getFirstimage(), imageItems))
                .parking(firstNonBlank(
                        extractIntroValue(introItem, "parking"),
                        extractIntroValue(introItem, "parkingculture"),
                        extractIntroValue(introItem, "parkinglodging"),
                        extractIntroValue(introItem, "parkingleports"),
                        extractIntroValue(introItem, "parkingfood"),
                        extractIntroValue(introItem, "parkingshopping")
                ))
                .useTime(firstNonBlank(
                        extractIntroValue(introItem, "usetime"),
                        extractIntroValue(introItem, "usetimeculture"),
                        extractIntroValue(introItem, "usetimeleports"),
                        extractIntroValue(introItem, "checkintime"),
                        extractIntroValue(introItem, "checkouttime"),
                        extractIntroValue(introItem, "opentimefood"),
                        extractIntroValue(introItem, "useseason"),
                        extractIntroValue(introItem, "useseasonculture"),
                        extractIntroValue(introItem, "opentime")
                ))
                .petInfo(firstNonBlank(
                        extractIntroValue(introItem, "chkpetshopping"),
                        extractIntroValue(introItem, "chkpet")
                ))
                .infoCenter(firstNonBlank(
                        extractIntroValue(introItem, "infocenter"),
                        extractIntroValue(introItem, "infocenterculture"),
                        extractIntroValue(introItem, "infocenterleports"),
                        extractIntroValue(introItem, "infocenterlodging"),
                        extractIntroValue(introItem, "infocenterfood"),
                        extractIntroValue(introItem, "reservationlodging"),
                        extractIntroValue(introItem, "reservationfood"),
                        extractIntroValue(introItem, "infocentershopping")
                ))
                .acmpyTypeCd(petTourItem != null ? petTourItem.getAcmpyTypeCd() : null)
                .acmpyPsblCpam(petTourItem != null ? petTourItem.getAcmpyPsblCpam() : null)
                .acmpyNeedMtr(petTourItem != null ? petTourItem.getAcmpyNeedMtr() : null)
                .etcAcmpyInfo(petTourItem != null ? petTourItem.getEtcAcmpyInfo() : null)
                .relaPosesFclty(petTourItem != null ? petTourItem.getRelaPosesFclty() : null)
                .build();
    }

    private List<String> buildImageList(String firstImage, List<DetailImageItem> imageItems) {
        List<String> images = imageItems.stream()
                .map(DetailImageItem::getOriginimgurl)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();

        if (StringUtils.hasText(firstImage) && images.stream().noneMatch(firstImage::equals)) {
            return java.util.stream.Stream.concat(
                    java.util.stream.Stream.of(firstImage),
                    images.stream()
            ).toList();
        }

        return images;
    }

    private <T> T extractSingleItem(TourApiResponse<T> apiResponse) {
        List<T> items = extractItems(apiResponse);
        return items.isEmpty() ? null : items.get(0);
    }

    private <T> List<T> extractItems(TourApiResponse<T> apiResponse) {
        if (apiResponse == null
                || apiResponse.getResponse() == null
                || apiResponse.getResponse().getBody() == null
                || apiResponse.getResponse().getBody().getItems() == null
                || apiResponse.getResponse().getBody().getItems().getItem() == null) {
            return Collections.emptyList();
        }

        return apiResponse.getResponse().getBody().getItems().getItem();
    }

    private String extractIntroValue(DetailIntroItem introItem, String key) {
        if (introItem == null) {
            return null;
        }
        return introItem.getString(key);
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private static final Pattern HREF_PATTERN = Pattern.compile("href=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);

    private String extractUrl(String raw) {
        if (!StringUtils.hasText(raw)) return null;
        Matcher m = HREF_PATTERN.matcher(raw);
        if (m.find()) return m.group(1);
        // href 없으면 URL처럼 보이는 텍스트 그대로 반환
        String trimmed = raw.trim();
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://") || trimmed.startsWith("www.")) {
            return trimmed.startsWith("www.") ? "https://" + trimmed : trimmed;
        }
        return null;
    }
}