package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class InvalidLectureException extends LectureException{

    private static final String MESSAGE = "내 클래스가 아니거나 검수가 완료된 클래스가 아닙니다.";
    private static final String CODE = "LECTURE-403";

    public InvalidLectureException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}
