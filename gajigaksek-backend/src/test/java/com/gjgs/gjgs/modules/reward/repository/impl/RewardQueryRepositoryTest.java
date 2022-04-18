package com.gjgs.gjgs.modules.reward.repository.impl;

import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.member.dto.mypage.RewardDto;
import com.gjgs.gjgs.modules.member.dto.mypage.RewardPagingRequest;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.reward.enums.RewardSaveType;
import com.gjgs.gjgs.modules.reward.enums.RewardType;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardQueryRepository;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RewardQueryRepositoryTest extends SetUpMemberRepository {

    @Autowired RewardQueryRepository rewardQueryRepository;
    @Autowired RewardRepository rewardRepository;

    @AfterEach
    void tearDown() throws Exception {
        rewardRepository.deleteAll();
    }

    @DisplayName("멤버 id와 리워드 타입으로 페이징 처리해서 리워드 상세 가져오기")
    @Test
    void find_by_memberId_and_rewardType() throws Exception{
        //given
        Member member = anotherMembers.get(0);
        List<Reward> rewardList = saveReward(member);
        RewardPagingRequest rewardPagingRequest = RewardPagingRequest.of(rewardList.get(31).getId(), RewardType.SAVE);
        PageRequest pageRequest = PageRequest.of(0, 20);
        flushAndClear();
        
        //when
        Slice<RewardDto> result = rewardQueryRepository.
                findRewardDtoListPaginationNoOffsetByMemberIdAndRewardType(member.getId(), rewardPagingRequest, pageRequest);

        //then
        List<RewardDto> content = result.getContent();

        assertAll(
                () -> assertEquals(20,content.size()),
                () -> assertEquals(rewardList.get(30).getId(),content.get(0).getRewardId()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getRewardType(),content.get(0).getRewardType()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getAmount(),content.get(0).getAmount()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getText(),content.get(0).getText()),
                () -> assertEquals(0,result.getNumber()),
                () -> assertEquals(20,result.getNumberOfElements()),
                () -> assertEquals(20,result.getSize())

        );
    }

    private List<Reward> saveReward(Member member){
        List<Reward> rewards = new ArrayList<>();
        for (int i=0;i<35;i++){
            rewards.add(Reward.createSaveReward(member, RewardSaveType.RECOMMEND));
        }
        return rewardRepository.saveAll(rewards);
    }
}
