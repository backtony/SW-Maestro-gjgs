package com.gjgs.gjgs.modules.payment.entity;

import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gjgs.gjgs.modules.payment.enums.TeamOrderStatus.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamOrderTest {

    @Test
    @DisplayName("of")
    void of() throws Exception {

        // given
        Schedule schedule = Schedule.builder().id(1L).build();
        Team team = Team.builder().id(2L).build();

        // when
        TeamOrder teamOrder = TeamOrder.of(schedule, team);

        // then
        assertAll(
                () -> assertEquals(teamOrder.getTeamId(), team.getId()),
                () -> assertEquals(teamOrder.getScheduleId(), schedule.getId()),
                () -> assertEquals(teamOrder.getTeamOrderStatus(), WAIT)
        );
    }

    @Test
    @DisplayName("팀원의 결제 완료")
    void change_plus_current_payment_member() throws Exception {

        // given
        TeamOrder teamOrder = TeamOrder.builder().currentPaymentCount(1).completePaymentCount(4).build();

        // when
        teamOrder.paid();

        // then
        assertEquals(teamOrder.getCurrentPaymentCount(), 2);
    }

    @Test
    @DisplayName("팀원의 결제 취소")
    void change_minus_current_payment_member() throws Exception {

        // given
        TeamOrder teamOrder = TeamOrder.builder().currentPaymentCount(1).completePaymentCount(4).build();

        // when
        teamOrder.cancel();

        // then
        assertEquals(teamOrder.getCurrentPaymentCount(), 0);
    }

    @Test
    @DisplayName("팀원의 결제 취소시 결제 완료된 상황에서 한명이 취소한 경우")
    void change_minus_current_payment_member_complete_to_wait() throws Exception {

        // given
        TeamOrder teamOrder = TeamOrder.builder().currentPaymentCount(4).completePaymentCount(4).teamOrderStatus(COMPLETE).build();

        // when
        teamOrder.cancel();

        // then
        assertAll(
                () -> assertEquals(teamOrder.getCurrentPaymentCount(), 3),
                () -> assertEquals(teamOrder.getTeamOrderStatus(), WAIT)
        );
    }

    @Test
    @DisplayName("결제가 모두 완료되었을 경우 COMPLETE로 변경")
    void change_complete_team_orders() throws Exception {

        // given
        TeamOrder teamOrder = TeamOrder.builder().currentPaymentCount(3).completePaymentCount(4).build();

        // when
        teamOrder.paid();

        // then
        assertAll(
                () -> assertEquals(teamOrder.getTeamOrderStatus(), COMPLETE),
                () -> assertEquals(teamOrder.getCompletePaymentCount(), teamOrder.getCurrentPaymentCount())
        );
    }

    @Test
    @DisplayName("팀장의 결제 취소")
    void team_leader_cancel_apply() throws Exception {

        // given
        TeamOrder teamOrder = TeamOrder.builder().currentPaymentCount(4).completePaymentCount(4).teamOrderStatus(COMPLETE).build();

        // when
        teamOrder.teamCancel();

        // then
        assertAll(
                () -> assertEquals(teamOrder.getTeamOrderStatus(), CANCEL),
                () -> assertEquals(teamOrder.getCurrentPaymentCount(), 0)
        );
    }
}
