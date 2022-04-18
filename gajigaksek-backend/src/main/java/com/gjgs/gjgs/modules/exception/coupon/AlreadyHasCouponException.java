package com.gjgs.gjgs.modules.exception.coupon;

import org.springframework.http.HttpStatus;

public class AlreadyHasCouponException extends CouponException{


    private static final String MESSAGE = "회원이 이미 쿠폰을 갖고 있습니다.";
    private static final String CODE = "COUPON-409";

    public AlreadyHasCouponException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
