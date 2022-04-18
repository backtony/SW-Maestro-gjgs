package com.gjgs.gjgs.modules.member.dto.mypage;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class TotalRewardDto {

    private Long memberId;

    private int totalReward;

    @QueryProjection
    public TotalRewardDto(Long memberId, int totalReward) {
        this.memberId = memberId;
        this.totalReward = totalReward;
    }

    public static TotalRewardDto of (Long memberId, int totalReward){
        return TotalRewardDto.builder()
                .memberId(memberId)
                .totalReward(totalReward)
                .build();
    }
}
