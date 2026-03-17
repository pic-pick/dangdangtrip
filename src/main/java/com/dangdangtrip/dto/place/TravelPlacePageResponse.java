package com.dangdangtrip.dto.place;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TravelPlacePageResponse {

    private int page;
    private int size;
    private int totalCount;
    private List<TravelPlaceSummaryResponse> content;
}