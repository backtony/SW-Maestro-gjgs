package com.gjgs.gjgs.modules.notification.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;

public enum TargetType implements EnumType {
    ALL("모든 사람 대상"),
    SELECT("특정 사람 대상")
    ;

    private String description;

    TargetType(String description) {
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
