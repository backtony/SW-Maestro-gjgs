package com.gjgs.gjgs.modules.notification.repository.impl;

import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.dto.NotificationIncludeFcmToken;
import com.gjgs.gjgs.modules.notification.entity.Notification;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.notification.enums.PushMessage;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationJdbcRepository;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

class NotificationJdbcRepositoryImplTest extends SetUpMemberRepository {

    @Autowired NotificationJdbcRepository notificationJdbcRepository;
    @Autowired NotificationRepository notificationRepository;


    @DisplayName("notification Include TeamId 벌크 insert")
    @Test
    void insert_notification() throws Exception{

        //given
        Member savedMember1 = anotherMembers.get(0);
        Member savedMember2 = anotherMembers.get(1);
        List<Member> memberList = List.of(savedMember1, savedMember2);
        List<Notification> notificationList = createNotificationList(memberList);
        flushAndClear();

        //when
        notificationJdbcRepository.insertNotification(notificationList);
        flushAndClear();

        List<Notification> notifications = notificationRepository.findAll();


        //then
        assertAll(
                () -> assertEquals(2,notifications.size()),

                () -> assertEquals(NotificationType.MATCHING_COMPLETE,notifications.get(0).getNotificationType()),
                () -> assertEquals(savedMember1.getId(),notifications.get(0).getMember().getId()),
                () -> assertEquals(PushMessage.MATCHING_COMPLETE.getTitle(),notifications.get(0).getTitle()),
                () -> assertEquals(PushMessage.MATCHING_COMPLETE.getMessage(),notifications.get(0).getMessage()),
                () -> assertFalse(notifications.get(0).isChecked()),
                () -> assertNotNull(notifications.get(0).getUuid()),
                () -> assertEquals(1,notifications.get(0).getTeamId()),
                () -> assertNotNull(notifications.get(0).getCreatedDate()),
                () -> assertNotNull(notifications.get(0).getLastModifiedDate()),

                () -> assertEquals(NotificationType.MATCHING_COMPLETE,notifications.get(1).getNotificationType()),
                () -> assertEquals(savedMember2.getId(),notifications.get(1).getMember().getId()),
                () -> assertEquals(PushMessage.MATCHING_COMPLETE.getTitle(),notifications.get(1).getTitle()),
                () -> assertEquals(PushMessage.MATCHING_COMPLETE.getMessage(),notifications.get(1).getMessage()),
                () -> assertFalse(notifications.get(1).isChecked()),
                () -> assertNotNull(notifications.get(1).getUuid()),
                () -> assertEquals(1,notifications.get(0).getTeamId()),
                () -> assertNotNull(notifications.get(1).getCreatedDate()),
                () -> assertNotNull(notifications.get(1).getLastModifiedDate())
        );
    }

    private List<Notification> createNotificationList(List<Member> memberList) {
        return memberList.stream().map(m1 ->
                Notification.of(m1,
                        PushMessage.MATCHING_COMPLETE.getTitle(),
                        PushMessage.MATCHING_COMPLETE.getMessage(),
                        NotificationType.MATCHING_COMPLETE,
                        1L
                )).collect(toList());
    }


    @DisplayName("notification exclude TeamId 벌크 insert")
    @Test
    void insert_notification_exclude_teamId() throws Exception{

        //given
        NotificationIncludeFcmToken notificationIncludeFcmToken = createNotificationIncludeFcmToken();
        List<Notification> notification = Notification.from(List.of(notificationIncludeFcmToken));

        //when
        notificationJdbcRepository.insertNotification(notification);
        flushAndClear();


        List<Notification> notifications = notificationRepository.findAll();


        //then
        assertAll(
                () -> assertEquals(1,notifications.size()),

                () -> assertEquals(notificationIncludeFcmToken.getNotificationType(),notifications.get(0).getNotificationType()),
                () -> assertEquals(notificationIncludeFcmToken.getMember().getId(),notifications.get(0).getMember().getId()),
                () -> assertEquals(notificationIncludeFcmToken.getTitle(),notifications.get(0).getTitle()),
                () -> assertEquals(notificationIncludeFcmToken.getMessage(),notifications.get(0).getMessage()),
                () -> assertFalse(notifications.get(0).isChecked()),
                () -> assertNotNull(notifications.get(0).getUuid()),
                () -> assertNull(notifications.get(0).getTeamId()),
                () -> assertNotNull(notifications.get(0).getCreatedDate()),
                () -> assertNotNull(notifications.get(0).getLastModifiedDate())
        );
    }

    private NotificationIncludeFcmToken createNotificationIncludeFcmToken() {
        Member member = anotherMembers.get(0);
        String title = "title";
        String message = "message";
        NotificationType notificationType = NotificationType.ADMIN_CUSTOM;
        String fcmToken = "fcmToken";

        return NotificationIncludeFcmToken.of(member, title, message, notificationType, fcmToken);
    }
}
