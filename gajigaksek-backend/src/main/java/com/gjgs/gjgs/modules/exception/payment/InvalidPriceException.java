package com.gjgs.gjgs.modules.exception.payment;

import org.springframework.http.HttpStatus;

public class InvalidPriceException extends PaymentException{

    private static final String MESSAGE = "결제 금액이 맞지 않습니다.";
    private static final String CODE = "PAYMENT-400";

    public InvalidPriceException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
