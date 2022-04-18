package com.gjgs.gjgs.modules.exception.schedule;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class ScheduleException extends ApplicationException {
    protected ScheduleException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
