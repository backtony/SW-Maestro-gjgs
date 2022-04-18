package com.gjgs.gjgs.modules.exception.member;

import org.springframework.http.HttpStatus;

public class AuthorityForbiddenException extends MemberException{

    private static final String MESSAGE = "해당 컨텐츠를 이용할 수 없습니다.";
    private static final String CODE = "MEMBER-403";

    public AuthorityForbiddenException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
