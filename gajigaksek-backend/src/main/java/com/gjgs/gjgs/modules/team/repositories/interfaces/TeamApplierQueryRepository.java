package com.gjgs.gjgs.modules.team.repositories.interfaces;

import com.gjgs.gjgs.modules.team.dtos.TeamAppliersResponse;
import com.gjgs.gjgs.modules.team.entity.TeamApplier;

import java.util.List;

public interface TeamApplierQueryRepository {

    List<TeamApplier> findWithTeamLeaderByTeamId(Long teamId);

    long deleteApplier(Long teamId, Long applierId);

    TeamAppliersResponse findAppliers(Long teamId);

    List<Long> findApplierMemberIdList(Long teamId);
}
