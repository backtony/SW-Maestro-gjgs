package com.gjgs.gjgs.modules.payment.entity;

import com.gjgs.gjgs.modules.exception.payment.CanNotCancelOrderException;
import com.gjgs.gjgs.modules.exception.payment.CanceledOrderException;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.enums.OrderStatus;
import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.gjgs.gjgs.modules.payment.enums.OrderStatus.*;
import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "orders")
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

    public static List<Order> ofTeam(Schedule schedule, Team team) {
        return team.getAllMembers().stream().map(member -> Order.ofTeam(member, schedule, team)).collect(toList());
    }

    private static Order ofTeam(Member member, Schedule schedule, Team team) {
        return Order.builder()
                .member(member).schedule(schedule)
                .orderStatus(WAIT)
                .team(team)
                .originalPrice(schedule.getLecturePrice(team.getCurrentMemberCount()))
                .build();
    }

    public static Order ofPersonal(Member member, Schedule schedule, int discountPrice) {
        Order order = Order.builder()
                .member(member)
                .schedule(schedule)
                .orderStatus(WAIT)
                .originalPrice(schedule.getLecturePrice(1))
                .discountPrice(discountPrice)
                .build();
        order.setFinalPrice();
        return order;
    }

    private void setFinalPrice() {
        finalPrice = originalPrice - discountPrice;
    }

    public void addMemberCoupon(MemberCoupon memberCoupon) {
        this.memberCoupon = memberCoupon;
    }

    public void addReward(Reward reward) {
        this.reward = reward;
    }

    public void applyPayment(PaymentRequest paymentRequest) {
        checkCancel();
        this.discountPrice = paymentRequest.getTotalDiscountPrice();
        setFinalPrice();
    }

    private void checkCancel() {
        if (this.getOrderStatus().equals(CANCEL)) {
            throw new CanceledOrderException();
        }
    }

    public void complete() {
        checkCancel();
        this.orderStatus = COMPLETE;
    }

    public boolean equalsPaymentPrice(int paymentPrice) {
        return paymentPrice == this.getFinalPrice();
    }

    public void cancel() {
        checkOverThreeDays(schedule.getStartLocalDateTime());
        this.orderStatus = CANCEL;
    }

    private void checkOverThreeDays(LocalDateTime startDateTime) {
        if (ChronoUnit.HOURS.between(now(), startDateTime) < 72) {
            throw new CanNotCancelOrderException();
        }
    }

    public void cancelForWait() {
        if (this.getOrderStatus().equals(WAIT)) {
            this.orderStatus = CANCEL;
        }
    }

    public void setIamportUid(String iamportUid) {
        this.iamportUid = iamportUid;
    }
}
