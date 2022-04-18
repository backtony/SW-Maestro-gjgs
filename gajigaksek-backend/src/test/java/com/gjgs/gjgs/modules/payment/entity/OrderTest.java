package com.gjgs.gjgs.modules.payment.entity;

import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.dummy.ZoneDummy;
import com.gjgs.gjgs.modules.exception.payment.CanNotCancelOrderException;
import com.gjgs.gjgs.modules.exception.payment.CanceledOrderException;
import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.gjgs.gjgs.modules.payment.enums.OrderStatus.CANCEL;
import static com.gjgs.gjgs.modules.payment.enums.OrderStatus.WAIT;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    @DisplayName("Order 생성 / 개인 신청")
    void of_personal_test() throws Exception {

        // given
        Member member = Member.builder().id(1L).build();
        Schedule schedule = Schedule.builder().id(1L)
                .lecture(Lecture.builder()
                        .price(Price.builder().priceOne(1000).priceTwo(900).priceThree(800).priceFour(700).regularPrice(1000).build())
                        .build())
                .build();
        int discountPrice = 100;

        // when
        Order order = Order.ofPersonal(member, schedule, discountPrice);

        // then
        assertAll(
                () -> assertEquals(order.getFinalPrice(), schedule.getLecturePrice(1) - discountPrice),
                () -> assertEquals(order.getOrderStatus(), WAIT)
        );
    }

    @Test
    @DisplayName("Order 생성 / 팀 신청")
    void of_team_test() throws Exception {

        // given
        Member member1 = Member.builder().id(1L).build();
        Member member2 = Member.builder().id(2L).build();
        Member member3 = Member.builder().id(3L).build();
        Member member4 = Member.builder().id(4L).build();
        Team team = TeamDummy.createTeamOfManyMembers(ZoneDummy.createZone(1L), member1, member2, member3, member4);
        Schedule schedule = Schedule.builder().id(1L)
                .lecture(Lecture.builder()
                        .price(Price.builder().priceOne(1000).priceTwo(900).priceThree(800).priceFour(700).regularPrice(1000).build())
                        .build())
                .build();

        // when
        List<Order> orders = Order.ofTeam(schedule, team);

        // then
        assertEquals(orders.size(), 4);
    }

    @Test
    @DisplayName("Order 생성 후, 팀원이 결제")
    void apply_payment_test() throws Exception {

        // given
        Order order = Order.builder().orderStatus(WAIT).originalPrice(20000).build();
        PaymentRequest request = PaymentRequest.builder().totalPrice(20000).build();

        // when
        order.applyPayment(request);

        // then
        assertAll(
                () -> assertEquals(order.getOrderStatus(), WAIT),
                () -> assertEquals(order.getFinalPrice(), request.getOriginalPrice())
        );
    }

    @Test
    @DisplayName("Order 생성 후, 팀원이 결제 / 쿠폰이 존재할 경우")
    void apply_payment_coupon_test() throws Exception {

        // given
        Order order = Order.builder().orderStatus(WAIT).originalPrice(20000).build();
        PaymentRequest request = PaymentRequest.builder().totalPrice(19000)
                .couponDiscountPrice(1000).build();

        // when
        order.applyPayment(request);

        // then
        assertAll(
                () -> assertEquals(order.getOrderStatus(), WAIT),
                () -> assertEquals(order.getFinalPrice(), request.getTotalPrice()),
                () -> assertEquals(order.getOriginalPrice(), request.getOriginalPrice())
        );
    }

    @Test
    @DisplayName("Order 생성 후, 팀원이 결제 / 리워드 존재할 경우")
    void apply_payment_reward_test() throws Exception {

        // given
        Order order = Order.builder().orderStatus(WAIT).originalPrice(20000).build();
        PaymentRequest request = PaymentRequest.builder().totalPrice(19000)
                .rewardAmount(1000).build();

        // when
        order.applyPayment(request);

        // then
        assertAll(
                () -> assertEquals(order.getOrderStatus(), WAIT),
                () -> assertEquals(order.getFinalPrice(), request.getTotalPrice()),
                () -> assertEquals(order.getOriginalPrice(), request.getOriginalPrice())
        );
    }

    @Test
    @DisplayName("Order 생성 후, 팀원이 결제 / 쿠폰과 리워드가 존재할 경우")
    void apply_payment_coupon_reward_test() throws Exception {

        // given
        Order order = Order.builder().orderStatus(WAIT).originalPrice(20000).build();
        PaymentRequest request = PaymentRequest.builder().totalPrice(15000).couponDiscountPrice(4000)
                .rewardAmount(1000).build();

        // when
        order.applyPayment(request);

        // then
        assertAll(
                () -> assertEquals(order.getOrderStatus(), WAIT),
                () -> assertEquals(order.getFinalPrice(), request.getTotalPrice()),
                () -> assertEquals(order.getOriginalPrice(), request.getOriginalPrice())
        );
    }

    @Test
    @DisplayName("환불처리")
    void cancel_test() throws Exception {

        // given
        Order order = Order.builder()
                .schedule(Schedule.builder()
                        .lectureDate(LocalDate.now().plusDays(4))
                        .startTime(LocalTime.now().plusHours(1)).build())
                .build();

        // when
        order.cancel();

        // then
        assertAll(
                () -> assertEquals(order.getOrderStatus(), CANCEL)
        );
    }

    @Test
    @DisplayName("결제가 취소되었을 경우 예외 발생")
    void cancel_test_should_order_exception() throws Exception {

        // given
        Order order = Order.builder().orderStatus(CANCEL).build();

        // when, then
        assertThrows(CanceledOrderException.class, () -> order.applyPayment(PaymentRequest.builder().build()));
    }

    @Test
    @DisplayName("3일 내에 클래스 결제 취소시 예외 발생")
    void cancel_should_not_between_now_start_day_is_three() throws Exception {

        // given
        Order order = Order.builder()
                .schedule(Schedule.builder()
                        .lectureDate(LocalDate.now().plusDays(2))
                        .startTime(LocalTime.now().plusHours(23)).build())
                .build();

        // when, then
        assertThrows(CanNotCancelOrderException.class,
                order::cancel,
                "취소 시점을 기준으로 3일 이내에 클래스가 진행될 경우 취소할 수 없다.");
    }
}
