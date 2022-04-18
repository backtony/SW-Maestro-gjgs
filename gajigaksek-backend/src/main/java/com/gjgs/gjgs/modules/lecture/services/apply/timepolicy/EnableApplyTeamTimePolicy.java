package com.gjgs.gjgs.modules.lecture.services.apply.timepolicy;

import com.gjgs.gjgs.modules.exception.schedule.ScheduleTimeOverException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class EnableApplyTeamTimePolicy implements EnableApplyScheduleTimePolicy {

    private final long TEAM_HOUR_POLICY = 1;

    @Override
    public CheckTimeType getCheckTimeType() {
        return CheckTimeType.TEAM;
    }

    @Override
    public void checkCloseTime(LocalDateTime scheduleCloseTime) {
        LocalDateTime closedTime = getTeamClosedTime(scheduleCloseTime);
        if (LocalDateTime.now().isAfter(closedTime)) {
            throw new ScheduleTimeOverException();
        }
    }

    private LocalDateTime getTeamClosedTime(LocalDateTime scheduleCloseTime) {
        return scheduleCloseTime.minusHours(TEAM_HOUR_POLICY);
    }
}
