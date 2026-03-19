package com.dangdangtrip.service;

import com.dangdangtrip.dto.area.AreaResponse;
import com.dangdangtrip.dto.tourism.AreaCodeItem;
import com.dangdangtrip.dto.tourism.TourApiResponse;
import com.dangdangtrip.external.tourism.TourismApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final TourismApiClient tourismApiClient;

    public List<AreaResponse> getAreas() {
        TourApiResponse<AreaCodeItem> apiResponse = tourismApiClient.getAreas();
        return toAreaResponses(extractItems(apiResponse));
    }

    public List<AreaResponse> getSigungu(String areaCode) {
        TourApiResponse<AreaCodeItem> apiResponse = tourismApiClient.getSigungu(areaCode);
        return toAreaResponses(extractItems(apiResponse));
    }

    public List<Map<String, Object>> getAllAreas() {
        List<AreaResponse> areas = getAreas();
        List<Map<String, Object>> result = new ArrayList<>();

        for (AreaResponse area : areas) {
            Map<String, Object> areaNode = new LinkedHashMap<>();
            areaNode.put("code", area.getCode());
            areaNode.put("name", area.getName());
            areaNode.put("children", getSigungu(area.getCode()));
            result.add(areaNode);
        }

        return result;
    }

    private List<AreaResponse> toAreaResponses(List<AreaCodeItem> items) {
        return items.stream()
                .map(item -> AreaResponse.builder()
                        .code(item.getCode())
                        .name(item.getName())
                        .build())
                .toList();
    }

    private List<AreaCodeItem> extractItems(TourApiResponse<AreaCodeItem> apiResponse) {
        if (apiResponse == null
                || apiResponse.getResponse() == null
                || apiResponse.getResponse().getBody() == null
                || apiResponse.getResponse().getBody().getItems() == null
                || apiResponse.getResponse().getBody().getItems().getItem() == null) {
            return Collections.emptyList();
        }
        return apiResponse.getResponse().getBody().getItems().getItem();
    }


}