package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class CanNotDeleteScheduleException extends ScheduleException{

    private static final String MESSAGE = "스케줄은 1명 이상일 경우 지울 수 없습니다.";
    private static final String CODE = "SCHEDULE-409";

    public CanNotDeleteScheduleException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
