package com.gjgs.gjgs.modules.member.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;

public enum Sex implements EnumType {
    F("여자"),
    M("남자")
    ;

    private String description;

    Sex(String description) {
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
