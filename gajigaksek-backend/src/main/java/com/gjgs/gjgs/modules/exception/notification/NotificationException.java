package com.gjgs.gjgs.modules.exception.notification;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class NotificationException extends ApplicationException {
    protected NotificationException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
