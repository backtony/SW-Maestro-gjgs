package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class AlreadyEnteredScheduledException extends ScheduleException{

    private static final String MESSAGE =  "참여 인원중에 이미 참여중인 회원이 있습니다.";
    private static final String CODE = "SCHEDULE-409";

    public AlreadyEnteredScheduledException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
