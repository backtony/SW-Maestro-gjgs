package com.gjgs.gjgs.modules.exception.s3;

import org.springframework.http.HttpStatus;

public class UploadFailException extends S3Exception{

    private static final String MESSAGE =  "파일 업로드에 실패했습니다.";
    private static final String CODE = "S3-405";


    public UploadFailException() {
        super(CODE, HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
