package com.gjgs.gjgs.modules.exception.member;

import org.springframework.http.HttpStatus;

public class KakaoConnectionException extends MemberException{

    private static final String MESSAGE = "카카오와 통신에 실패했습니다.";
    private static final String CODE = "KAKAO-405";

    public KakaoConnectionException() {
        super(CODE, HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
