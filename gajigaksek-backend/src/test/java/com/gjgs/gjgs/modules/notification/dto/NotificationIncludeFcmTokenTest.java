package com.gjgs.gjgs.modules.notification.dto;


import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotificationIncludeFcmTokenTest {


    @DisplayName("teamId null인 NotificationIncludeFcmToken만들기")
    @Test
    void create_notificationIncludeFcmToken() throws Exception{

        // given
        Member member = MemberDummy.createTestMember();
        String title = "title";
        String message = "message";
        NotificationType notificationType = NotificationType.MATCHING_COMPLETE;
        String fcmToken = "fcmToken";
        Long teamId = 1L;

        //when
        NotificationIncludeFcmToken dto =
                NotificationIncludeFcmToken.of(member, title, message, notificationType, fcmToken);

        //then
        assertAll(
                () -> assertEquals(member,dto.getMember()),
                () -> assertEquals(title,dto.getTitle()),
                () -> assertEquals(message,dto.getMessage()),
                () -> assertEquals(notificationType,dto.getNotificationType()),
                () -> assertEquals(fcmToken,dto.getFcmToken()),
                () -> assertNotNull(dto.getUuid()),
                () -> assertFalse(dto.isChecked())

        );
    }
    @DisplayName("teamId있는 NotificationIncludeFcmToken만들기")
    @Test
    void create_notificationIncludeFcmToken_include_teamId() throws Exception{

        // given
        Member member = MemberDummy.createTestMember();
        String title = "title";
        String message = "message";
        NotificationType notificationType = NotificationType.MATCHING_COMPLETE;
        String fcmToken = "fcmToken";
        Long teamId = 1L;

        //when
        NotificationIncludeFcmToken dto =
                NotificationIncludeFcmToken.of(member, title, message, notificationType, fcmToken,teamId);

        //then
        assertAll(
                () -> assertEquals(member,dto.getMember()),
                () -> assertEquals(title,dto.getTitle()),
                () -> assertEquals(message,dto.getMessage()),
                () -> assertEquals(notificationType,dto.getNotificationType()),
                () -> assertEquals(fcmToken,dto.getFcmToken()),
                () -> assertEquals(teamId,dto.getTeamId()),
                () -> assertNotNull(dto.getUuid()),
                () -> assertFalse(dto.isChecked())

        );
    }
}


