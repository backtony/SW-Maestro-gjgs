package com.gjgs.gjgs.modules.reward.repository.interfaces;

import com.gjgs.gjgs.modules.member.dto.mypage.RewardDto;
import com.gjgs.gjgs.modules.member.dto.mypage.RewardPagingRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface RewardQueryRepository {

    Slice<RewardDto> findRewardDtoListPaginationNoOffsetByMemberIdAndRewardType
            (Long memberId, RewardPagingRequest rewardPagingRequest, Pageable pageable);
}
