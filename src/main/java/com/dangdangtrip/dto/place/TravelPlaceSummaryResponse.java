package com.dangdangtrip.dto.place;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TravelPlaceSummaryResponse {

    private String contentId;
    private String title;
    private String addr1;
    private String addr2;
    private Integer areaCode;
    private Integer sigunguCode;
    private String firstImage;
    private String tel;
    private String cat1;
    private String cat2;
    private String cat3;
}