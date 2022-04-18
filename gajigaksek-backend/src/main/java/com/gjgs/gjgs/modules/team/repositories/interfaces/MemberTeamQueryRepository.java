package com.gjgs.gjgs.modules.team.repositories.interfaces;

public interface MemberTeamQueryRepository {

    Boolean existByApplierInTeamMember(Long teamId, Long memberId);
}
