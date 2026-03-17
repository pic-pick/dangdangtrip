package com.dangdangtrip.dto.tourism;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiResponse<T> {

    private Response<T> response;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response<T> {
        private Header header;
        private Body<T> body;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body<T> {
        private Items<T> items;
        private Integer numOfRows;
        private Integer pageNo;
        private Integer totalCount;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items<T> {
        private List<T> item;
    }
}