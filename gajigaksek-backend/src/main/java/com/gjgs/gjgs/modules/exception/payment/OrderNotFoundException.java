package com.gjgs.gjgs.modules.exception.payment;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends PaymentException{

    private static final String MESSAGE = "존재하지 않는 팀 결제 내역입니다.";
    private static final String CODE = "ORDER-404";

    public OrderNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
