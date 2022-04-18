package com.gjgs.gjgs.modules.exception.payment;

import org.springframework.http.HttpStatus;

public class TeamHaveWaitOrderException extends PaymentException{

    private static final String MESSAGE = "결제 진행 중인 클래스가 있을 경우 리더 위임, 팀원 추방, 팀 나가기 및 삭제를 할 수 없습니다.";
    private static final String CODE = "ORDER-400";

    public TeamHaveWaitOrderException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
