package com.gjgs.gjgs.modules.exception.member;

import org.springframework.http.HttpStatus;

public class InvalidKaKaoAccessTokenException extends MemberException{
    private static final String MESSAGE = "카카오에서 사용자 정보를 가져오는데 실패했습니다.";
    private static final String CODE = "KAKAO-401";

    public InvalidKaKaoAccessTokenException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
