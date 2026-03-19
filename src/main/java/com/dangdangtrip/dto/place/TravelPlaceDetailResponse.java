package com.dangdangtrip.dto.place;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TravelPlaceDetailResponse {

    private String contentId;
    private String contentTypeId;
    private String title;
    private String addr1;
    private String addr2;
    private String tel;
    private String homepage;
    private String overview;
    private String firstImage;
    private List<String> images;

    private String parking;
    private String useTime;
    private String petInfo;
    private String infoCenter;

    // 반려동물 동반 세부 정보 (detailPetTour2)
    private String acmpyTypeCd;      // 동반 가능 유형 코드
    private String acmpyPsblCpam;    // 동반 가능 조건
    private String acmpyNeedMtr;     // 동반 시 필요 사항
    private String etcAcmpyInfo;     // 기타 동반 안내
    private String relaPosesFclty;   // 관련 보유 시설
}