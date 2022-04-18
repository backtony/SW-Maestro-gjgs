package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class ScheduleNotFoundOrNotExistParticipantException extends ScheduleException{

    private static final String MESSAGE = "해당 스케줄이 존재하지 않거나 참여자가 없습니다.";
    private static final String CODE = "SCHEDULE-404";

    public ScheduleNotFoundOrNotExistParticipantException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
