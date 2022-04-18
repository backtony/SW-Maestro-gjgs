package com.gjgs.gjgs.modules.team.services.manage;

import com.gjgs.gjgs.modules.team.dtos.DelegateLeaderResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamAppliersResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamExitResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamManageResponse;

public interface TeamManageService {

    void applyTeam(Long teamId);

    TeamAppliersResponse getTeamAppliers(Long teamId);

    TeamManageResponse acceptApplier(Long teamId, Long memberId);

    TeamManageResponse rejectApplier(Long teamId, Long memberId);

    TeamExitResponse excludeMember(Long teamId, Long memberId);

    TeamExitResponse exitMember(Long teamId);

    DelegateLeaderResponse changeLeader(Long teamId, Long becomeLeaderId);
}
