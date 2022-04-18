package com.batch.redisbatch.service.impl;

import com.batch.redisbatch.domain.Member;
import com.batch.redisbatch.domain.Order;
import com.batch.redisbatch.domain.lecture.Schedule;
import com.batch.redisbatch.enums.OrderStatus;
import com.batch.redisbatch.event.OrderCancelEvent;
import com.batch.redisbatch.repository.interfaces.OrderQueryRepository;
import com.batch.redisbatch.repository.interfaces.ParticipantQueryRepository;
import com.batch.redisbatch.service.interfaces.IamportClientService;
import com.batch.redisbatch.service.interfaces.OrderService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.batch.redisbatch.enums.OrderStatus.COMPLETE;
import static com.batch.redisbatch.enums.OrderStatus.WAIT;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderQueryRepository orderQueryRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final IamportClientService iamportClientService;
    private final DiscountPolicyDecider discountPolicyDecider;
    private final ParticipantQueryRepository participantQueryRepository;

    public void changeStatus(Long teamId, Long scheduleId) {
        List<Order> orderList = orderQueryRepository.findWithCouponRewardMemberByTeamIdScheduleId(teamId, scheduleId);
        orderCancelProcess(scheduleId, orderList);
    }

    private void orderCancelProcess(Long scheduleId, List<Order> orderList) {
        if (isContainWaitOrder(orderList)) {
            orderList.forEach(order -> {
                Member member = order.getMember();
                ifCompleteOrderThenRefund(order, member);
                order.changeStatus(OrderStatus.CANCEL);
                eventPublisher.publishEvent(new OrderCancelEvent(member.getId()));
            });
            removeCancelMembers(scheduleId, orderList);
        }
    }

    private boolean isContainWaitOrder(List<Order> orderList) {
        Optional<Order> waitOrder = orderList.stream().filter(order -> order.getOrderStatus().equals(WAIT)).findFirst();
        return waitOrder.isPresent();
    }

    private void ifCompleteOrderThenRefund(Order order, Member member) {
        if (order.getOrderStatus().equals(COMPLETE)) {
            refund(order.getIamportUid());
            discountPolicyDecider.refund(order, member.getId());
        }
    }

    private void refund(String iamportUid) {
        try {
            iamportClientService.cancelPayment(iamportUid);
        } catch (IamportResponseException | IOException e) {
            e.printStackTrace();
        }
    }

    private void removeCancelMembers(Long scheduleId, List<Order> orderList) {
        List<Long> memberIdList = orderList.stream().map(order -> order.getMember().getId()).collect(toList());
        participantQueryRepository.deleteParticipantsByScheduleIdMemberId(scheduleId, memberIdList);
        Schedule schedule = orderList.get(0).getSchedule();
        schedule.setCurrentMember(orderList.size());
    }
}
