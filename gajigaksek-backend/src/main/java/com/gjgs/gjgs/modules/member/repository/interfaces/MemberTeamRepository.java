package com.gjgs.gjgs.modules.member.repository.interfaces;

import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {

    List<MemberTeam> findByTeamId(Long teamId);
}
