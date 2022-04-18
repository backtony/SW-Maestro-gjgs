package com.gjgs.gjgs.modules.exception.s3;

import org.springframework.http.HttpStatus;

public class GetUrlFailedException extends S3Exception{

    private static final String MESSAGE =  "파일 URL을 가져오는데 실패했습니다.";
    private static final String CODE = "S3-305";


    public GetUrlFailedException() {
        super(CODE, HttpStatus.INTERNAL_SERVER_ERROR, MESSAGE);
    }
}
