package com.gjgs.gjgs.modules.lecture.dtos.director.lecture;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum GetLectureType implements EnumType {

    ALL("전체"),
    TEMP("임시 저장된 클래스"),
    CONFIRM("검수 대기 중"),
    PROGRESS("진행중"),
    CLOSED("마감"),
    REJECT("검수 거부")
    ;

    private String description;

    @Override
    public String getName() {
        return this.name();
    }
}
