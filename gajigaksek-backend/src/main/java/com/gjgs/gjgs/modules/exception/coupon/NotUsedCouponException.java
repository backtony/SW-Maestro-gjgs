package com.gjgs.gjgs.modules.exception.coupon;

import org.springframework.http.HttpStatus;

public class NotUsedCouponException extends CouponException{

    private static final String MESSAGE = "사용하지 않은 쿠폰입니다.";
    private static final String CODE = "COUPON-400";

    public NotUsedCouponException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
