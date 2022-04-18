package com.gjgs.gjgs.modules.member.dto.mypage;

import com.gjgs.gjgs.modules.reward.enums.RewardType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class RewardDto {

    private Long rewardId;

    private int amount;

    private String text;

    private RewardType rewardType;

    private LocalDateTime createdDate;


    @QueryProjection
    public RewardDto(Long rewardId, int amount, String text, RewardType rewardType, LocalDateTime createdDate) {
        this.rewardId = rewardId;
        this.amount = amount;
        this.text = text;
        this.rewardType = rewardType;
        this.createdDate = createdDate;
    }
}
