package com.dangdangtrip.dto.place;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TravelPlaceDetailResponse {

    private String contentId;
    private String contentTypeId;
    private String title;
    private String addr1;
    private String addr2;
    private String tel;
    private String homepage;
    private String overview;
    private String firstImage;
    private List<String> images;

    private String parking;
    private String useTime;
    private String petInfo;
    private String infoCenter;
}