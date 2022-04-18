package com.batch.redisbatch.event;


import com.batch.redisbatch.service.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Async("cancel")
@RequiredArgsConstructor
public class OrderCancelEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void handleOrderCancelNotification(OrderCancelEvent orderCancelEvent){
            notificationService.sendOrderCancelNotification(orderCancelEvent.getMemberId());
    }
}
