package com.gjgs.gjgs.modules.notification.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum PushMessage implements EnumType {
    MATCHING_COMPLETE(
            "매칭 완료 알림",
            " 매칭이 완료되었습니다."),
    TEAM_APPLY(
            "팀 클래스 신청 알림",
            " 팀 리더가 클래스에 단체신청했습니다. 30분 이내로 결제를 완료해주세요.")
    ;
   ;

    private String title;
    private String message;

    PushMessage(String title, String message) {
        this.title = title;
        this.message = message;
    }


    @Override
    public String getDescription() {
        return this.title;
    }

    @Override
    public String getName() {
        return this.name();
    }


}
