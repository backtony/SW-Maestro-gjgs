package com.gjgs.gjgs.modules.exception.coupon;

import org.springframework.http.HttpStatus;

public class AlreadyUsedCouponException extends CouponException{

    private static final String MESSAGE = "이미 사용한 쿠폰입니다.";
    private static final String CODE = "COUPON-400";

    public AlreadyUsedCouponException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
