package com.dangdangtrip.dto.tourism;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourPlaceItem {

    private String contentid;
    private String contenttypeid;
    private String title;
    private String addr1;
    private String addr2;
    private String areacode;
    private String sigungucode;
    private String firstimage;
    private String firstimage2;
    private String cat1;
    private String cat2;
    private String cat3;
    private String tel;
    private String mapx;
    private String mapy;
    private String overview;
}