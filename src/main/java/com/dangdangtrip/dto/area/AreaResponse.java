package com.dangdangtrip.dto.area;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AreaResponse {

    private String code;
    private String name;
}