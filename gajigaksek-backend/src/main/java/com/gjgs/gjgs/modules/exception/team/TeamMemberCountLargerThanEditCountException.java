package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class TeamMemberCountLargerThanEditCountException extends TeamException {

    private static final String MESSAGE = "팀의 현재 인원이 수정할 최대 인원 보다 많습니다.";
    private static final String CODE = "TEAM-400";

    public TeamMemberCountLargerThanEditCountException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
