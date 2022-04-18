package com.gjgs.gjgs.modules.team.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;

public enum Age implements EnumType {
    TWENTY_TO_TWENTYFIVE("20 - 25 세"),
    TWENTYFIVE_TO_THIRTY("25 - 30 세"),
    THIRTY_TO_THIRTYFIVE("30 - 35 세"),
    THIRTYFIVE_TO_FORTY("35 - 40 세"),
    FORTY("40 이상")
    ;

    private String description;

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
