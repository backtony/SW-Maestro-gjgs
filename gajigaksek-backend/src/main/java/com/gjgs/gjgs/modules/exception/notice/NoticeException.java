package com.gjgs.gjgs.modules.exception.notice;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class NoticeException extends ApplicationException {
    protected NoticeException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
