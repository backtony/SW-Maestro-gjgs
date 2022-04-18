package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class AlreadyInTeamException extends TeamException{

    private static final String MESSAGE = "현재 신청자는 이 그룹에 속해있습니다.";
    private static final String CODE = "TEAM-409";

    public AlreadyInTeamException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
