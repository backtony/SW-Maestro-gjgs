package com.gjgs.gjgs.modules.lecture.dtos.create;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum CreateLectureStep implements EnumType {

    FIRST("첫째"),
    INTRO("인트로"),
    CURRICULUM("커리큘럼"),
    SCHEDULE("스케쥴"),
    PRICE_COUPON("가격, 쿠폰"),
    TERMS("조건")
    ;

    private String description;

    CreateLectureStep(String description) {
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
