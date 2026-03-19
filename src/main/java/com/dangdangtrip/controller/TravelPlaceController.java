package com.dangdangtrip.controller;

import com.dangdangtrip.dto.place.TravelPlaceDetailResponse;
import com.dangdangtrip.dto.place.TravelPlacePageResponse;
import com.dangdangtrip.service.TravelPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class TravelPlaceController {

    private final TravelPlaceService travelPlaceService;

    @GetMapping
    public TravelPlacePageResponse getPlaces(
            @RequestParam(required = false) Integer areaCode,
            @RequestParam(required = false) Integer sigunguCode,
            @RequestParam(required = false) Integer contentTypeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return travelPlaceService.getPlaces(areaCode, sigunguCode, contentTypeId, keyword, page, size);
    }

    @GetMapping("/{contentId}")
    public TravelPlaceDetailResponse getPlaceDetail(@PathVariable String contentId) {
        return travelPlaceService.getPlaceDetail(contentId);
    }
}