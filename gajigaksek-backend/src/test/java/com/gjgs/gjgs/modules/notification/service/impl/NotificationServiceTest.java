package com.gjgs.gjgs.modules.notification.service.impl;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.NotificationDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.dummy.ZoneDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberQueryRepository;
import com.gjgs.gjgs.modules.notification.dto.MemberFcmDto;
import com.gjgs.gjgs.modules.notification.dto.NotificationCreateRequest;
import com.gjgs.gjgs.modules.notification.entity.Notification;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.notification.enums.TargetType;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationJdbcRepository;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationQueryRepository;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks NotificationServiceImpl notificationService;
    @Mock MemberQueryRepository memberQueryRepository;
    @Mock NotificationJdbcRepository notificationJdbcRepository;
    @Mock NotificationQueryRepository notificationQueryRepository;
    @Mock SecurityUtil securityUtil;

    static FirebaseOptions TEST_OPTIONS = FirebaseOptions.builder()
            .setCredentials(new MockGoogleCredentials())
            .setProjectId("test-project")
            .build();

    String title = "title";
    String message = "message";


    @BeforeAll
    static void init(){
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(TEST_OPTIONS);
        }
    }

    @DisplayName("매칭 알림 보내기")
    @Test
    void send_matching_notification() throws Exception{
        //given
        Long teamId = 1L;
        Member member = MemberDummy.createTestMember();

        //when
        notificationService.sendMatchingNotification(List.of(member),teamId, NotificationType.MATCHING_COMPLETE);

        //then
        verify(notificationJdbcRepository).insertNotification(any());
    }


    @DisplayName("admin 알림 보내기")
    @Test
    void send_custom_notification() throws Exception{
        //given
        NotificationCreateRequest notificationCreateRequest = createNotificationCreateRequest();
        List<MemberFcmDto> memberFcmDtoList = createMemberFcmDtoList();

        when(memberQueryRepository.findMemberFcmDtoByTargetTypeAndMemberIdListAndMemberEventAlarm(any(),any(), ArgumentMatchers.anyBoolean())).thenReturn(memberFcmDtoList);

        //when
        notificationService.sendCustomNotification(notificationCreateRequest);

        //then
        verify(memberQueryRepository).findMemberFcmDtoByTargetTypeAndMemberIdListAndMemberEventAlarm(any(),any(),ArgumentMatchers.anyBoolean());
        verify(notificationJdbcRepository).insertNotification(any());
    }

    private List<MemberFcmDto> createMemberFcmDtoList() {
        String fcmToken ="fcmToken";
        MemberFcmDto mfd1 = MemberDummy.createMemberFcmDto(1L,fcmToken);
        MemberFcmDto mfd2 = MemberDummy.createMemberFcmDto(2L,fcmToken);
        List<MemberFcmDto> memberFcmDtoList = List.of(mfd1, mfd2);
        return memberFcmDtoList;
    }

    private NotificationCreateRequest createNotificationCreateRequest() {
        TargetType targetType = TargetType.ALL;
        List<Long> memberIdList = List.of(1L, 2L);
        NotificationCreateRequest notificationCreateRequest
                = NotificationDummy.createNotificationForm(title,message,targetType,memberIdList);
        return notificationCreateRequest;
    }

    @DisplayName("알림 읽음 처리")
    @Test
    void read_notification() throws Exception{
        //given
        Member member = MemberDummy.createTestMember();
        Notification notification = NotificationDummy.createNotification(member);
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of("test"));
        when(notificationQueryRepository.findNotificationByUuidAndUsername(any(),any())).thenReturn(Optional.of(notification));

        //when
        notificationService.readNotification(notification.getUuid());

        //then
        assertTrue(notification.isChecked());
    }

    @DisplayName("단체 신청 알림")
    @Test
    void send_apply_notification() throws Exception{
        //given
        Member member = MemberDummy.createTestMember();
        Team team = TeamDummy.createTeam(member, ZoneDummy.createZone(1L));

        //when
        notificationService.sendApplyNotification(team);

        //then
        verify(notificationJdbcRepository,times(1)).insertNotification(any());

    }



    private static class MockGoogleCredentials extends GoogleCredentials {

        @Override
        public AccessToken refreshAccessToken() {
            Date expiry = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
            return new AccessToken(UUID.randomUUID().toString(), expiry);
        }
    }
}
