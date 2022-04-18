package com.gjgs.gjgs.modules.exception.token;

import org.springframework.http.HttpStatus;

public class TokenTypeException extends TokenException{

    private static final String MESSAGE = "Token Type이 Bearer이 아닙니다.";
    private static final String CODE = "KAKAO-401";

    public TokenTypeException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
