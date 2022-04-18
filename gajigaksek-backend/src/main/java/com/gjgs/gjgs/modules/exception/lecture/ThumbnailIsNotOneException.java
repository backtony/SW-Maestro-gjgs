package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class ThumbnailIsNotOneException extends LectureException{

    private static final String MESSAGE = "대표 사진은 한장만 필요합니다.";
    private static final String CODE = "LECTURE-400";

    public ThumbnailIsNotOneException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
