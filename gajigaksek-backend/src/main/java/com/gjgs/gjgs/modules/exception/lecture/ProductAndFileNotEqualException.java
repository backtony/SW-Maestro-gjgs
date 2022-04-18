package com.gjgs.gjgs.modules.exception.lecture;

import org.springframework.http.HttpStatus;

public class ProductAndFileNotEqualException extends LectureException{

    private static final String MESSAGE = "사진의 갯수와 완성작 혹은 커리큘럼 정보의 갯수가 일치하지 않습니다.";
    private static final String CODE = "LECTURE-400";

    public ProductAndFileNotEqualException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
