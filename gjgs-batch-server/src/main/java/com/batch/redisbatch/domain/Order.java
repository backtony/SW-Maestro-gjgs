package com.batch.redisbatch.domain;

import com.batch.redisbatch.domain.lecture.Schedule;
import com.batch.redisbatch.enums.OrderStatus;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "ORDERS")
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY) @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SCHEDULE_ID", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int originalPrice;

    private int discountPrice;

    private int finalPrice;

    private String iamportUid;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_COUPON_ID")
    private MemberCoupon memberCoupon;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "REWARD_ID")
    private Reward reward;

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
