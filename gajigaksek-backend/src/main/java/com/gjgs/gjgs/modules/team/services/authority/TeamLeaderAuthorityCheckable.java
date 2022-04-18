package com.gjgs.gjgs.modules.team.services.authority;

import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.utils.validators.authority.AuthorityCheckable;

public interface TeamLeaderAuthorityCheckable extends AuthorityCheckable<Team> {

    Team findTeamMembers(Long teamId);

    Team findZone(Long teamId);

    Team findLeader(Long deleteTeamId);
}
