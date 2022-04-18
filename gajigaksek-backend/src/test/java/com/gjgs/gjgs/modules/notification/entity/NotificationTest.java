package com.gjgs.gjgs.modules.notification.entity;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.dto.NotificationIncludeFcmToken;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @DisplayName("notificationIncludeFcmToken으로 Notification 만들기")
    @Test
    void create_notification() throws Exception{
        //given
        NotificationIncludeFcmToken notificationIncludeFcmToken = createNotificationInCludeFcmToken();

        //when
        List<Notification> notification = Notification.from(List.of(notificationIncludeFcmToken));

        //then
        assertAll(
                () -> assertEquals(notificationIncludeFcmToken.getMember(),notification.get(0).getMember()),
                () -> assertEquals(notificationIncludeFcmToken.getTitle(),notification.get(0).getTitle()),
                () -> assertEquals(notificationIncludeFcmToken.getMessage(),notification.get(0).getMessage()),
                () -> assertEquals(notificationIncludeFcmToken.isChecked(),notification.get(0).isChecked()),
                () -> assertEquals(notificationIncludeFcmToken.getNotificationType(),notification.get(0).getNotificationType()),
                () -> assertNotNull(notification.get(0).getUuid()),
                () -> assertNull(notification.get(0).getTeamId())
        );
    }

    private NotificationIncludeFcmToken createNotificationInCludeFcmToken() {
        Member member = MemberDummy.createTestMember();
        String title ="title";
        String message = "message";
        NotificationType notificationType = NotificationType.MATCHING_COMPLETE;
        String fcmToken ="fcmToken";
        NotificationIncludeFcmToken notificationIncludeFcmToken
                = NotificationIncludeFcmToken.of(member, title, message, notificationType, fcmToken);
        return notificationIncludeFcmToken;
    }

    @DisplayName("teamId 있는 Notification 만들기")
    @Test
    void create_notification_include_teamId() throws Exception{
        //given
        Member member = MemberDummy.createTestMember();
        String title ="title";
        String message = "message";
        NotificationType notificationType = NotificationType.MATCHING_COMPLETE;
        Long teamId = 1L;

        //when
        Notification notification = Notification.of(member, title, message, notificationType,teamId);

        //then
        assertAll(
                () -> assertEquals(member,notification.getMember()),
                () -> assertEquals(title,notification.getTitle()),
                () -> assertEquals(message,notification.getMessage()),
                () -> assertEquals(false,notification.isChecked()),
                () -> assertEquals(notificationType,notification.getNotificationType()),
                () -> assertEquals(teamId,notification.getTeamId()),
                () -> assertNotNull(notification.getUuid())

        );
    }
}


