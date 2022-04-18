package com.gjgs.gjgs.modules.lecture.services.apply.timepolicy;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum CheckTimeType implements EnumType {

    PERSONAL("개인 신청"),
    TEAM("팀 신청")
    ;

    private final String description;

    @Override
    public String getName() {
        return this.name();
    }
}
