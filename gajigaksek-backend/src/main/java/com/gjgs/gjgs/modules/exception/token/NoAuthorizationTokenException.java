package com.gjgs.gjgs.modules.exception.token;

import org.springframework.http.HttpStatus;

public class NoAuthorizationTokenException extends TokenException{

    private static final String MESSAGE = "헤더에 Authorization 토큰이 없습니다.";
    private static final String CODE = "TOKEN-401";

    public NoAuthorizationTokenException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
