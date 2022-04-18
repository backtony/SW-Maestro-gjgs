package com.gjgs.gjgs.modules.team.services.crud;

import com.gjgs.gjgs.modules.team.dtos.*;

public interface TeamCrudService {

    CreateTeamResponse createTeam(CreateTeamRequest createTeamRequest);

    ModifyTeamResponse modifyTeam(Long teamId, CreateTeamRequest modifyTeamRequest);

    MyTeamListResponse getMyTeamList();

    TeamDetailResponse getTeamDetail(Long teamId);

    MyLeadTeamsResponse getMyLeadTeamWithBulletinLecture();

    TeamExitResponse deleteTeam(Long teamId);
}
