package com.gjgs.gjgs.modules.matching.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;

public enum Status implements EnumType {
    MATCHING("매칭 진행중"),
    NOT_MATCHING("매칭 미진행중")
    ;


    private String description;

    Status(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
