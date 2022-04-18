package com.batch.redisbatch.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum PushMessage {
    MATCHING_COMPLETE(
            "매칭 알림",
            " 매칭이 완료되었습니다."),
    TEAM_APPLY(
            "팀 클래스 신청 알림",
            " 팀 리더가 클래스에 단체신청했습니다. 30분 이내로 결제를 완료해주세요."),
    TEAM_APPLY_CANCEL(
            "팀 클래스 취소 알림",
            " 30분 이내에 결제를 완료하지 않아 팀 신청이 취소되었습니다."
    )
    ;
   ;

    private String title;
    private String message;

    PushMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }


}
