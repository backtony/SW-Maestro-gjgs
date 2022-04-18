package com.gjgs.gjgs.modules.exception.matching;

import org.springframework.http.HttpStatus;

public class AlreadyMatchingMemberException extends MatchingException{

    private static final String MESSAGE = "이미 매칭이 진행중인 회원입니다.";
    private static final String CODE = "MATCH-400";

    public AlreadyMatchingMemberException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
