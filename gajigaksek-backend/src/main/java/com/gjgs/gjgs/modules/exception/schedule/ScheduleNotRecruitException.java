package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class ScheduleNotRecruitException extends ScheduleException{

    private static final String MESSAGE = "모집 중인 상태만 참여할 수 있습니다.";
    private static final String CODE = "SCHEDULE-400";

    public ScheduleNotRecruitException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
