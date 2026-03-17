package com.dangdangtrip.service;

import com.dangdangtrip.dto.area.AreaResponse;
import com.dangdangtrip.dto.tourism.AreaCodeItem;
import com.dangdangtrip.dto.tourism.TourApiResponse;
import com.dangdangtrip.external.tourism.TourismApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AreaService {

    private final TourismApiClient tourismApiClient;

    public List<AreaResponse> getAreas() {
        TourApiResponse<AreaCodeItem> apiResponse = tourismApiClient.getAreas();

        List<AreaCodeItem> items = extractItems(apiResponse);

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