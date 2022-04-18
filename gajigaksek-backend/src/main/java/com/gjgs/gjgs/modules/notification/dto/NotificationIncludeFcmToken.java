package com.gjgs.gjgs.modules.notification.dto;


import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.notification.enums.PushMessage;
import com.gjgs.gjgs.modules.team.entity.Team;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationIncludeFcmToken {

    private Member member;

    private String title;

    private String message;

    private boolean checked;

    private NotificationType notificationType;

    private String uuid;

    private Long TeamId;

    private String fcmToken;

    public static NotificationIncludeFcmToken of(Member member, String title, String message, NotificationType notificationType, String fcmToken){
        return NotificationIncludeFcmToken.builder()
                .member(member)
                .title(title)
                .message(message)
                .checked(false)
                .notificationType(notificationType)
                .fcmToken(fcmToken)
                .uuid(UUID.randomUUID().toString())
                .build();
    }
    public static NotificationIncludeFcmToken of(Member member, String title, String message, NotificationType notificationType, String fcmToken, Long teamId){
        return NotificationIncludeFcmToken.builder()
                .member(member)
                .title(title)
                .message(message)
                .checked(false)
                .notificationType(notificationType)
                .fcmToken(fcmToken)
                .uuid(UUID.randomUUID().toString())
                .TeamId(teamId)
                .build();
    }

    public static List<NotificationIncludeFcmToken> createCustomTypeList(NotificationCreateRequest notificationCreateRequest, List<MemberFcmDto> memberFcmDto) {
        return memberFcmDto.stream().map(mfd -> NotificationIncludeFcmToken.of(
                Member.builder().id(mfd.getMemberId()).build(),
                notificationCreateRequest.getTitle(),
                notificationCreateRequest.getMessage(),
                NotificationType.ADMIN_CUSTOM,
                mfd.getFcmToken()
        )).collect(Collectors.toList());
    }

    public static List<NotificationIncludeFcmToken> createMatchingCompleteTypeList(List<Member> memberList, Long teamId) {
        return memberList.stream().map(member -> NotificationIncludeFcmToken.of(
                member,
                PushMessage.MATCHING_COMPLETE.getTitle(),
                PushMessage.MATCHING_COMPLETE.getMessage(),
                NotificationType.MATCHING_COMPLETE,
                member.getFcmToken(),
                teamId
        )).collect(toList());
    }

    public static List<NotificationIncludeFcmToken> createTeamApplyTypeList(Team team) {
        return team.getAllMembers().stream().map(member -> NotificationIncludeFcmToken.of(
                member,
                PushMessage.TEAM_APPLY.getTitle(),
                PushMessage.TEAM_APPLY.getMessage(),
                NotificationType.TEAM_APPLY,
                member.getFcmToken(),
                team.getId()
        )).collect(toList());
    }
}
