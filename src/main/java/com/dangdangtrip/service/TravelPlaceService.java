package com.dangdangtrip.service;

import com.dangdangtrip.dto.place.TravelPlaceDetailResponse;
import com.dangdangtrip.dto.place.TravelPlacePageResponse;
import com.dangdangtrip.dto.place.TravelPlaceSummaryResponse;
import com.dangdangtrip.dto.tourism.*;
import com.dangdangtrip.external.tourism.TourismApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelPlaceService {

    private final TourismApiClient tourismApiClient;

    public TravelPlacePageResponse getPlaces(
            Integer areaCode,
            Integer sigunguCode,
            String keyword,
            int page,
            int size
    ) {
        TourApiResponse<TourPlaceItem> apiResponse;

        if (StringUtils.hasText(keyword)) {
            apiResponse = tourismApiClient.searchPlaces(keyword, page, size);
        } else if (areaCode != null) {
            apiResponse = tourismApiClient.getAreaPlaces(areaCode, sigunguCode, page, size);
        } else {
            apiResponse = tourismApiClient.getDefaultPlaces(page, size);
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

        TourApiResponse<DetailIntroItem> introResponse = null;
        if (StringUtils.hasText(contentTypeId)) {
            introResponse = tourismApiClient.getPlaceIntroDetail(contentId, contentTypeId);
        }



        TourApiResponse<DetailImageItem> imageResponse = tourismApiClient.getPlaceImages(contentId);

        DetailIntroItem introItem = extractSingleItem(introResponse);
        List<DetailImageItem> imageItems = extractItems(imageResponse);

//        System.out.println("[DETAIL DEBUG] contentId = " + contentId);
//        System.out.println("[DETAIL DEBUG] contentTypeId = " + contentTypeId);
//        System.out.println("[DETAIL DEBUG] intro fields = " + (introItem != null ? introItem.getFields() : null));
//        System.out.println("[DETAIL DEBUG] image count = " + imageItems.size());

        return TravelPlaceDetailResponse.builder()
                .contentId(commonItem.getContentid())
                .contentTypeId(commonItem.getContenttypeid())
                .title(commonItem.getTitle())
                .addr1(commonItem.getAddr1())
                .addr2(commonItem.getAddr2())
                .tel(commonItem.getTel())
                .homepage(commonItem.getHomepage())
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
                        extractIntroValue(introItem, "chkpet"),
                        extractIntroValue(introItem, "accomcount"),
                        extractIntroValue(introItem, "roomcount"),
                        extractIntroValue(introItem, "reservationurl"),
                        extractIntroValue(introItem, "subfacility"),
                        extractIntroValue(introItem, "treatmenu"),
                        extractIntroValue(introItem, "packing")
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
}