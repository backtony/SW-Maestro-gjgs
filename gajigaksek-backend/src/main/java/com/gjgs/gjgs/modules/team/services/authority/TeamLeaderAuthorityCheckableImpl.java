package com.gjgs.gjgs.modules.team.services.authority;

import com.gjgs.gjgs.modules.exception.team.TeamNotFoundException;
import com.gjgs.gjgs.modules.team.aop.CheckLeader;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamLeaderAuthorityCheckableImpl implements TeamLeaderAuthorityCheckable {

    private final TeamQueryRepository teamQueryRepository;

    @CheckLeader
    @Override
    public Team findTeamMembers(Long teamId) {
        return teamQueryRepository.findWithTeamMembers(teamId).orElseThrow(() -> new TeamNotFoundException());
    }

    @CheckLeader
    @Override
    public Team findZone(Long teamId) {
        return teamQueryRepository.findWithLeaderZoneCategories(teamId).orElseThrow(() -> new TeamNotFoundException());
    }

    @CheckLeader
    @Override
    public Team findLeader(Long deleteTeamId) {
        return teamQueryRepository.findWithLeader(deleteTeamId).orElseThrow(() -> new TeamNotFoundException());
    }
}
