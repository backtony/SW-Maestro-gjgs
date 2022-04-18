package com.gjgs.gjgs.modules.bulletin.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.Getter;

@Getter
public enum TimeType implements EnumType {

    MORNING("아침", "7시~12시"),
    NOON("점심", "12시~18시"),
    AFTERNOON("저녁", "18시~22시");

    private String timeName;
    private String timeDetail;

    TimeType(String timeName, String timeDetail) {
        this.timeName = timeName;
        this.timeDetail = timeDetail;
    }

    @Override
    public String getDescription() {
        return this.timeDetail;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
