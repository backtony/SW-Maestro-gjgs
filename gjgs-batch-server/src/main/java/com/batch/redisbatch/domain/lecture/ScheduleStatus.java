package com.batch.redisbatch.domain.lecture;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ScheduleStatus {

    RECRUIT("모집중", "해당 스케줄을 모집하는 상황"),
    HOLD("모집 보류", "해당 스케줄 모집을 보류 / 다양한 원인에 의해 개설자가 모집을 보류할 때 사용"),
    CANCEL("모집 취소", "선택한 스케줄 모집을 취소"),
    FULL("모집 인원 마감", "모집할 인원이 다 찬 상태 / 신청자가 취소할 경우 상태 변경해주기"),
    CLOSE("모집 확정 및 마감", "모집된 신청자로 해당 모집을 확정 / 진행하는 시간이 현재 시간에서 지날 경우 자동 CLOSE, 개설자가 미리 확정할 수 있음"),
    END("진행 완료", "클래스 진행을 모두 마친 상황")
    ;

    private String simpleDescription;
    private String fullDescription;

    ScheduleStatus(String simpleDescription, String fullDescription) {
        this.simpleDescription = simpleDescription;
        this.fullDescription = fullDescription;
    }
}
