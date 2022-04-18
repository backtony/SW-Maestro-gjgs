package com.gjgs.gjgs.modules.exception.token;

import org.springframework.http.HttpStatus;

public class NoKakaoAccessTokenException extends TokenException{

    private static final String MESSAGE = "헤더에 KakaoAccessToken 토큰이 없습니다.";
    private static final String CODE = "KAKAO-401";

    public NoKakaoAccessTokenException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
