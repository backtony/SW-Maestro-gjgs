package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class InvalidApplyLectureScheduleException extends LectureException{

    private static final String MESSAGE = "모집중인 스케줄이 아니거나 종료된 클래스입니다.";
    private static final String CODE = "LECTURE-400";

    public InvalidApplyLectureScheduleException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
