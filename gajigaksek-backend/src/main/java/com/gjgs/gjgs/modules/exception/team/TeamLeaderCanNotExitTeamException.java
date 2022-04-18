package com.gjgs.gjgs.modules.exception.team;

import org.springframework.http.HttpStatus;

public class TeamLeaderCanNotExitTeamException extends TeamException{

    private static final String MESSAGE = "팀장은 팀을 나갈 수 없습니다. 팀을 삭제할 수 있습니다.";
    private static final String CODE = "TEAM-400";

    public TeamLeaderCanNotExitTeamException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
