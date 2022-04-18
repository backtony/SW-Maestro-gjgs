package com.batch.redisbatch.domain;

import com.batch.redisbatch.dto.MemberFcmDto;
import com.batch.redisbatch.enums.NotificationType;
import com.batch.redisbatch.enums.PushMessage;
import lombok.*;

import javax.persistence.*;
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


    public static Notification createTeamOrderCancelNotification(MemberFcmDto dto) {
        return Notification.builder()
                .member(Member.builder().id(dto.getId()).build())
                .title(PushMessage.TEAM_APPLY_CANCEL.getTitle())
                .message(PushMessage.TEAM_APPLY_CANCEL.getMessage())
                .checked(false)
                .notificationType(NotificationType.TEAM_APPLY_CANCEL)
                .uuid(UUID.randomUUID().toString())
                .build();

    }
}
