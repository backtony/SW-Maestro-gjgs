package com.gjgs.gjgs.modules.exception.coupon;

import org.springframework.http.HttpStatus;

public class InvalidCouponException extends CouponException{

    private static final String MESSAGE = "해당 쿠폰은 사용이 중지되었습니다.";
    private static final String CODE = "COUPON-400";


    public InvalidCouponException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
