package com.gjgs.gjgs.modules.exception.payment;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class PaymentException extends ApplicationException {
    protected PaymentException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
