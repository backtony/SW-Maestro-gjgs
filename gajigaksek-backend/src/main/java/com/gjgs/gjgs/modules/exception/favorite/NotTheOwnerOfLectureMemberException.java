package com.gjgs.gjgs.modules.exception.favorite;

import org.springframework.http.HttpStatus;

public class NotTheOwnerOfLectureMemberException extends FavoriteException{


    private static final String MESSAGE = "해당 찜 번호의 소유자가 아닙니다.";
    private static final String CODE = "LECTURE-MEMBER-403";

    public NotTheOwnerOfLectureMemberException() {
        super(CODE, HttpStatus.FORBIDDEN, MESSAGE);
    }
}
