package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class LectureNotFoundException extends LectureException{

    private static final String MESSAGE = "해당 클래스가 존재하지 않습니다.";
    private static final String CODE = "LECTURE-404";

    public LectureNotFoundException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
