package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.member.dto.myinfo.AlarmEditRequest;
import com.gjgs.gjgs.modules.member.enums.Alarm;

public class AlarmDummy {

    public static AlarmEditRequest createAlarmEditRequest(boolean isActive, Alarm type){
        return AlarmEditRequest.builder()
                .isActive(isActive)
                .type(type)
                .build();
    }

}
