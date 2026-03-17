package com.dangdangtrip.controller;

import com.dangdangtrip.dto.area.AreaResponse;
import com.dangdangtrip.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/areas")
public class AreaController {

    private final AreaService areaService;

    @GetMapping
    public List<AreaResponse> getAreas() {
        return areaService.getAreas();
    }
}