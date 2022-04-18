package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class ExceedScheduleParticipantException extends ScheduleException{

    private static final String MESSAGE = "신청할 수 있는 참가 인원을 초과했습니다.";
    private static final String CODE = "SCHEDULE-409";

    public ExceedScheduleParticipantException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
