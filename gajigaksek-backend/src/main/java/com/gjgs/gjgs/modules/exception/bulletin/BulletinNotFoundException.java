package com.gjgs.gjgs.modules.exception.bulletin;

import org.springframework.http.HttpStatus;

public class BulletinNotFoundException extends BulletinException{

    private static final String MESSAGE = "해당 모집 게시글이 존재하지 않습니다.";
    private static final String CODE = "BULLETIN-404";

    public BulletinNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
