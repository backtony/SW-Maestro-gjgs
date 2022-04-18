package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends TeamException{

    private static final String MESSAGE = "해당 팀이 존재하지 않습니다.";
    private static final String CODE = "TEAM-404";

    public TeamNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
