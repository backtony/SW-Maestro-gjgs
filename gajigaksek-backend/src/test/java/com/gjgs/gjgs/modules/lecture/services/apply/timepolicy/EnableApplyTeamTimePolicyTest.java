package com.gjgs.gjgs.modules.lecture.services.apply.timepolicy;

import com.gjgs.gjgs.modules.exception.schedule.ScheduleTimeOverException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class EnableApplyTeamTimePolicyTest {

    @Test
    @DisplayName("팀 신청시 클래스 시작 시간 한 시간 전에 신청하지 않을 경우")
    void check_close_time_exception_test() throws Exception {

        // given
        EnableApplyTeamTimePolicy policy = new EnableApplyTeamTimePolicy();
        LocalDateTime scheduleStartTime = LocalDateTime.of(LocalDate.now(), LocalTime.now().plusMinutes(59));

        // when, then
        assertThrows(ScheduleTimeOverException.class,
                () -> policy.checkCloseTime(scheduleStartTime),
                "팀 신청의 경우 한시간 이상 남아있어야 신청 가능하다");
    }
}