package com.gjgs.gjgs.modules.lecture.event;

import com.gjgs.gjgs.modules.team.entity.Team;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplyNotificationEvent {
    private final Team team;
}
