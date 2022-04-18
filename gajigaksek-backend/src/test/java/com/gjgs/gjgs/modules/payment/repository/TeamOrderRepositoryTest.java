package com.gjgs.gjgs.modules.payment.repository;

import com.gjgs.gjgs.config.repository.RepositoryTest;
import com.gjgs.gjgs.modules.payment.entity.TeamOrder;
import com.gjgs.gjgs.modules.payment.enums.TeamOrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamOrderRepositoryTest extends RepositoryTest {

    @Autowired TeamOrderRepository teamOrderRepository;

    @Test
    @DisplayName("TeamOrder 가져오기")
    void find_team_order() throws Exception {

        // given
        TeamOrder teamOrder = TeamOrder.builder()
                .teamId(1L).scheduleId(2L)
                .currentPaymentCount(4).completePaymentCount(4)
                .teamOrderStatus(TeamOrderStatus.COMPLETE)
                .build();
        teamOrderRepository.save(teamOrder);

        // when
        TeamOrder find = teamOrderRepository.findByScheduleIdTeamId(teamOrder.getScheduleId(), teamOrder.getTeamId());

        // then
        assertAll(
                () -> assertEquals(find.getScheduleId(), teamOrder.getScheduleId()),
                () -> assertEquals(find.getTeamId(), teamOrder.getTeamId())
        );
    }
}