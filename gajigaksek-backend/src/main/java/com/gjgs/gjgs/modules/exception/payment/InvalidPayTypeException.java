package com.gjgs.gjgs.modules.exception.payment;

import org.springframework.http.HttpStatus;

public class InvalidPayTypeException extends PaymentException{

    private static final String MESSAGE = "존재하지 않는 결제 유형입니다.";
    private static final String CODE = "PAYMENT-404";

    public InvalidPayTypeException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
