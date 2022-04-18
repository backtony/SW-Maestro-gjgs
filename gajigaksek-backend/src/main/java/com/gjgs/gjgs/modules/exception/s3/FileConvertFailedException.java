package com.gjgs.gjgs.modules.exception.s3;

import org.springframework.http.HttpStatus;

public class FileConvertFailedException extends S3Exception{

    private static final String MESSAGE =  "MultipartFile -> File로 전환이 실패했습니다.";
    private static final String CODE = "S3-405";

    public FileConvertFailedException() {
        super(CODE, HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
