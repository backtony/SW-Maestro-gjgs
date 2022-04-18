package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class ScheduleTimeOverException extends ScheduleException{

    private static final String MESSAGE = "클래스 시작 시간이 지났습니다. 팀 신청 시, 클래스 시작 시간 한 시간 전에 신청 가능합니다.";
    private static final String CODE = "SCHEDULE-400";

    public ScheduleTimeOverException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
