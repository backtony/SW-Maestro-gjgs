package com.gjgs.gjgs.modules.exception.payment;

import org.springframework.http.HttpStatus;

public class CanceledOrderException extends PaymentException{

    private static final String MESSAGE = "해당 결제는 취소되었습니다. 다시 결제해주세요.";
    private static final String CODE = "ORDER-400";

    public CanceledOrderException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
