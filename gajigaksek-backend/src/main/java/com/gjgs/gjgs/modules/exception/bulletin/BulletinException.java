package com.gjgs.gjgs.modules.exception.bulletin;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class BulletinException extends ApplicationException {

    protected BulletinException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
