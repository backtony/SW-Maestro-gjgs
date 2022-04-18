package com.gjgs.gjgs.modules.notice.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;

public enum NoticeType implements EnumType {
    ALL("모든 사람 대상"),
    DIRECTOR("디렉터 대상")
    ;

    private String description;

    NoticeType(String description) {
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
