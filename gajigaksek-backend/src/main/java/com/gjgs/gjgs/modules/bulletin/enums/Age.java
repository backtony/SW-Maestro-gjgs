package com.gjgs.gjgs.modules.bulletin.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum Age implements EnumType {

    TWENTY_TO_TWENTYFIVE("20~25세"),
    TWENTYFIVE_TO_THIRTY("25~30세"),
    THIRTY_TO_THIRTYFIVE("30~35세"),
    THIRTYFIVE_TO_FORTY("35~40세"),
    FORTY("40세 이상");

    String description;

    Age(String description) {
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
