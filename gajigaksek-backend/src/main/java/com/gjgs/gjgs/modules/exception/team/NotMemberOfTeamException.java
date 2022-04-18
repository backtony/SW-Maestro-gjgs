package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class NotMemberOfTeamException extends TeamException{

    private static final String MESSAGE = "팀의 멤버가 아닙니다.";
    private static final String CODE = "TEAM-404";

    public NotMemberOfTeamException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
