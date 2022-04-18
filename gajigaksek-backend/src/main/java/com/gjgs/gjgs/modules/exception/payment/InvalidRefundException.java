package com.gjgs.gjgs.modules.exception.payment;

import org.springframework.http.HttpStatus;

public class InvalidRefundException extends PaymentException{

    private static final String MESSAGE = "결제했던 할인 정보들이 일치하지 않습니다.";
    private static final String CODE = "PAYMENT-400";

    public InvalidRefundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
