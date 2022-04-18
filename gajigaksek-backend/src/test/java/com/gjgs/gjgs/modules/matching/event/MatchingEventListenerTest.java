package com.gjgs.gjgs.modules.matching.event;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.notification.service.interfaces.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MatchingEventListenerTest {

    @InjectMocks MatchingEventListener matchingEventListener;

    @Mock NotificationService notificationService;


    @DisplayName("매칭 완료 알림")
    @Test
    void handle_matching_complete_event() throws Exception{
        //given
        Member m1 = createMemberByFcmToken("test1");
        Member m2 = createMemberByFcmToken("test2");

        // when
        MatchingCompleteEvent matchingCompleteEvent = new MatchingCompleteEvent(Arrays.asList(m1, m2), 1L);

        //when
        matchingEventListener.handleMatchingCompleteEvent(matchingCompleteEvent);

        //then
        verify(notificationService).sendMatchingNotification(any(),any(),any());
    }

    private Member createMemberByFcmToken(String test1) {
        return Member.builder()
                .fcmToken(test1)
                .build();
    }
}
