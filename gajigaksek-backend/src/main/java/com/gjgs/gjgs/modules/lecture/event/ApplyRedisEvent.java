package com.gjgs.gjgs.modules.lecture.event;

import com.gjgs.gjgs.modules.lecture.dtos.apply.ScheduleIdTeamIdDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplyRedisEvent {
    private final ScheduleIdTeamIdDto scheduleIdTeamIdDto;
}
