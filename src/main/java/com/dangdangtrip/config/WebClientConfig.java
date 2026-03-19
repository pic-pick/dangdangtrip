package com.dangdangtrip.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final TourApiProperties tourApiProperties;

    @Bean
    public WebClient tourApiWebClient() {
        return WebClient.builder()
                .baseUrl(tourApiProperties.getBaseUrl())
                .build();
    }
}
