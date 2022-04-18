package com.gjgs.gjgs.modules.reward.entity;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.reward.enums.RewardSaveType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardTest {

    @DisplayName("리워드 생성")
    @Test
    void create_reward() throws Exception{
        //given
        Member member = MemberDummy.createTestMember();

        //when
        Reward reward = Reward.createSaveReward(member, RewardSaveType.RECOMMEND);

        //then
        assertAll(
                () -> assertEquals(RewardSaveType.RECOMMEND.getAmount(),reward.getAmount()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getRewardType(),reward.getRewardType()),
                () -> assertEquals(RewardSaveType.RECOMMEND.getText(),reward.getText()),
                () -> assertEquals(member,reward.getMember())
        );

    }
}
