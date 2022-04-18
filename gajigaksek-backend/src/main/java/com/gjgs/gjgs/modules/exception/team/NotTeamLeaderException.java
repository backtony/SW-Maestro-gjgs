package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class NotTeamLeaderException extends TeamException{

    private static final String MESSAGE = "팀장 권한이 없습니다.";
    private static final String CODE = "TEAM-403";

    public NotTeamLeaderException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}
