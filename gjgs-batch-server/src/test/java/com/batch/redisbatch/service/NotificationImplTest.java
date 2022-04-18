package com.batch.redisbatch.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.batch.redisbatch.domain.Notification;
import com.batch.redisbatch.dto.MemberFcmDto;
import com.batch.redisbatch.repository.interfaces.MemberQueryRepository;
import com.batch.redisbatch.repository.interfaces.NotificationRepository;
import com.batch.redisbatch.service.impl.NotificationServiceImpl;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationImplTest {

    @InjectMocks NotificationServiceImpl notificationService;
    @Mock NotificationRepository notificationRepository;
    @Mock MemberQueryRepository memberQueryRepository;

    static FirebaseOptions TEST_OPTIONS = FirebaseOptions.builder()
            .setCredentials(new MockGoogleCredentials())
            .setProjectId("test-project")
            .build();

    @BeforeAll
    static void init(){
        FirebaseApp.initializeApp(TEST_OPTIONS);
    }


    @DisplayName("30분 지나서 취소 알림")
    @Test
    void sendOrderCancelNotification() throws Exception{
        //given
        MemberFcmDto dto = MemberFcmDto.builder()
                .id(1L)
                .fcmToken("fcmToken")
                .build();

        Notification notification = Notification.createTeamOrderCancelNotification(dto);

        when(memberQueryRepository.findOnlyFcmById(any())).thenReturn(Optional.of(dto));
        when(notificationRepository.save(any())).thenReturn(notification);

        //when
        notificationService.sendOrderCancelNotification(1L);


        //then
        verify(notificationRepository,times(1)).save(any());

    }

    @DisplayName("취소 메시지에서 맴버 존재하지 않을 때")
    @Test
    void sendOrderCancelNotificationFail() throws Exception{

        //given
        when(memberQueryRepository.findOnlyFcmById(any())).thenReturn(Optional.empty());
        Logger fooLogger = (Logger) LoggerFactory.getLogger(NotificationServiceImpl.class);
        // create and start a ListAppender
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        // add the appender to the logger
        fooLogger.addAppender(listAppender);

        //when
        notificationService.sendOrderCancelNotification(1L);

        //then
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(Level.ERROR,logsList.get(0).getLevel());
        assertEquals("해당 id의 Member가 FcmToken을 갖고 있지 않습니다.",logsList.get(0).getMessage());
    }


    private static class MockGoogleCredentials extends GoogleCredentials {

        @Override
        public AccessToken refreshAccessToken() {
            Date expiry = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
            return new AccessToken(UUID.randomUUID().toString(), expiry);
        }
    }

}
