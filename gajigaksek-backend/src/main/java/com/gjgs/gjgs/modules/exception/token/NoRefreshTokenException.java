package com.gjgs.gjgs.modules.exception.token;

import org.springframework.http.HttpStatus;

public class NoRefreshTokenException extends TokenException{

    private static final String MESSAGE = "헤더에 RefreshToken 토큰이 없습니다.";
    private static final String CODE = "TOKEN-401";

    public NoRefreshTokenException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
