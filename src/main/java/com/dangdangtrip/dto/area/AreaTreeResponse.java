package com.dangdangtrip.dto.area;

import java.util.List;

public class AreaTreeResponse {
    private String code;
    private String name;
    private List<AreaResponse> children;

    public AreaTreeResponse() {
    }

    public AreaTreeResponse(String code, String name, List<AreaResponse> children) {
        this.code = code;
        this.name = name;
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<AreaResponse> getChildren() {
        return children;
    }
}