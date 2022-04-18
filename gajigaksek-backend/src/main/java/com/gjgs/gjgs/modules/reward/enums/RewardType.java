package com.gjgs.gjgs.modules.reward.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;

public enum RewardType implements EnumType {
    SAVE("적립"),
    USE("사용")
    ;


    private String description;

    RewardType(String description) {
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
