package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class ApplierInApplierListException extends TeamException{

    private static final String MESSAGE = "이미 가입신청한 팀입니다.";
    private static final String CODE = "TEAM-400";

    public ApplierInApplierListException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
