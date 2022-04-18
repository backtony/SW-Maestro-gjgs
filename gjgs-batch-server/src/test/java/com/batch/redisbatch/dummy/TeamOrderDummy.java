package com.batch.redisbatch.dummy;

import com.batch.redisbatch.domain.TeamOrder;
import com.batch.redisbatch.enums.TeamOrderStatus;

public class TeamOrderDummy {

    public static TeamOrder createTeamOrder(Long teamId, Long scheduleId, TeamOrderStatus teamOrderStatus){
        return TeamOrder.builder()
                .teamId(teamId)
                .scheduleId(scheduleId)
                .completePaymentCount(4)
                .currentPaymentCount(4)
                .teamOrderStatus(teamOrderStatus)
                .build();
    }
}
