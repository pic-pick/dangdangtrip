package com.dangdangtrip.dto.tourism;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailImageItem {

    private String originimgurl;
    private String smallimageurl;
    private String imgname;
}