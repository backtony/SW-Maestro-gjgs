package com.gjgs.gjgs.modules.reward.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.Getter;

@Getter
public enum RewardSaveType implements EnumType {

    RECOMMEND(RewardType.SAVE,3000,"추천인 적립금");

    private RewardType rewardType;
    private int amount;
    private String text;

    RewardSaveType(RewardType rewardType, int amount, String text) {
        this.rewardType = rewardType;
        this.amount = amount;
        this.text = text;
    }


    @Override
    public String getDescription() {
        return this.text;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
