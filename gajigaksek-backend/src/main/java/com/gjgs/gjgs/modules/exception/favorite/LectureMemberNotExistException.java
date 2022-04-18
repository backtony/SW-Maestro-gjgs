package com.gjgs.gjgs.modules.exception.favorite;

import org.springframework.http.HttpStatus;

public class LectureMemberNotExistException extends FavoriteException{

    private static final String MESSAGE = "존재하지 않는 찜 번호입니다.";
    private static final String CODE = "LECTURE-MEMBER-400";

    public LectureMemberNotExistException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
