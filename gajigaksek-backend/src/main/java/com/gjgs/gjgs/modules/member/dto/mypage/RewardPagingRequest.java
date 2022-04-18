package com.gjgs.gjgs.modules.member.dto.mypage;

import com.gjgs.gjgs.modules.reward.enums.RewardType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardPagingRequest {

    private Long lastRewardId;

    @NotNull
    private RewardType rewardType;

    public static RewardPagingRequest of(Long lastRewardId, RewardType rewardType){
        return RewardPagingRequest.builder()
                .lastRewardId(lastRewardId)
                .rewardType(rewardType)
                .build();
    }

}
