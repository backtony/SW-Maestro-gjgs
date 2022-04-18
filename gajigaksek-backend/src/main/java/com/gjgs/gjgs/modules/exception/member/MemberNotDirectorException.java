package com.gjgs.gjgs.modules.exception.member;

import org.springframework.http.HttpStatus;

public class MemberNotDirectorException extends MemberException{

    private static final String MESSAGE = "회원은 디렉터가 아닙니다.";
    private static final String CODE = "MEMBER-403";

    public MemberNotDirectorException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}
