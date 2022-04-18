package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class TemporaryNotSaveLectureException extends LectureException{
    private static final String MESSAGE = "디렉터가 임시 저장한 클래스 정보가 없습니다.";
    private static final String CODE = "LECTURE-404";

    public TemporaryNotSaveLectureException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
