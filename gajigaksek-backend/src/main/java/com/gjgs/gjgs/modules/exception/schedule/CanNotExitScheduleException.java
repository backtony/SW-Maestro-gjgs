package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class CanNotExitScheduleException extends ScheduleException{

    private static final String MESSAGE = "모집 확정 상태이거나 클래스를 이미 진행했던 경우는 클래스 신청을 취소할 수 없습니다.";
    private static final String CODE = "SCHEDULE-409";

    public CanNotExitScheduleException() {
        super(CODE, HttpStatus.CONFLICT, MESSAGE);
    }
}
