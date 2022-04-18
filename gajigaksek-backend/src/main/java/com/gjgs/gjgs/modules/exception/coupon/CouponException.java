package com.gjgs.gjgs.modules.exception.coupon;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class CouponException extends ApplicationException {
    protected CouponException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
