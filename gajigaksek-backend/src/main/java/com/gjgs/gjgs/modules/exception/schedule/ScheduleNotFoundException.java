package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class ScheduleNotFoundException extends ScheduleException{

    private static final String MESSAGE = "해당 스케줄이 존재하지 않습니다.";
    private static final String CODE = "SCHEDULE-404";

    public ScheduleNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
