package com.gjgs.gjgs.modules.notification.service.impl;

import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.notification.FcmConnectionException;
import com.gjgs.gjgs.modules.exception.notification.NotificationNotFoundException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberQueryRepository;
import com.gjgs.gjgs.modules.notification.dto.MemberFcmDto;
import com.gjgs.gjgs.modules.notification.dto.NotificationCreateRequest;
import com.gjgs.gjgs.modules.notification.dto.NotificationIncludeFcmToken;
import com.gjgs.gjgs.modules.notification.entity.Notification;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationJdbcRepository;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationQueryRepository;
import com.gjgs.gjgs.modules.notification.service.interfaces.NotificationService;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final MemberQueryRepository memberQueryRepository;
    private final NotificationJdbcRepository notificationJdbcRepository;
    private final NotificationQueryRepository notificationQueryRepository;
    private final SecurityUtil securityUtil;

    // Firebase Admin SDK 라이브러리는 FCM HTTP v1 API를 기반으로 동작합니다.
    // HTTP v1 전송 요청의 경우 기존 요청에서 사용하는 서버 키 문자열 대신 OAuth 2.0 액세스 토큰이 필요합니다.
    // Admin SDK를 사용하여 메시지를 보내는 경우 라이브러리에서 토큰이 자동으로 처리됩니다.

    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;

    // https://developers.google.com/identity/protocols/oauth2/scopes#fcm
    @Value("${fcm.key.scope}")
    private String fireBaseScope;

    @PostConstruct
    public void init() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(
                            GoogleCredentials
                                    .fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
                                    .createScoped(List.of(fireBaseScope)))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FcmConnectionException();
        }
    }

    @Override
    public void sendCustomNotification(NotificationCreateRequest notificationCreateRequest) {
        List<MemberFcmDto> memberFcmDto = memberQueryRepository
                .findMemberFcmDtoByTargetTypeAndMemberIdListAndMemberEventAlarm(notificationCreateRequest.getTargetType(),
                        notificationCreateRequest.getMemberIdList(),
                        true);
        List<NotificationIncludeFcmToken> notificationIncludeFcmToken
                = NotificationIncludeFcmToken.createCustomTypeList(notificationCreateRequest, memberFcmDto);
        List<NotificationIncludeFcmToken> NotificationOnlyIncludeFcmTokenList = notificationIncludeFcmToken.stream()
                .filter(dto -> StringUtils.hasText(dto.getFcmToken()))
                .collect(Collectors.toList());

        saveNotification(notificationIncludeFcmToken);

        if (!NotificationOnlyIncludeFcmTokenList.isEmpty()) {
            sendMessageList(NotificationOnlyIncludeFcmTokenList);
        }
    }


    @Override
    public void readNotification(String uuid) {
        Notification notification = notificationQueryRepository.findNotificationByUuidAndUsername(uuid, getUsernameOrThrow())
                .orElseThrow(() -> new NotificationNotFoundException());
        notification.changeCheckStatus(true);
    }

    private String getUsernameOrThrow() {
        return securityUtil.getCurrentUsername()
                .orElseThrow(() -> new MemberNotFoundException());
    }

    @Override
    public void sendMatchingNotification(List<Member> memberList, Long teamId, NotificationType notificationType) {
        List<NotificationIncludeFcmToken> notificationIncludeFcmToken
                = NotificationIncludeFcmToken.createMatchingCompleteTypeList(memberList, teamId);
        List<NotificationIncludeFcmToken> NotificationOnlyIncludeFcmToken = notificationIncludeFcmToken.stream()
                .filter(dto -> StringUtils.hasText(dto.getFcmToken())).collect(Collectors.toList());

        saveNotification(notificationIncludeFcmToken);

        if (!NotificationOnlyIncludeFcmToken.isEmpty()) {
            sendMessageList(NotificationOnlyIncludeFcmToken);
        }
    }

    @Override
    public void sendApplyNotification(Team team) {
        List<NotificationIncludeFcmToken> notificationIncludeFcmToken = NotificationIncludeFcmToken.createTeamApplyTypeList(team);
        saveNotification(notificationIncludeFcmToken);
        List<NotificationIncludeFcmToken> NotificationOnlyIncludeFcmToken = notificationIncludeFcmToken.stream()
                .filter(dto -> StringUtils.hasText(dto.getFcmToken())).collect(Collectors.toList());

        if (!NotificationOnlyIncludeFcmToken.isEmpty()) {
            sendMessageList(NotificationOnlyIncludeFcmToken);
        }
    }

    private void saveNotification(List<NotificationIncludeFcmToken> notificationIncludeFcmToken) {
        notificationJdbcRepository.insertNotification(Notification.from(notificationIncludeFcmToken));
    }

    /**
     * message send는 한번에 최대 500개 가능
     * 메시지 요청에 따른 응답은 response에 순서대로 담겨져 온다.
     */
    private void sendMessageList(List<NotificationIncludeFcmToken> NotificationOnlyIncludeFcmToken) {
        List<Message> messageList = createMessages(NotificationOnlyIncludeFcmToken);
        List<String> fcmTokenList = NotificationOnlyIncludeFcmToken.stream().map(dto -> dto.getFcmToken()).collect(Collectors.toList());
        List<List<String>> fcmPartition = Lists.partition(fcmTokenList, 500);
        List<List<Message>> messagesPartition = Lists.partition(messageList, 500);

        for (int i = 0; i < fcmPartition.size(); i++) {
            BatchResponse response;
            try {
                response = FirebaseMessaging.getInstance().sendAll(messagesPartition.get(i));
                if (response.getFailureCount() > 0) {
                    List<SendResponse> responses = response.getResponses();
                    List<String> failedTokens = new ArrayList<>();
                    for (int j = 0; j < responses.size(); j++) {
                        if (!responses.get(j).isSuccessful()) {
                            failedTokens.add(fcmPartition.get(i).get(j));
                        }
                    }
                    log.error("List of tokens are not valid FCM token : " + failedTokens);
                }
            } catch (FirebaseMessagingException e) {
                log.error("cannot send to memberList push message. error info : {}", e.getMessage());
            }
        }
    }

    private List<Message> createMessages(List<NotificationIncludeFcmToken> notificationIncludeFcmTokenList) {
        final String finalTeamId = getTeamId(notificationIncludeFcmTokenList);
        return notificationIncludeFcmTokenList.stream().map(notification -> Message.builder()
                .putData("time", LocalDateTime.now().toString())
                .putData("notificationType", notification.getNotificationType().name())
                .putData("uuid", notification.getUuid())
                .putData("teamId", finalTeamId)
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(notification.getTitle())
                        .setBody(notification.getMessage())
                        .build())
                .setToken(notification.getFcmToken())
                .build()).collect(Collectors.toList());
    }

    private String getTeamId(List<NotificationIncludeFcmToken> notificationIncludeFcmTokenList) {
        String teamId = "";
        if (notificationIncludeFcmTokenList.get(0).getTeamId() != null) {
            teamId = notificationIncludeFcmTokenList.get(0).getTeamId().toString();
        }
        return teamId;
    }
}
