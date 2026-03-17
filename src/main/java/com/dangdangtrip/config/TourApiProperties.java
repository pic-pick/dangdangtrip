package com.dangdangtrip.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "tourapi")
public class TourApiProperties {

    private String serviceKey;
    private String baseUrl;
}