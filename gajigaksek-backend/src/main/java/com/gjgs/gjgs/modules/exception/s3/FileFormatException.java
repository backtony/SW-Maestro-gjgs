package com.gjgs.gjgs.modules.exception.s3;

import org.springframework.http.HttpStatus;

public class FileFormatException extends S3Exception{

    private static final String MESSAGE =  "잘못된 형식의 파일 입니다.";
    private static final String CODE = "S3-400";

    public FileFormatException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
