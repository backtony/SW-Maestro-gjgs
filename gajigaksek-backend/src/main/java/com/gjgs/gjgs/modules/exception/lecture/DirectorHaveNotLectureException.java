package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class DirectorHaveNotLectureException extends LectureException{

    private static final String MESSAGE = "디렉터가 만든 클래스가 없습니다.";
    private static final String CODE = "LECTURE-404";

    public DirectorHaveNotLectureException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
