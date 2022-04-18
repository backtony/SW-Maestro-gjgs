package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class NotScheduleParticipantException extends ScheduleException {

    private static final String MESSAGE = "리뷰를 작성할 수 있는 클래스 참가자가 아닙니다.";
    private static final String CODE = "SCHEDULE-400";

    public NotScheduleParticipantException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
