package com.gjgs.gjgs.modules.reward.service;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardJdbcRepository;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardRepository;
import com.gjgs.gjgs.modules.reward.service.impl.RewardServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @InjectMocks RewardServiceImpl rewardService;
    @Mock RewardRepository rewardRepository;
    @Mock MemberRepository memberRepository;
    @Mock RewardJdbcRepository rewardJdbcRepository;

    @DisplayName("추천인 리워드 저장")
    @Test
    void save_recommend_reward() throws Exception{
        //given
        Member m1 = MemberDummy.createTestMember();
        Member m2 = MemberDummy.createTestDirectorMember();
        when(memberRepository.findByNickname(any()))
                .thenReturn(Optional.of(m1))
                .thenReturn(Optional.of(m2));

        //when
        rewardService.SaveRecommendReward(m1.getNickname(),m2.getNickname());

        //then
        assertAll(
                () -> assertEquals(6000,m1.getTotalReward()),
                () -> assertEquals(3000,m2.getTotalReward())
        );
    }

    @DisplayName("리워드 사용")
    @Test
    void use_reward() throws Exception{

        // given
        Member member = Member.builder()
                .id(1L)
                .totalReward(1000)
                .build();
        PaymentRequest request = PaymentRequest.builder()
                .rewardAmount(100)
                .build();

        // when
        rewardService.useReward(member, request);

        // then
        assertAll(
                () -> verify(rewardRepository).save(any()),
                () -> assertEquals(member.getTotalReward(), 900)
        );
    }

    @DisplayName("리워드 환불")
    @Test
    void refund() throws Exception {

        // given
        Member member = Member.builder().build();
        Reward reward = Reward.builder().amount(2000).build();

        // when
        rewardService.refundReward(member, reward);

        // then
        assertAll(
                () -> verify(rewardRepository).save(any()),
                () -> assertEquals(member.getTotalReward(), 2000)
        );
    }
}
