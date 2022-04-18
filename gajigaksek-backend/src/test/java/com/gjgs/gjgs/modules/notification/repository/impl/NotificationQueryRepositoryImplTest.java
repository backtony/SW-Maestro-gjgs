package com.gjgs.gjgs.modules.notification.repository.impl;

import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.dummy.NotificationDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.dto.NotificationDto;
import com.gjgs.gjgs.modules.notification.entity.Notification;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationQueryRepository;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationQueryRepositoryImplTest extends SetUpMemberRepository {

    @Autowired NotificationRepository notificationRepository;
    @Autowired NotificationQueryRepository notificationQueryRepository;

    @AfterEach
    void tearDown() throws Exception {
        notificationRepository.deleteAll();
    }

    @DisplayName("uuid와 username으로 알림 찾기")
    @Test
    void find_notification_by_uuid_and_username() throws Exception{
        //given
        Member member = anotherMembers.get(0);
        Notification notification = NotificationDummy.createNotification(member);
        notificationRepository.save(notification);
        flushAndClear();

        //when
        Notification noti
                = notificationQueryRepository.findNotificationByUuidAndUsername(notification.getUuid(), notification.getMember().getUsername()).get();

        //then
        assertAll(
                () -> assertEquals(notification.getTitle(),noti.getTitle()),
                () -> assertEquals(notification.getMessage(),noti.getMessage()),
                () -> assertEquals(member.getId(),noti.getMember().getId()),
                () -> assertEquals(notification.getNotificationType(),noti.getNotificationType()),
                () -> assertNotNull(noti.getCreatedDate()),
                () -> assertNotNull(noti.getLastModifiedDate()),
                () -> assertFalse(noti.isChecked())
        );
    }

    @DisplayName("해당 유저이름의 페이징 알림 찾기")
    @Test
    void find_notification_by_username() throws Exception{

        //given
        Member member = anotherMembers.get(0);
        String title = "title";
        String message = "message";
        String uuid = UUID.randomUUID().toString();

        List<Notification> notificationList = saveNotification(member, title, message, uuid);

        flushAndClear();

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Slice<NotificationDto> results
                = notificationQueryRepository.findNotificationDtoListPaginationNoOffsetByUsername
                (member.getUsername(), notificationList.get(21).getId(), pageRequest);

        //then
        List<NotificationDto> content = results.getContent();

        assertAll(
                () -> assertEquals(notificationList.get(20).getId(), content.get(0).getNotificationId()),
                () -> assertEquals(10,results.getNumberOfElements()),
                () -> assertEquals(0,results.getNumber()),
                () -> assertEquals(10,results.getSize()),
                () -> assertNull(content.get(0).getTeamId()),
                () -> assertTrue(content.get(0).getTitle().contains(title)),
                () -> assertTrue(content.get(0).getMessage().contains(message)),
                () -> assertEquals(NotificationType.ADMIN_CUSTOM,content.get(0).getNotificationType()),
                () -> assertEquals(uuid,content.get(0).getUuid()),
                () -> assertNotNull(content.get(0).getCreatedDate())
        );
    }

    private List<Notification> saveNotification(Member member, String title, String message, String uuid) {
        List<Notification> notificationList = new ArrayList<>();
        for(int i=0;i<30;i++){
            Notification notification = createNotification(member, title, message, uuid, i);
            notificationList.add(notification);
        }
        return notificationRepository.saveAll(notificationList);
    }

    private Notification createNotification(Member member, String title, String message, String uuid, int i) {
        Notification notification = Notification.builder()
                .member(member)
                .title(title + i)
                .message(message + i)
                .checked(false)
                .notificationType(NotificationType.ADMIN_CUSTOM)
                .uuid(uuid)
                .build();
        return notification;
    }

}
