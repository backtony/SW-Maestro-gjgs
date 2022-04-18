package com.gjgs.gjgs.modules.exception.member;

import org.springframework.http.HttpStatus;

public class MemberNotAdminException extends MemberException{

    private static final String MESSAGE = "회원이 어드민이 아닙니다.";
    private static final String CODE = "MEMBER-403";

    public MemberNotAdminException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}
