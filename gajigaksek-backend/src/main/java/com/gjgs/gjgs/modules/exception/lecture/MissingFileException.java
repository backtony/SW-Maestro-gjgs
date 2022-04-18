package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class MissingFileException extends LectureException{

    public static final String MESSAGE = "사진 파일이 존재하지 않습니다.";
    public static final String CODE = "FILE-400";

    public MissingFileException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
