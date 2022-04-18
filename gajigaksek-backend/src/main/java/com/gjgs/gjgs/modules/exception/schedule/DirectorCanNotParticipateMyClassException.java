package com.gjgs.gjgs.modules.exception.schedule;

import org.springframework.http.HttpStatus;

public class DirectorCanNotParticipateMyClassException extends ScheduleException{

    private static final String MESSAGE = "디렉터는 본인의 클래스를 신청할 수 없습니다.";
    private static final String CODE = "SCHEDULE-400";

    public DirectorCanNotParticipateMyClassException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
