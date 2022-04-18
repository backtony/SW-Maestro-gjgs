package com.batch.redisbatch.domain;

import com.batch.redisbatch.dto.MemberFcmDto;
import com.batch.redisbatch.enums.NotificationType;
import com.batch.redisbatch.enums.PushMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @DisplayName("캔슬 알림 만들기")
    @Test
    void createTeamOrderCancelNotification() throws Exception{
        //given
        MemberFcmDto dto = MemberFcmDto.builder()
                .id(1L)
                .fcmToken("fcmToken")
                .build();

        //when
        Notification notification = Notification.createTeamOrderCancelNotification(dto);

        //then
        assertAll(
                () -> assertEquals(1L,notification.getMember().getId()),
                () -> assertEquals(PushMessage.TEAM_APPLY_CANCEL.getTitle(),notification.getTitle()),
                () -> assertEquals(PushMessage.TEAM_APPLY_CANCEL.getMessage(),notification.getMessage()),
                () -> assertFalse(notification.isChecked()),
                () -> assertEquals(NotificationType.TEAM_APPLY_CANCEL,notification.getNotificationType())
        );

    }
}
