package com.gjgs.gjgs.dummy;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.entity.Notification;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class NotificationDummy {

    private final NotificationRepository notificationRepository;
    String title = "title";
    String message = "message";
    String uuid = UUID.randomUUID().toString();

    public void createNotification(Member member,int i){
        Notification notification = Notification.builder()
                .member(member)
                .title(title)
                .message(message)
                .checked(false)
                .notificationType(NotificationType.ADMIN_CUSTOM)
                .uuid(uuid+i)
                .build();
        notificationRepository.save(notification);
    }
}
