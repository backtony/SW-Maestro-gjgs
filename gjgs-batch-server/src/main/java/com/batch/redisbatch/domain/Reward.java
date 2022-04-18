package com.batch.redisbatch.domain;

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
    private String rewardType; // use(사용), save(적립)

    private static Reward build(int useRewardAmount, String useText, String rewardType) {
        return Reward.builder()
                .amount(useRewardAmount)
                .text(useText)
                .rewardType(rewardType)
                .build();
    }

    public static Reward ofPaymentCancel(int refundReward) {
        final String useText = "리워드 사용 취소";
        final String rewardType = "save";

        return build(refundReward, useText, rewardType);
    }
}
