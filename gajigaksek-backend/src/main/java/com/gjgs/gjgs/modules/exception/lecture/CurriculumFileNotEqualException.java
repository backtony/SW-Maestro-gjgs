package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;


public class CurriculumFileNotEqualException extends LectureException {

    private static final String MESSAGE = "사진의 갯수와 등록할 커리큘럼의 갯수가 일치하지 않습니다.";
    private static final String CODE = "LECTURE-400";

    public CurriculumFileNotEqualException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
