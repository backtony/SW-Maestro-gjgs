package com.gjgs.gjgs.modules.exception.team;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class TeamException extends ApplicationException {
    protected TeamException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
