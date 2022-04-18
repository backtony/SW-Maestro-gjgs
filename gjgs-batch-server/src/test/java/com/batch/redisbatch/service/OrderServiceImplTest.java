package com.batch.redisbatch.service;


import com.batch.redisbatch.domain.Member;
import com.batch.redisbatch.domain.Order;
import com.batch.redisbatch.domain.Team;
import com.batch.redisbatch.domain.lecture.Schedule;
import com.batch.redisbatch.enums.OrderStatus;
import com.batch.redisbatch.event.OrderCancelEvent;
import com.batch.redisbatch.repository.interfaces.OrderQueryRepository;
import com.batch.redisbatch.repository.interfaces.ParticipantQueryRepository;
import com.batch.redisbatch.service.impl.DiscountPolicyDecider;
import com.batch.redisbatch.service.impl.OrderServiceImpl;
import com.batch.redisbatch.service.interfaces.IamportClientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;
import java.util.List;

import static com.batch.redisbatch.domain.lecture.ScheduleStatus.FULL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock OrderQueryRepository orderQueryRepository;
    @Mock ApplicationEventPublisher eventPublisher;
    @Mock IamportClientService iamportClientService;
    @Mock ParticipantQueryRepository participantQueryRepository;
    @Mock DiscountPolicyDecider discountPolicyDecider;
    @InjectMocks OrderServiceImpl orderService;

    Long memberId = 12345L;
    Long teamId = 12345L;
    Long scheduleId = 12345L;
    String uuid = "uuid";
    Schedule schedule = Schedule.builder()
            .currentParticipants(2)
            .id(scheduleId)
            .scheduleStatus(FULL)
            .build();

    Order waitOrder = Order.builder()
            .member(Member.builder().id(memberId).build())
            .orderStatus(OrderStatus.WAIT)
            .team(Team.builder().id(teamId).build())
            .schedule(schedule)
            .build();
    Order completeOrder = Order.builder()
            .iamportUid("test")
            .member(Member.builder().id(memberId).build())
            .team(Team.builder().id(teamId).build())
            .orderStatus(OrderStatus.COMPLETE)
            .schedule(schedule)
            .build();


    @DisplayName("주문 상태 변경")
    @Test
    void changeStatus() throws Exception{
        //given
        when(orderQueryRepository.findWithCouponRewardMemberByTeamIdScheduleId(any(), any())).thenReturn(List.of(waitOrder, completeOrder));

        //when
        orderService.changeStatus(teamId, scheduleId);

        //then
        assertAll(
                () -> assertEquals(OrderStatus.CANCEL, waitOrder.getOrderStatus()),
                () -> assertEquals(OrderStatus.CANCEL, completeOrder.getOrderStatus()),
                () -> verify(iamportClientService, times(1)).cancelPayment(completeOrder.getIamportUid()),
                () -> verify(participantQueryRepository).deleteParticipantsByScheduleIdMemberId(any(), any()),
                () -> verify(eventPublisher, times(2)).publishEvent(ArgumentMatchers.any(OrderCancelEvent.class)),
                () -> assertEquals(schedule.getCurrentParticipants(), 0)
        );
    }

    @DisplayName("결제 리스트가 없을 경우")
    @Test
    void team_order_cancel_if_list_is_empty() throws Exception{

        // given
        when(orderQueryRepository.findWithCouponRewardMemberByTeamIdScheduleId(any(), any()))
                .thenReturn(Collections.emptyList());

        // when
        orderService.changeStatus(teamId, scheduleId);

        // then
        verify(iamportClientService, times(0)).cancelPayment(any());
    }

    @DisplayName("팀원 모두 결제한 경우")
    @Test
    void team_order_success() throws Exception{

        // given
        when(orderQueryRepository.findWithCouponRewardMemberByTeamIdScheduleId(any(), any()))
                .thenReturn(List.of(completeOrder, completeOrder));

        //when
        orderService.changeStatus(scheduleId, teamId);

        //then
        verify(iamportClientService, times(0)).cancelPayment(any());
    }
}
