package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class TeamNotRecruitmentException extends TeamException{

    private static final String MESSAGE = "현재 팀원이 모두 차있거나, 모집하는 상태가 아닙니다.";
    private static final String CODE = "TEAM-400";

    public TeamNotRecruitmentException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
