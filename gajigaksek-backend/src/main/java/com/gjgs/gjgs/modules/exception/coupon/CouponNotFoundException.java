package com.gjgs.gjgs.modules.exception.coupon;

import org.springframework.http.HttpStatus;

public class CouponNotFoundException extends CouponException{


    private static final String MESSAGE = "해당 클래스에 적용할 수 있는 쿠폰이 없습니다.";
    private static final String CODE = "COUPON-404";

    public CouponNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
