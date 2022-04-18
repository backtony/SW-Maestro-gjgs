package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class TeamMemberMaxException extends TeamException{

    private static final String MESSAGE = "현재 팀 인원이 최대 인원입니다.";
    private static final String CODE = "TEAM-400";

    public TeamMemberMaxException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
