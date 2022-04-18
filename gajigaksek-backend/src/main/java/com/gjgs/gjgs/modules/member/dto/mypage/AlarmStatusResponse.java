package com.gjgs.gjgs.modules.member.dto.mypage;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class AlarmStatusResponse {

    private boolean eventAlarm;

    @QueryProjection
    public AlarmStatusResponse(boolean eventAlarm) {
        this.eventAlarm = eventAlarm;
    }
}
