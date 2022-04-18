package com.batch.redisbatch.service.impl;

import com.batch.redisbatch.domain.Notification;
import com.batch.redisbatch.dto.MemberFcmDto;
import com.batch.redisbatch.repository.interfaces.MemberQueryRepository;
import com.batch.redisbatch.repository.interfaces.NotificationRepository;
import com.batch.redisbatch.service.interfaces.NotificationService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final MemberQueryRepository memberQueryRepository;
    private final NotificationRepository notificationRepository;

    // Firebase Admin SDK 라이브러리는 FCM HTTP v1 API를 기반으로 동작한다.
    // HTTP v1 전송 요청의 경우 기존 요청에서 사용하는 서버 키 문자열 대신 OAuth 2.0 액세스 토큰이 필요합니다. Admin SDK를 사용하여 메시지를 보내는 경우 라이브러리에서 토큰이 자동으로 처리됩니다.

    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;

    // https://developers.google.com/identity/protocols/oauth2/scopes#fcm
    @Value("${fcm.key.scope}")
    private String fireBaseScope;

    // fcm 기본 설정 진행
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
            // spring 뜰때 알림 서버가 잘 동작하지 않는 것이므로 바로 죽임
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void sendOrderCancelNotification(Long memberId) {
        Optional<MemberFcmDto> memberFcmDto = memberQueryRepository.findOnlyFcmById(memberId);

        memberFcmDto.ifPresentOrElse(memberFcm -> {
            Notification notification = notificationRepository.save(Notification.createTeamOrderCancelNotification(memberFcm));
            if (memberFcm.getFcmToken() != null){
                sendMessage(memberFcm, notification);
            }
        },() -> log.error("해당 id의 Member가 FcmToken을 갖고 있지 않습니다."));
    }

    private void sendMessage(MemberFcmDto dto, Notification notification) {
        Message message = createMessage(dto, notification);
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e){
            log.error("cannot send to member push message. error info : {}", e.getMessage());
        }
    }

    private Message createMessage(MemberFcmDto dto, Notification notification) {
        return Message.builder()
                .putData("time", LocalDateTime.now().toString())
                .putData("notificationType", notification.getNotificationType().name())
                .putData("uuid", notification.getUuid())
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(notification.getTitle())
                        .setBody(notification.getMessage())
                        .build())
                .setToken(dto.getFcmToken())
                .build();
    }
}
