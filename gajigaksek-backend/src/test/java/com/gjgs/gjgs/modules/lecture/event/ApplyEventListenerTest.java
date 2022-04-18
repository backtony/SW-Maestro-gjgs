package com.gjgs.gjgs.modules.lecture.event;


import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.dummy.ZoneDummy;
import com.gjgs.gjgs.modules.lecture.dtos.apply.ScheduleIdTeamIdDto;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.service.interfaces.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplyEventListenerTest {

    @InjectMocks ApplyEventListener applyEventListener;
    @Mock NotificationService notificationService;
    @Mock StringRedisTemplate redisTemplate;
    @Mock ValueOperations valueOperations;



    @DisplayName("팀 신청 알림")
    @Test
    void handle_apply_notification_event() throws Exception{
        //given
        Member member = MemberDummy.createTestMember();
        ApplyNotificationEvent event = new ApplyNotificationEvent(TeamDummy.createTeam(member, ZoneDummy.createZone(1L)));

        //when
        applyEventListener.handleApplyNotificationEvent(event);

        //then
        verify(notificationService,times(1)).sendApplyNotification(any());
    }

    @DisplayName("팀 신청 레디스 저장")
    @Test
    void handle_apply_redis_event() throws Exception{
        //given
        ScheduleIdTeamIdDto test1 = ScheduleIdTeamIdDto.builder()
                .scheduleId(1L)
                .teamId(1L)
                .build();

        ApplyRedisEvent event = new ApplyRedisEvent(test1);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);


        //when
        applyEventListener.handleApplyRedisEvent(event);

        // then
        verify(redisTemplate,times(1)).opsForValue();

    }
}
