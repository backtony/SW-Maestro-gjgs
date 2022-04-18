package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class ApplierNotInTeamApplierListException extends TeamException{

    private static final String MESSAGE = "신청하지 않은 회원입니다.";
    private static final String CODE = "TEAM-404";

    public ApplierNotInTeamApplierListException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
