package com.gjgs.gjgs.modules.payment.controller;

import com.gjgs.gjgs.modules.payment.dto.*;
import com.gjgs.gjgs.modules.payment.service.order.OrderService;
import com.gjgs.gjgs.modules.payment.service.pay.PaymentServiceFactory;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment/{payType}")
@PreAuthorize("hasAnyRole('USER,DIRECTOR')")// isauthenticated
public class PaymentController {

    private final PaymentServiceFactory paymentServiceFactory;
    private final OrderService orderService;

    @GetMapping("/{scheduleId}")
    public ResponseEntity<TeamMemberPaymentResponse> getTeamPayments(@PathVariable PayType payType,
                                                                     @PathVariable Long scheduleId) {
        return ResponseEntity.ok(orderService.getTeamMemberPayment(scheduleId));
    }

    @PostMapping("/{scheduleId}")
    public ResponseEntity<OrderIdDto> payments(@PathVariable PayType payType,
                                               @PathVariable Long scheduleId,
                                               @RequestBody @Validated PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentServiceFactory.getProcess(payType).payProcess(scheduleId, paymentRequest));
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity<OrderIdDto> verifyPayment(@PathVariable PayType payType,
                              @PathVariable Long scheduleId,
                              @RequestBody @Validated PaymentVerifyRequest request) throws IamportResponseException, IOException {

        return ResponseEntity.ok(orderService.verifyAndCompletePayment(request));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelPayment(@PathVariable PayType payType,
                                              @PathVariable Long orderId) throws IamportResponseException, IOException {
        paymentServiceFactory.getProcess(payType).cancelProcess(orderId);
        return ResponseEntity.ok().build();
    }
}
