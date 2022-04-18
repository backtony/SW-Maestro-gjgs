package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class TeamNotFoundOrNotLeaderException extends TeamException{

    private static final String MESSAGE = "존재하지 않는 팀이거나 팀장 권한이 없는 팀입니다.";
    private static final String CODE = "TEAM-403";

    public TeamNotFoundOrNotLeaderException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}
