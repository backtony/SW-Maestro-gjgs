package com.batch.redisbatch.domain;

import com.batch.redisbatch.enums.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    @DisplayName("상태 변경")
    @Test
    void changeStatus() throws Exception{
        //given
        Order order = Order.builder()
                .orderStatus(OrderStatus.CANCEL)
                .build();

        //when
        order.changeStatus(OrderStatus.WAIT);

        //then
        assertEquals(OrderStatus.WAIT,order.getOrderStatus());
    }
}
