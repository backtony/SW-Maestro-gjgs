package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.dto.NotificationCreateRequest;
import com.gjgs.gjgs.modules.notification.dto.NotificationDto;
import com.gjgs.gjgs.modules.notification.entity.Notification;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.notification.enums.TargetType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class NotificationDummy {
    public static NotificationCreateRequest createNotificationForm(String title, String message, TargetType targetType, List<Long> memberIdList){
        return NotificationCreateRequest.builder()
                .title(title)
                .message(message)
                .targetType(targetType)
                .memberIdList(memberIdList)
                .build();
    }

    public static Notification createNotification(Member member){
        return Notification.builder()
                .member(member)
                .title("title")
                .message("message")
                .checked(false)
                .notificationType(NotificationType.ADMIN_CUSTOM)
                .uuid(UUID.randomUUID().toString())
                .build();
    }

    public static NotificationDto createNotificationDto(Long notificationId){
        return NotificationDto.builder()
                .notificationId(notificationId)
                .title("title")
                .message("message")
                .checked(false)
                .notificationType(NotificationType.ADMIN_CUSTOM)
                .uuid("uuid")
                .teamId(1L)
                .createdDate(LocalDateTime.now())
                .build();
    }

    public static List<NotificationDto> createNotificationDtoList(){
        NotificationDto notificationDto1 = NotificationDummy.createNotificationDto(1L);
        NotificationDto notificationDto2 = NotificationDummy.createNotificationDto(2L);
        return List.of(notificationDto1, notificationDto2);
    }
}
