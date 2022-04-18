package com.gjgs.gjgs.modules.member.dto.mypage;

import lombok.*;
import org.springframework.data.domain.Slice;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardResponse {

    private int totalReward;

    private Slice<RewardDto> rewardDtoSlice;

    public static RewardResponse of(Slice<RewardDto> rewardDtoSlice, int totalReward){
        return RewardResponse.builder()
                .totalReward(totalReward)
                .rewardDtoSlice(rewardDtoSlice)
                .build();
    }
}
