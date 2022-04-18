package com.gjgs.gjgs.modules.payment.service.order;


import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.payment.OrderNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.apply.ScheduleIdTeamIdDto;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.payment.dto.OrderIdDto;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.dto.PaymentVerifyRequest;
import com.gjgs.gjgs.modules.payment.dto.TeamMemberPaymentResponse;
import com.gjgs.gjgs.modules.payment.entity.Order;
import com.gjgs.gjgs.modules.payment.entity.TeamOrder;
import com.gjgs.gjgs.modules.payment.repository.OrderJdbcRepository;
import com.gjgs.gjgs.modules.payment.repository.OrderQueryRepository;
import com.gjgs.gjgs.modules.payment.repository.TeamOrderRepository;
import com.gjgs.gjgs.modules.payment.service.discountpolicy.DiscountPolicyDecider;
import com.gjgs.gjgs.modules.payment.service.iamport.IamportClientService;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.gjgs.gjgs.modules.payment.enums.OrderStatus.COMPLETE;
import static com.gjgs.gjgs.modules.payment.enums.OrderStatus.WAIT;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final IamportClientService iamportClientService;
    private final OrderJdbcRepository orderJdbcRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final DiscountPolicyDecider discountPolicyDecider;
    private final SecurityUtil securityUtil;
    private final TeamOrderRepository teamOrderRepository;

    @Override
    public ScheduleIdTeamIdDto createOrder(Schedule schedule, Team team) {
        List<Order> orderList = Order.ofTeam(schedule, team);
        orderJdbcRepository.insertOrders(orderList);
        return ScheduleIdTeamIdDto.of(schedule.getId(), team.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public TeamMemberPaymentResponse getTeamMemberPayment(Long scheduleId) {
        String username = getUsernameOrElseThrow();
        return orderQueryRepository.findTeamMemberPaymentByScheduleIdUsername(scheduleId, username).orElseThrow(() -> new OrderNotFoundException());
    }

    private String getUsernameOrElseThrow() {
        return securityUtil.getCurrentUsername().orElseThrow(() -> new MemberNotFoundException());
    }

    @Override
    public Long paymentPersonal(Member member, Schedule schedule, PaymentRequest paymentRequest) {
        return discountPolicyDecider.getDiscountPolicy(paymentRequest).applyPayPersonal(member, schedule, paymentRequest);
    }

    @Override
    public Long paymentTeamMember(Member member, Long scheduleId, PaymentRequest paymentRequest) {
        Order order = orderQueryRepository.findByLectureScheduleIdUsername(paymentRequest.getLectureId(), scheduleId, member.getUsername())
                .orElseThrow(() -> new OrderNotFoundException());
        return discountPolicyDecider.getDiscountPolicy(paymentRequest).applyPayTeamMember(member, order, paymentRequest);
    }

    @Override
    public OrderIdDto verifyAndCompletePayment(PaymentVerifyRequest request) throws IamportResponseException, IOException {
        final String iamportUid = request.getIamportUid();
        Order order = orderQueryRepository.findWithCouponRewardScheduleTeamById(request.getOrderId()).orElseThrow(() -> new OrderNotFoundException());
        order.setIamportUid(iamportUid);

        if(!order.equalsPaymentPrice(getPaymentAmount(iamportUid))) {
            refund(securityUtil.getCurrentUserOrThrow(), order, verifyAndGetRefund(order.getIamportUid()));
            return OrderIdDto.ofCancel(order.getId());
        }

        order.complete();
        paymentTeamOrder(order);
        return OrderIdDto.ofComplete(order.getId());
    }

    private int getPaymentAmount(String iamportUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> iamportResponse = iamportClientService.verifyPayment(iamportUid);
        return iamportResponse.getResponse().getAmount().intValue();
    }

    private void paymentTeamOrder(Order order) {
        TeamOrder teamOrder = findTeamOrderOrElseThrow(order);
        teamOrder.paid();
    }

    private TeamOrder findTeamOrderOrElseThrow(Order order) {
        if (order.getTeam() != null && order.getSchedule() != null) {
            Schedule schedule = order.getSchedule();
            Team team = order.getTeam();
            return teamOrderRepository.findByScheduleIdTeamId(schedule.getId(), team.getId());
        }
        throw new OrderNotFoundException();
    }

    @Override
    public void cancel(Member refundMember, Long orderId) throws IamportResponseException, IOException {
        Order order = orderQueryRepository.findWithCouponRewardScheduleTeamById(orderId).orElseThrow(() -> new OrderNotFoundException());
        refund(refundMember, order, verifyAndGetRefund(order.getIamportUid()));
        cancelTeamOrder(order);
    }

    private void cancelTeamOrder(Order order) {
        TeamOrder teamOrder = findTeamOrderOrElseThrow(order);
        teamOrder.cancel();
    }

    private int verifyAndGetRefund(String iamportUid) throws IamportResponseException, IOException {
        IamportResponse<Payment> iamportResponse = iamportClientService.cancelPayment(iamportUid);
        return iamportResponse.getResponse().getAmount().intValue();
    }

    private void refund(Member refundMember, Order order, int refundPrice) {
        discountPolicyDecider.getRefundPolicy(order).refund(refundMember, order, refundPrice);
    }

    @Override
    public void cancelAllTeamMembers(Long scheduleId, Long teamId) throws IamportResponseException, IOException {
        List<Order> teamOrders = orderQueryRepository.findWithMemberPaymentByScheduleTeamId(scheduleId, teamId);
        completeOrdersCancel(teamOrders);
        waitOrdersCancel(teamOrders);
    }

    private void completeOrdersCancel(List<Order> teamOrders) throws IamportResponseException, IOException {
        List<Order> completeOrders = teamOrders.stream().filter(order -> order.getOrderStatus().equals(COMPLETE)).collect(toList());
        for (Order order : completeOrders) {
            int refundPrice = verifyAndGetRefund(order.getIamportUid());
            refund(order.getMember(), order, refundPrice);
        }
    }

    private void waitOrdersCancel(List<Order> teamOrders) {
        List<Order> waitOrders = teamOrders.stream().filter(order -> order.getOrderStatus().equals(WAIT)).collect(toList());
        waitOrders.forEach(Order::cancelForWait);
    }
}
