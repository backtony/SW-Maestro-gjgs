package com.gjgs.gjgs.modules.exception.lecture;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class LectureException extends ApplicationException {
    protected LectureException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
