package com.gjgs.gjgs.modules.member.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.Getter;


@Getter
public enum Alarm implements EnumType {
    EVENT("eventAlarm","이벤트 알림")
    ;

    private String field;
    private String description;


    Alarm(String field, String description) {
        this.field = field;
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
