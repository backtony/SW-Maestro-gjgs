package com.gjgs.gjgs.modules.reward.repository.impl;

import com.gjgs.gjgs.modules.member.dto.mypage.QRewardDto;
import com.gjgs.gjgs.modules.member.dto.mypage.RewardDto;
import com.gjgs.gjgs.modules.member.dto.mypage.RewardPagingRequest;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardQueryRepository;
import com.gjgs.gjgs.modules.utils.querydsl.RepositorySliceHelper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gjgs.gjgs.modules.reward.entity.QReward.reward;

@Repository
@RequiredArgsConstructor
public class RewardQueryRepositoryImpl implements RewardQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Slice<RewardDto> findRewardDtoListPaginationNoOffsetByMemberIdAndRewardType
            (Long memberId, RewardPagingRequest rewardPagingRequest, Pageable pageable) {

        List<RewardDto> content = query
                .select(new QRewardDto(
                        reward.id.as("rewardId"),
                        reward.amount,
                        reward.text,
                        reward.rewardType,
                        reward.createdDate
                ))
                .from(reward)
                .where(ltRewardId(rewardPagingRequest.getLastRewardId()),
                        reward.member.id.eq(memberId),
                        reward.rewardType.eq(rewardPagingRequest.getRewardType()))
                .orderBy(reward.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return RepositorySliceHelper.toSlice(content, pageable);

    }

    private BooleanExpression ltRewardId(Long lastRewardId) {
        if (lastRewardId == null)
            return null;

        return reward.id.lt(lastRewardId);
    }


}
