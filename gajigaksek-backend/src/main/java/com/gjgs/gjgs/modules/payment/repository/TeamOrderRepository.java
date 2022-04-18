package com.gjgs.gjgs.modules.payment.repository;

import com.gjgs.gjgs.modules.payment.entity.TeamOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamOrderRepository extends JpaRepository<TeamOrder, Long> {

    @Query("select to from TeamOrder to where to.scheduleId = :scheduleId and to.teamId = :teamId")
    TeamOrder findByScheduleIdTeamId(@Param("scheduleId") Long scheduleId, @Param("teamId") Long teamId);
}
