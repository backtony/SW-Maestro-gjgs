package com.gjgs.gjgs.modules.exception.payment;

import org.springframework.http.HttpStatus;

public class NotMatchRefundPriceException extends PaymentException{

    private static final String MESSAGE = "아임포트에서 결제한 금액과 환불 금액이 일치하지 않아 환불되었습니다.";
    private static final String CODE = "ORDER-409";

    public NotMatchRefundPriceException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
