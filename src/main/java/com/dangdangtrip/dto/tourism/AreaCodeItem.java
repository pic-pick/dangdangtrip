package com.dangdangtrip.dto.tourism;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaCodeItem {

    private String code;
    private String name;
}