package com.gjgs.gjgs.modules.member.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;

public enum Authority implements EnumType {
    ROLE_USER("일반 유저"),
    ROLE_DIRECTOR("디렉터"),
    ROLE_ADMIN("관리자")
    ;


    private String description;

    Authority(String description) {
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
