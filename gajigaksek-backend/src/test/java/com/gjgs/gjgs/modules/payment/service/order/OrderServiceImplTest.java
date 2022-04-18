package com.gjgs.gjgs.modules.payment.service.order;

import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.dummy.ZoneDummy;
import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.dto.PaymentVerifyRequest;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.payment.entity.TeamOrder;
import com.gjgs.gjgs.modules.payment.enums.TeamOrderStatus;
import com.gjgs.gjgs.modules.payment.repository.OrderJdbcRepository;
import com.gjgs.gjgs.modules.payment.repository.OrderQueryRepository;
import com.gjgs.gjgs.modules.payment.repository.TeamOrderRepository;
import com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountPolicyDecider;
import com.gjgs.gjgs.modules.payment.service.discountpolicy.impl.NothingDiscountImpl;
import com.gjgs.gjgs.modules.payment.service.iamport.IamportClientService;
import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.reward.enums.RewardType;
import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.modules.payment.enums.OrderStatus.COMPLETE;
import static com.gjgs.gjgs.modules.payment.enums.OrderStatus.WAIT;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class OrderServiceImplTest {

    @Mock IamportClientService iamportClientService;
    @Mock OrderJdbcRepository orderJdbcRepository;
    @Mock OrderQueryRepository orderQueryRepository;
    @Mock DiscountPolicyDecider discountPolicyDecider;
    @Mock NothingDiscountImpl nothingDiscount;
    @Mock SecurityUtil securityUtil;
    @Mock TeamOrderRepository teamOrderRepository;
    @InjectMocks OrderServiceImpl orderService;

    @Test
    @DisplayName("주문 생성")
    void create_order_test() throws Exception {

        // given
        Member member1 = Member.builder().id(1L).build();
        Member member2 = Member.builder().id(2L).build();
        Member member3 = Member.builder().id(3L).build();
        Team team = TeamDummy.createTeamOfManyMembers(ZoneDummy.createZone(1L), member1, member2, member3);
        Schedule schedule = Schedule.builder().id(1L)
                .lecture(Lecture.builder()
                        .price(Price.builder()
                                .priceOne(10000).priceTwo(9000).priceThree(8000).priceFour(7000)
                                .build())
                        .build()).build();

        // when
        orderService.createOrder(schedule, team);

        // then
        verify(orderJdbcRepository).insertOrders(any());
    }

    @Test
    @DisplayName("팀에 속한 멤버가 결제한다.")
    void pay_process_test() throws Exception {

        // given
        Member member = createMember();
        Schedule schedule = createSchedule();
        Order order = createOrder(member, schedule);
        stubbingFindMemberOrder(member, schedule, order);
        PaymentRequest request = PaymentRequest.builder().lectureId(schedule.getLectureId()).build();
        stubbingDiscountPolicyDecider(request);

        // when
        orderService.paymentTeamMember(member, schedule.getId(), request);

        // then
        verify(orderQueryRepository).findByLectureScheduleIdUsername(schedule.getLectureId(), schedule.getId(), member.getUsername());
    }

    @Test
    @DisplayName("결제 검증 시 아임포트 결제 금액과 일치하지 않을 경우 예외 발생")
    void verify_and_complete_payment_exception_test() throws Exception {

        // given
        Order order = createWaitOrder();
        stubbingFindOrder(order);
        PaymentVerifyRequest request = createPaymentVerifyRequest(order);
        IamportResponse iamportResponse = mock(IamportResponse.class);
        when(iamportClientService.verifyPayment(any())).thenReturn(iamportResponse);
        Payment payment = mock(Payment.class);
        when(iamportResponse.getResponse()).thenReturn(payment);
        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(1375198));
        stubbingFindOrderQuery(order);
        IamportResponse cancelResponse = mock(IamportResponse.class);
        when(iamportClientService.cancelPayment(any())).thenReturn(cancelResponse);
        Payment cancelPayment = mock(Payment.class);
        when(cancelResponse.getResponse()).thenReturn(cancelPayment);
        when(cancelPayment.getAmount()).thenReturn(BigDecimal.valueOf(order.getFinalPrice()));
        stubbingDiscountPolicyDeciderForRefund(order);

        // when
        orderService.verifyAndCompletePayment(request);

        // then
        assertAll(
                () -> verify(orderQueryRepository).findWithCouponRewardScheduleTeamById(request.getOrderId()),
                () -> verify(securityUtil).getCurrentUserOrThrow(),
                () -> verify(teamOrderRepository, times(0)).findByScheduleIdTeamId(any(), any()),
                () -> verify(discountPolicyDecider).getRefundPolicy(order)
        );
    }

    @Test
    @DisplayName("결제 검증 및 확정하기")
    void verify_and_complete_payment_test() throws Exception {

        // given
        Order order = createWaitOrder();
        stubbingFindOrder(order);
        PaymentVerifyRequest request = createPaymentVerifyRequest(order);
        stubbingIamportVerify(request.getIamportUid(), order);
        stubbingFindTeamOrder(order);

        // when
        orderService.verifyAndCompletePayment(request);

        // then
        assertAll(
                () -> verify(orderQueryRepository).findWithCouponRewardScheduleTeamById(order.getId()),
                () -> verify(iamportClientService).verifyPayment(request.getIamportUid()),
                () -> assertEquals(order.getOrderStatus(), COMPLETE),
                () -> verify(teamOrderRepository).findByScheduleIdTeamId(order.getSchedule().getId(), order.getTeam().getId())
        );
    }

    private void stubbingFindTeamOrder(Order order) {
        when(teamOrderRepository.findByScheduleIdTeamId(any(), any()))
                .thenReturn(TeamOrder.builder()
                        .id(1L)
                        .teamId(order.getTeam().getId())
                        .scheduleId(order.getSchedule().getId())
                        .completePaymentCount(4)
                        .currentPaymentCount(3)
                        .teamOrderStatus(TeamOrderStatus.WAIT)
                        .build());
    }

    @Test
    @DisplayName("결제 취소하기")
    void cancel_test() throws Exception {

        // given
        Member member = Member.builder().id(1L).build();
        Order order = createCompleteOrder();
        stubbingFindOrder(order);
        stubbingFindTeamOrder(order);
        stubbingIamportCancel(order);
        stubbingDiscountPolicyDeciderForRefund(order);

        // when
        orderService.cancel(member,order.getId());

        // then
        assertAll(
                () -> verify(orderQueryRepository).findWithCouponRewardScheduleTeamById(order.getId()),
                () -> verify(iamportClientService).cancelPayment(order.getIamportUid()),
                () -> verify(teamOrderRepository).findByScheduleIdTeamId(order.getSchedule().getId(), order.getTeam().getId())
        );
    }

    private void stubbingFindOrderQuery(Order order) {
        when(orderQueryRepository.findWithCouponRewardScheduleTeamById(order.getId()))
                .thenReturn(Optional.of(order));
    }

    @Test
    @DisplayName("팀 신청 취소하기")
    void cancel_team_members_test()throws Exception {

        // given
        Long scheduleId = 1L;
        Long teamId = 2L;
        List<Order> orders = createTeamOrders();
        List<Order> completeOrders = orders.stream().filter(order -> order.getOrderStatus().equals(COMPLETE)).collect(toList());
        stubbingFindTeamOrders(scheduleId, teamId, orders);
        IamportResponse iamportResponse = mock(IamportResponse.class);
        when(iamportClientService.cancelPayment(any())).thenReturn(iamportResponse);
        Payment payment = mock(Payment.class);
        when(iamportResponse.getResponse()).thenReturn(payment);
        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(300));
        stubbingDiscountPolicyDeciderForRefundOrders();

        // when
        orderService.cancelAllTeamMembers(scheduleId, teamId);

        // then
        assertAll(
                () -> verify(orderQueryRepository).findWithMemberPaymentByScheduleTeamId(scheduleId, teamId),
                () -> verify(iamportClientService, times(2)).cancelPayment("test")
        );
    }

    private void stubbingDiscountPolicyDeciderForRefundOrders() {
        when(discountPolicyDecider.getRefundPolicy(any())).thenReturn(nothingDiscount);
    }

    private void stubbingFindTeamOrders(Long scheduleId, Long teamId, List<Order> orders) {
        when(orderQueryRepository.findWithMemberPaymentByScheduleTeamId(scheduleId, teamId)).thenReturn(orders);
    }

    private List<Order> createTeamOrders() {
        Member member1 = Member.builder().id(1L).build();
        Member member2 = Member.builder().id(2L).build();
        Member member3 = Member.builder().id(3L).build();
        Member member4 = Member.builder().id(4L).build();
        Team team = Team.builder().id(1L).teamMembers(List.of(
                MemberTeam.builder().member(member1).build(),
                MemberTeam.builder().member(member2).build(),
                MemberTeam.builder().member(member3).build(),
                MemberTeam.builder().member(member4).build())).build();
        Order order1 = Order.builder().member(member1).finalPrice(300).orderStatus(COMPLETE).iamportUid("test").team(team).build();
        Order order2 = Order.builder().member(member2).finalPrice(300).orderStatus(COMPLETE).iamportUid("test").team(team).build();
        Order order3 = Order.builder().member(member3).finalPrice(300).orderStatus(WAIT).team(team).build();
        Order order4 = Order.builder().member(member4).finalPrice(300).orderStatus(WAIT).team(team).build();
        return List.of(order1, order2, order3, order4);
    }

    private Order createCompleteOrder() {
        return Order.builder().id(1L)
                .iamportUid("test")
                .finalPrice(1000).finalPrice(1000).orderStatus(COMPLETE)
                .discountPrice(1000)
                .reward(Reward.builder()
                        .rewardType(RewardType.USE).amount(500)
                        .build())
                .memberCoupon(MemberCoupon.builder()
                        .used(true)
                        .discountPrice(500)
                        .build())
                .schedule(Schedule.builder().id(1L).build())
                .team(Team.builder().id(1L).build())
                .build();
    }

    private void stubbingIamportCancel(Order order) throws IamportResponseException, IOException {
        IamportResponse<Payment> iamportResponse = mock(IamportResponse.class);
        Payment payment = mock(Payment.class);

        when(iamportClientService.cancelPayment(order.getIamportUid())).thenReturn(iamportResponse);
        when(iamportResponse.getResponse()).thenReturn(payment);
        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(order.getFinalPrice()));
    }

    private void stubbingIamportVerify(String impUid, Order order) throws IamportResponseException, IOException {
        IamportResponse<Payment> iamportResponse = mock(IamportResponse.class);
        Payment payment = mock(Payment.class);

        when(iamportClientService.verifyPayment(impUid)).thenReturn(iamportResponse);
        when(iamportResponse.getResponse()).thenReturn(payment);
        when(payment.getAmount()).thenReturn(BigDecimal.valueOf(order.getFinalPrice()));
    }

    private Order createWaitOrder() {
        return Order.builder().id(1L).orderStatus(WAIT).originalPrice(10000).discountPrice(1000).finalPrice(9000)
                .team(Team.builder().id(1L).build())
                .schedule(Schedule.builder().id(1L).build())
                .build();
    }

    private PaymentVerifyRequest createPaymentVerifyRequest(Order order) {
        return PaymentVerifyRequest.builder().orderId(order.getId()).iamportUid("imp_12341234").build();
    }

    private void stubbingFindOrder(Order order) {
        when(orderQueryRepository.findWithCouponRewardScheduleTeamById(order.getId())).thenReturn(Optional.of(order));
    }

    private void stubbingDiscountPolicyDecider(PaymentRequest request) {
        when(discountPolicyDecider.getDiscountPolicy(request)).thenReturn(nothingDiscount);
    }

    private void stubbingDiscountPolicyDeciderForRefund(Order order) {
        when(discountPolicyDecider.getRefundPolicy(order)).thenReturn(nothingDiscount);
    }

    private void stubbingFindMemberOrder(Member member, Schedule schedule, Order order) {
        when(orderQueryRepository.findByLectureScheduleIdUsername(schedule.getLectureId(),  schedule.getId(), member.getUsername())).thenReturn(Optional.of(order));
    }

    private Order createOrder(Member member, Schedule schedule) {
        return Order.builder()
                .member(member).schedule(schedule)
                .build();
    }

    private Schedule createSchedule() {
        return Schedule.builder()
                .id(1L)
                .lecture(Lecture.builder().id(1L).build())
                .build();
    }

    private Member createMember() {
        return Member.builder().id(1L).username("test").build();
    }
}