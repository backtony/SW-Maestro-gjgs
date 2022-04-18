package com.gjgs.gjgs.modules.lecture.event;


import com.gjgs.gjgs.modules.lecture.dtos.apply.ScheduleIdTeamIdDto;
import com.gjgs.gjgs.modules.notification.service.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Async("apply")
@RequiredArgsConstructor
public class ApplyEventListener {

    private static final long APPLY_TEAM_TIMEOUT = 30; // 30분
    private static final String PREFIX = "ORDER";


    private final NotificationService notificationService;
    private final StringRedisTemplate redisTemplate;


    @EventListener
    public void handleApplyNotificationEvent(ApplyNotificationEvent applyNotificationEvent){
        notificationService.sendApplyNotification(applyNotificationEvent.getTeam());
    }

    @EventListener
    public void handleApplyRedisEvent(ApplyRedisEvent applyRedisEvent){
        ScheduleIdTeamIdDto scheduleIdTeamIdDto = applyRedisEvent.getScheduleIdTeamIdDto();
            StringBuilder stringBuilder = new StringBuilder();
            String key = stringBuilder
                    .append(PREFIX)
                    .append(",")
                    .append(scheduleIdTeamIdDto.getTeamId())
                    .append(",")
                    .append(scheduleIdTeamIdDto.getScheduleId())
                    .toString();
            redisTemplate.opsForValue().set(key,key,APPLY_TEAM_TIMEOUT, TimeUnit.MINUTES);
    }
}
