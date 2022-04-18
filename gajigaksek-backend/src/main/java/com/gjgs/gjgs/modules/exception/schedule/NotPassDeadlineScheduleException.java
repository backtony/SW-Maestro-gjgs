package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class NotPassDeadlineScheduleException extends ScheduleException {

    private static final String MESSAGE = "현재 시간이 클래스 마감 시간을 지나지 않았습니다.";
    private static final String CODE = "SCHEDULE-400";

    public NotPassDeadlineScheduleException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
