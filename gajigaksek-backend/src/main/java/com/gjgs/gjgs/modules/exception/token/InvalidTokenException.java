package com.gjgs.gjgs.modules.exception.token;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends TokenException{
    private static final String MESSAGE = "정상적이지 않은 토큰입니다.";
    private static final String CODE = "KAKAO-401";

    public InvalidTokenException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
