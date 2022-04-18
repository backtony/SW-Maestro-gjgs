package com.gjgs.gjgs.modules.exception.review;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class ReviewException extends ApplicationException {
    protected ReviewException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
