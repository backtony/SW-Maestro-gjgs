package com.gjgs.gjgs.modules.lecture.dtos.search;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum SearchPriceCondition implements EnumType {

    LOWER_EQUAL_FIVE("5만원 이하", 0, 50000),
    FIVE_TO_TEN("5만원 이상 ~ 10만원 이하", 50000, 100000),
    GREATER_EQUAL_TEN("10만원 이상", 100000, 0);

    private String description;
    private int low;
    private int high;

    SearchPriceCondition(String description, int low, int high) {
        this.description = description;
        this.low = low;
        this.high = high;
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
