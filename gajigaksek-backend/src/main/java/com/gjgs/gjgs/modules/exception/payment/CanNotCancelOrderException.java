package com.gjgs.gjgs.modules.exception.payment;

import org.springframework.http.HttpStatus;

public class CanNotCancelOrderException extends PaymentException{

    private static final String MESSAGE = "3일 내로 클래스가 진행될 경우 결제를 취소할 수 없습니다.";
    private static final String CODE =  "ORDER-400";

    public CanNotCancelOrderException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
