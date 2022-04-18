package com.gjgs.gjgs.modules.exception.s3;

import org.springframework.http.HttpStatus;

public class FileDeleteFailedException extends S3Exception{

    private static final String MESSAGE =  "파일 삭제에 실패했습니다.";
    private static final String CODE = "S3-405";

    public FileDeleteFailedException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
