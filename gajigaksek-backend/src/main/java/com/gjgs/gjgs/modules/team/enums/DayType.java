package com.gjgs.gjgs.modules.team.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.Getter;

@Getter
public enum DayType implements EnumType {
    MON("월요일"),
    TUE("화요일"),
    WED("수요일"),
    THU("목요일"),
    FRI("금요일"),
    SAT("토요일"),
    SUN("일요일");

    private String dayName;

    DayType(String dayName) {
        this.dayName = dayName;
    }

    public String getEnumName(DayType type) {
        return type.name();
    }

    @Override
    public String getDescription() {
        return this.dayName;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
