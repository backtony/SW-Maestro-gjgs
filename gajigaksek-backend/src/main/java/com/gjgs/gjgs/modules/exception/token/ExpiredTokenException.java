package com.gjgs.gjgs.modules.exception.token;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends TokenException{

    private static final String MESSAGE = "만료된 토큰입니다.";
    private static final String CODE = "TOKEN-401";

    public ExpiredTokenException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
