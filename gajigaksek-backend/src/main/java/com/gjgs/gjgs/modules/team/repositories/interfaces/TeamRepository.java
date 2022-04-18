package com.gjgs.gjgs.modules.team.repositories.interfaces;

import com.gjgs.gjgs.modules.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select distinct t from Team t left join fetch t.leader left join fetch t.teamMembers tm " +
            "left join fetch tm.member where t.id =:teamId")
    Optional<Team> findWithMembersAndLeaderById(@Param("teamId") Long teamId);

}