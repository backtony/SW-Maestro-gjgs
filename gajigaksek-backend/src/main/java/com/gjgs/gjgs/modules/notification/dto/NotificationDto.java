package com.gjgs.gjgs.modules.notification.dto;

import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {

    private Long notificationId;

    private String title;

    private String message;

    private boolean checked;

    private NotificationType notificationType;

    private String uuid;

    private Long teamId;

    private LocalDateTime createdDate;

    @QueryProjection
    public NotificationDto(Long notificationId ,String title, String message, boolean checked, NotificationType notificationType, String uuid, Long teamId, LocalDateTime createdDate) {
        this.notificationId = notificationId;
        this.title = title;
        this.message = message;
        this.checked = checked;
        this.notificationType = notificationType;
        this.uuid = uuid;
        this.teamId = teamId;
        this.createdDate = createdDate;
    }
}
