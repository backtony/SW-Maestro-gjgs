package com.gjgs.gjgs.modules.notification.entity;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.dto.NotificationIncludeFcmToken;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Notification extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean checked;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(nullable = false)
    private String uuid;

    private Long TeamId;

    public static List<Notification> from(List<NotificationIncludeFcmToken> notificationIncludeFcmToken){
        List<Notification> results = new ArrayList<>();

        for (NotificationIncludeFcmToken dto : notificationIncludeFcmToken) {
            results.add(Notification.builder()
                    .member(dto.getMember())
                    .title(dto.getTitle())
                    .message(dto.getMessage())
                    .checked(dto.isChecked())
                    .notificationType(dto.getNotificationType())
                    .TeamId(dto.getTeamId())
                    .uuid(dto.getUuid())
                    .build());
        }
        return results;
    }

    public static Notification of(Member member, String title, String message, NotificationType notificationType, Long teamId){
        return Notification.builder()
                .member(member)
                .title(title)
                .message(message)
                .checked(false)
                .notificationType(notificationType)
                .TeamId(teamId)
                .uuid(UUID.randomUUID().toString())
                .build();
    }



    public void changeCheckStatus(boolean bool) {
        this.checked=bool;
    }
}
