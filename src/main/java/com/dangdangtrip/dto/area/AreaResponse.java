package com.dangdangtrip.dto.area;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AreaResponse {
    private String code;
    private String name;

    public AreaResponse() {
    }

    public AreaResponse(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}