package com.dangdangtrip.dto.tourism;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetailIntroItem {

    private final Map<String, Object> fields = new HashMap<>();

    @JsonAnySetter
    public void setField(String key, Object value) {
        fields.put(key, value);
    }

    public String getString(String key) {
        Object value = fields.get(key);
        return value != null ? String.valueOf(value) : null;
    }
}