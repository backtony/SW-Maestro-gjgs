package com.gjgs.gjgs.modules.lecture.dtos.admin;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public enum DecideLectureType implements EnumType {

    ACCEPT("클래스를 승인한다."),
    REJECT("클래스를 거절한다.");

    private String description;

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
