package com.gjgs.gjgs.modules.team.repositories.interfaces;

import com.gjgs.gjgs.modules.team.dtos.MyLeadTeamsResponse;
import com.gjgs.gjgs.modules.team.dtos.MyTeamListResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamDetailResponse;
import com.gjgs.gjgs.modules.team.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamQueryRepository {

    Optional<Team> findWithTeamMembers(Long teamId);

    Optional<Team> findWithLeaderZoneCategories(Long teamId);

    List<Team> findMyAllTeamByUsername(String username);

    Optional<Team> findWithLeader(Long deleteTeamId);

    long deleteTeamMembers(Long teamId);

    long deleteTeamCategories(Long teamId);

    long deleteTeamLectures(Long teamId);

    long deleteTeamAppliers(Long teamId);

    MyLeadTeamsResponse findMyTeamByUsername(String username);

    TeamDetailResponse findTeamDetail(Long teamId);

    MyTeamListResponse findMyTeamListByUsername(String username);

    long deleteTeamMember(Long teamId, Long memberId);

    Boolean existByIdLeaderUsername(Long teamId, String leaderUsername);
}
