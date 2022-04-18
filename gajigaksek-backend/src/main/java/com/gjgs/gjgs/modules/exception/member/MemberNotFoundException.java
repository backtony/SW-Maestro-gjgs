package com.gjgs.gjgs.modules.exception.member;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberNotFoundException extends MemberException{

    public static final String MESSAGE = "탈퇴했거나 존재하지 않는 회원입니다.";
    public static final String CODE = "MEMBER-401";

    public MemberNotFoundException() {
        super(CODE, HttpStatus.UNAUTHORIZED, MESSAGE);
    }
}
