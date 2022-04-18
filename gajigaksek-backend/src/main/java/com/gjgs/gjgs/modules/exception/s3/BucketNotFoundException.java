package com.gjgs.gjgs.modules.exception.s3;

import org.springframework.http.HttpStatus;

public class BucketNotFoundException extends S3Exception{

    private static final String MESSAGE =  "버킷이 존재하지 않아 파일 업로드에 실패했습니다.";
    private static final String CODE = "S3-405";

    public BucketNotFoundException() {
        super(CODE, HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
