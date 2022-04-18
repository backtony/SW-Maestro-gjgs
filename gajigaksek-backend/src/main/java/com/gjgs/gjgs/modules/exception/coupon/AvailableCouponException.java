package com.gjgs.gjgs.modules.exception.coupon;

import org.springframework.http.HttpStatus;

public class AvailableCouponException extends CouponException{

    private static final String MESSAGE = "아직 쿠폰이 발급중입니다. 재발행을 원할 경우 발행을 중지해주세요.";
    private static final String CODE = "COUPON-409";

    public AvailableCouponException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
