package com.gjgs.gjgs.modules.exception.category;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class CategoryException extends ApplicationException {
    protected CategoryException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
