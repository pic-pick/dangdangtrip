package com.dangdangtrip.dto.tourism;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PetTourItem {

    private String contentid;
    private String acmpyTypeCd;      // 동반 가능 유형
    private String acmpyPsblCpam;    // 동반 가능 조건
    private String acmpyNeedMtr;     // 동반 시 필요 사항
    private String etcAcmpyInfo;     // 기타 동반 안내
    private String relaPosesFclty;   // 관련 보유 시설
}
