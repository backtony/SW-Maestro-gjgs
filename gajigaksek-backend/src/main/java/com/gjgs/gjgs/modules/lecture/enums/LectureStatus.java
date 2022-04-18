package com.gjgs.gjgs.modules.lecture.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum LectureStatus implements EnumType {

    CREATING("개설중"),
    CONFIRM("검수중"),
    ACCEPT("개설 수락"),
    REJECT("개설 거부")
    ;

    private String description;

    LectureStatus(String description) {
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
