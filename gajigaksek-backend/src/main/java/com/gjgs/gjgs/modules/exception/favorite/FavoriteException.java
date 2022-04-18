package com.gjgs.gjgs.modules.exception.favorite;

import com.gjgs.gjgs.modules.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class FavoriteException extends ApplicationException {
    protected FavoriteException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
