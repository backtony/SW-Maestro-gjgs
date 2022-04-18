package com.gjgs.gjgs.modules.bulletin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum RecruitStatus {

    RECRUIT("모집중"),
    CLOSE("모집완료");

    private String description;

    RecruitStatus(String description) {
        this.description = description;
    }
}
