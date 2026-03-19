package com.dangdangtrip.external.tourism;

import com.dangdangtrip.config.TourApiProperties;
import com.dangdangtrip.dto.tourism.AreaCodeItem;
import com.dangdangtrip.dto.tourism.PetTourItem;
import com.dangdangtrip.dto.tourism.DetailCommonItem;
import com.dangdangtrip.dto.tourism.DetailImageItem;
import com.dangdangtrip.dto.tourism.DetailIntroItem;
import com.dangdangtrip.dto.tourism.TourApiResponse;
import com.dangdangtrip.dto.tourism.TourPlaceItem;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class TourismApiClient {

    private final WebClient tourApiWebClient;
    private final TourApiProperties tourApiProperties;

    @Cacheable(value = "places", key = "#contentTypeId + '-' + #page + '-' + #size")
    public TourApiResponse<TourPlaceItem> getDefaultPlaces(Integer contentTypeId, int page, int size) {
        return tourApiWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/areaBasedList2")
                            .queryParam("serviceKey", tourApiProperties.getServiceKey())
                            .queryParam("MobileOS", "ETC")
                            .queryParam("MobileApp", "dangdangtrip")
                            .queryParam("_type", "json")
                            .queryParam("numOfRows", size)
                            .queryParam("pageNo", page);
                    if (contentTypeId != null) builder.queryParam("contentTypeId", contentTypeId);
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<TourPlaceItem>>() {})
                .block();
    }

    @Cacheable(value = "places", key = "#areaCode + '-' + #sigunguCode + '-' + #contentTypeId + '-' + #page + '-' + #size")
    public TourApiResponse<TourPlaceItem> getAreaPlaces(Integer areaCode, Integer sigunguCode, Integer contentTypeId, int page, int size) {
        return tourApiWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/areaBasedList2")
                            .queryParam("serviceKey", tourApiProperties.getServiceKey())
                            .queryParam("MobileOS", "ETC")
                            .queryParam("MobileApp", "dangdangtrip")
                            .queryParam("_type", "json")
                            .queryParam("numOfRows", size)
                            .queryParam("pageNo", page)
                            .queryParam("areaCode", areaCode);
                    if (sigunguCode != null) builder.queryParam("sigunguCode", sigunguCode);
                    if (contentTypeId != null) builder.queryParam("contentTypeId", contentTypeId);
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<TourPlaceItem>>() {})
                .block();
    }

    @Cacheable(value = "places", key = "'search-' + #keyword + '-' + #contentTypeId + '-' + #page + '-' + #size")
    public TourApiResponse<TourPlaceItem> searchPlaces(String keyword, Integer contentTypeId, int page, int size) {
        return tourApiWebClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/searchKeyword2")
                            .queryParam("serviceKey", tourApiProperties.getServiceKey())
                            .queryParam("MobileOS", "ETC")
                            .queryParam("MobileApp", "dangdangtrip")
                            .queryParam("_type", "json")
                            .queryParam("numOfRows", size)
                            .queryParam("pageNo", page)
                            .queryParam("keyword", keyword);
                    if (contentTypeId != null) builder.queryParam("contentTypeId", contentTypeId);
                    return builder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<TourPlaceItem>>() {})
                .block();
    }

    @Cacheable(value = "areas", key = "'all'")
    public TourApiResponse<AreaCodeItem> getAreas() {
        return tourApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/areaCode2")
                        .queryParam("serviceKey", tourApiProperties.getServiceKey())
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "dangdangtrip")
                        .queryParam("_type", "json")
                        .queryParam("numOfRows", 100)
                        .queryParam("pageNo", 1)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<AreaCodeItem>>() {})
                .block();
    }

    @Cacheable(value = "areas", key = "'sigungu-' + #areaCode")
    public TourApiResponse<AreaCodeItem> getSigungu(String areaCode) {
        return tourApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/areaCode2")
                        .queryParam("serviceKey", tourApiProperties.getServiceKey())
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "dangdangtrip")
                        .queryParam("_type", "json")
                        .queryParam("areaCode", areaCode)
                        .queryParam("numOfRows", 100)
                        .queryParam("pageNo", 1)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<AreaCodeItem>>() {})
                .block();
    }



    @Cacheable(value = "detail", key = "'common-' + #contentId")
    public TourApiResponse<DetailCommonItem> getPlaceCommonDetail(String contentId) {
        return tourApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/detailCommon2")
                        .queryParam("serviceKey", tourApiProperties.getServiceKey())
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "dangdangtrip")
                        .queryParam("_type", "json")
                        .queryParam("contentId", contentId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<DetailCommonItem>>() {})
                .block();
    }

    public TourApiResponse<DetailIntroItem> getPlaceIntroDetail(String contentId, String contentTypeId) {
        return getPlaceIntroDetailAsync(contentId, contentTypeId).block();
    }

    public reactor.core.publisher.Mono<TourApiResponse<PetTourItem>> getPlacePetTourDetailAsync(String contentId) {
        return tourApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/detailPetTour2")
                        .queryParam("serviceKey", tourApiProperties.getServiceKey())
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "dangdangtrip")
                        .queryParam("_type", "json")
                        .queryParam("contentId", contentId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<PetTourItem>>() {});
    }

    public reactor.core.publisher.Mono<TourApiResponse<DetailIntroItem>> getPlaceIntroDetailAsync(String contentId, String contentTypeId) {
        return tourApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/detailIntro2")
                        .queryParam("serviceKey", tourApiProperties.getServiceKey())
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "dangdangtrip")
                        .queryParam("_type", "json")
                        .queryParam("contentId", contentId)
                        .queryParam("contentTypeId", contentTypeId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<DetailIntroItem>>() {});
    }

    public TourApiResponse<DetailImageItem> getPlaceImages(String contentId) {
        return getPlaceImagesAsync(contentId).block();
    }

    public reactor.core.publisher.Mono<TourApiResponse<DetailImageItem>> getPlaceImagesAsync(String contentId) {
        return tourApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/detailImage2")
                        .queryParam("serviceKey", tourApiProperties.getServiceKey())
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "dangdangtrip")
                        .queryParam("_type", "json")
                        .queryParam("contentId", contentId)
                        .queryParam("imageYN", "Y")
                        .queryParam("subImageYN", "Y")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<DetailImageItem>>() {});
    }
}