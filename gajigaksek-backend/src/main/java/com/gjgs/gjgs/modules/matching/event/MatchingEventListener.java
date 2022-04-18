package com.gjgs.gjgs.modules.matching.event;


import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.notification.service.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Async("matching")
@RequiredArgsConstructor
public class MatchingEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleMatchingCompleteEvent(MatchingCompleteEvent matchingCompleteEvent){
        notificationService.sendMatchingNotification(matchingCompleteEvent.getMemberList(),
                                                        matchingCompleteEvent.getTeamId(),
                                                        NotificationType.MATCHING_COMPLETE);
    }
}
