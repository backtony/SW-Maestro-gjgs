package com.gjgs.gjgs.modules.exception.zone;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ZoneException extends ApplicationException {
    protected ZoneException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
