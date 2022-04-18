package com.gjgs.gjgs.modules.exception.reward;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;


public abstract class RewardException extends ApplicationException {

    protected RewardException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
