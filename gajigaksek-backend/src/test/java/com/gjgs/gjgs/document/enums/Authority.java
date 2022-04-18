package com.gjgs.gjgs.document.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;

public enum Authority implements EnumType {
    director("디렉터"),
    admin("관리자"),
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

