package com.gjgs.gjgs.modules.payment.event;

import com.gjgs.gjgs.modules.payment.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@Async("orderCancel")
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderService orderService;
}
