package com.gjgs.gjgs.modules.exception.s3;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class S3Exception extends ApplicationException {
    protected S3Exception(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
