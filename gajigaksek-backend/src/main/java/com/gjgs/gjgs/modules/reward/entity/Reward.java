package com.gjgs.gjgs.modules.reward.entity;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.reward.enums.RewardSaveType;
import com.gjgs.gjgs.modules.reward.enums.RewardType;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Reward extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REWARD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String text; // 해당 리워드의 설명 -> ex) 추천인 적립금, 클래스 결제에 사용

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RewardType rewardType;

    private static Reward build(Member member, int useRewardAmount, String text, RewardType rewardType) {
        return Reward.builder()
                .member(member)
                .amount(useRewardAmount)
                .text(text)
                .rewardType(rewardType)
                .build();
    }


    public static Reward createSaveReward(Member member, RewardSaveType rewardType){
        return build(member, rewardType.getAmount(), rewardType.getText(), rewardType.getRewardType());
    }

    public static Reward createUseReward(Member member, int useRewardAmount) {
        final String text = "적립된 리워드 사용";
        return build(member, useRewardAmount, text, RewardType.USE);
    }

    public static Reward createCancelReward(Member member, int refundReward) {
        final String text = "리워드 사용 취소";
        return build(member, refundReward, text, RewardType.SAVE);
    }
}
