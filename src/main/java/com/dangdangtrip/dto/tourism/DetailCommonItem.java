package com.dangdangtrip.dto.tourism;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailCommonItem {

    private String contentid;
    private String contenttypeid;
    private String title;
    private String addr1;
    private String addr2;
    private String tel;
    private String homepage;
    private String overview;
    private String firstimage;
    private String firstimage2;
    private String mapx;
    private String mapy;
}