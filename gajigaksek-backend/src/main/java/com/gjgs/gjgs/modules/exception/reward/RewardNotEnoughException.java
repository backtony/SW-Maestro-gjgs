package com.gjgs.gjgs.modules.exception.reward;

import org.springframework.http.HttpStatus;

public class RewardNotEnoughException extends RewardException{

    private static final String MESSAGE = "현재 회원의 리워드 소유 금액이 부족합니다.";
    private static final String CODE = "REWARD-400";

    public RewardNotEnoughException() {
        super(CODE, HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
