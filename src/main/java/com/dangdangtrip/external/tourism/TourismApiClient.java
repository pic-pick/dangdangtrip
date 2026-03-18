package com.dangdangtrip.external.tourism;

import com.dangdangtrip.config.TourApiProperties;
import com.dangdangtrip.dto.tourism.AreaCodeItem;
import com.dangdangtrip.dto.tourism.DetailCommonItem;
import com.dangdangtrip.dto.tourism.DetailImageItem;
import com.dangdangtrip.dto.tourism.DetailIntroItem;
import com.dangdangtrip.dto.tourism.TourApiResponse;
import com.dangdangtrip.dto.tourism.TourPlaceItem;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class TourismApiClient {

    private final WebClient tourApiWebClient;
    private final TourApiProperties tourApiProperties;

    public TourApiResponse<TourPlaceItem> getDefaultPlaces(int page, int size) {
        return tourApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/areaBasedList2")
                        .queryParam("serviceKey", tourApiProperties.getServiceKey())
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "dangdangtrip")
                        .queryParam("_type", "json")
                        .queryParam("numOfRows", size)
                        .queryParam("pageNo", page)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<TourPlaceItem>>() {})
                .block();
    }
    public TourApiResponse<TourPlaceItem> getAreaPlaces(Integer areaCode, Integer sigunguCode, int page, int size) {
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

                    if (sigunguCode != null) {
                        builder.queryParam("sigunguCode", sigunguCode);
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<TourPlaceItem>>() {})
                .block();
    }

    public TourApiResponse<TourPlaceItem> searchPlaces(String keyword, int page, int size) {
        return tourApiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/searchKeyword2")
                        .queryParam("serviceKey", tourApiProperties.getServiceKey())
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "dangdangtrip")
                        .queryParam("_type", "json")
                        .queryParam("numOfRows", size)
                        .queryParam("pageNo", page)
                        .queryParam("keyword", keyword)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<TourPlaceItem>>() {})
                .block();
    }

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
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<DetailIntroItem>>() {})
                .block();
    }

    public TourApiResponse<DetailImageItem> getPlaceImages(String contentId) {
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
                .bodyToMono(new ParameterizedTypeReference<TourApiResponse<DetailImageItem>>() {})
                .block();
    }
}