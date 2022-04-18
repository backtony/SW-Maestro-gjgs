package com.batch.redisbatch.event;


import com.batch.redisbatch.service.interfaces.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderCancelEventListenerTest {

    @InjectMocks OrderCancelEventListener orderCancelEventListener;
    @Mock NotificationService notificationService;


    @DisplayName("취소 알림")
    @Test
    void handleOrderCancelNotification() throws Exception{
        //given

        //when
        orderCancelEventListener.handleOrderCancelNotification(new OrderCancelEvent(1L));

        //then
        verify(notificationService,times(1)).sendOrderCancelNotification(any());
    }
}
