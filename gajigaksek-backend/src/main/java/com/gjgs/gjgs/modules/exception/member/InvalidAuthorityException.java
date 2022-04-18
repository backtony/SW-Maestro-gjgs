package com.gjgs.gjgs.modules.exception.member;

import org.springframework.http.HttpStatus;

public class InvalidAuthorityException extends MemberException{

    private static final String MESSAGE = "권한이 없습니다.";
    private static final String CODE = "MEMBER-403";

    public InvalidAuthorityException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}
