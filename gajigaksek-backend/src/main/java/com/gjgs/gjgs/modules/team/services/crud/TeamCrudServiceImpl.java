package com.gjgs.gjgs.modules.team.services.crud;

import com.gjgs.gjgs.modules.bulletin.repositories.BulletinRepository;
import com.gjgs.gjgs.modules.category.services.interfaces.CategoryService;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.zone.ZoneNotFoundException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.team.aop.HasWaitOrder;
import com.gjgs.gjgs.modules.team.dtos.*;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamJdbcRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamQueryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import com.gjgs.gjgs.modules.team.services.authority.TeamLeaderAuthorityCheckable;
import com.gjgs.gjgs.modules.utils.aop.LoginMemberFavoriteLecture;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamCrudServiceImpl implements TeamCrudService {

    private final TeamRepository teamRepository;
    private final ZoneRepository zoneRepository;
    private final CategoryService categoryService;
    private final SecurityUtil securityUtil;
    private final TeamJdbcRepository teamJdbcRepository;
    private final TeamQueryRepository teamQueryRepository;
    private final BulletinRepository bulletinRepository;
    private final TeamLeaderAuthorityCheckable teamLeaderAuthorityCheckable;
    private final MemberRepository memberRepository;

    @Override
    public CreateTeamResponse createTeam(CreateTeamRequest createTeamRequest) {
        Long memberId = memberRepository.findIdByUsername(getUsername()).orElseThrow(() -> new MemberNotFoundException());
        Team team = Team.of(createTeamRequest, Member.from(memberId));
        putZone(createTeamRequest.getZoneId(), team);
        Team savedTeam = teamRepository.save(team);
        putCategoryList(createTeamRequest.getCategoryList(), savedTeam);
        return CreateTeamResponse.of(savedTeam.getId());
    }

    private void putZone(Long zoneId, Team team) {
        if (team.isNewTeamOrZoneIsDifferent(zoneId)) {
            Zone zone = zoneRepository.findById(zoneId).orElseThrow(() -> new ZoneNotFoundException());
            team.putZone(zone);
        }
    }

    private void putCategoryList(List<Long> categoryIdList, Team team) {
        teamQueryRepository.deleteTeamCategories(team.getId());
        List<Long> findCategoryIdList = categoryService.findAndValidCategories(categoryIdList);
        teamJdbcRepository.insertTeamCategoryList(team.getId(), findCategoryIdList);
    }

    @Override
    public ModifyTeamResponse modifyTeam(Long teamId, CreateTeamRequest modifyTeamRequest) {
        Team findTeam = teamLeaderAuthorityCheckable.findZone(teamId);
        findTeam.modify(modifyTeamRequest);
        putZone(modifyTeamRequest.getZoneId(), findTeam);
        putCategoryList(modifyTeamRequest.getCategoryList(), findTeam);
        return ModifyTeamResponse.of(findTeam.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public MyTeamListResponse getMyTeamList() {
        return teamQueryRepository.findMyTeamListByUsername(securityUtil.getCurrentUsername().get());
    }

    @Override
    @LoginMemberFavoriteLecture
    @Transactional(readOnly = true)
    public TeamDetailResponse getTeamDetail(Long teamId) {
        return teamQueryRepository.findTeamDetail(teamId);
    }

    @Override
    @Transactional(readOnly = true)
    public MyLeadTeamsResponse getMyLeadTeamWithBulletinLecture() {
        MyLeadTeamsResponse response = teamQueryRepository.findMyTeamByUsername(securityUtil.getCurrentUsername().get());

        if (response.getMyLeadTeams().isEmpty()) {
            return response;
        }

        bulletinRepository.findWithLectureByMemberId(response);
        return response;
    }

    @Override
    @HasWaitOrder
    public TeamExitResponse deleteTeam(Long deleteTeamId) {
        Team deleteTeam = teamLeaderAuthorityCheckable.findLeader(deleteTeamId);
        deleteBulletin(deleteTeamId);
        deleteTeam(deleteTeam);
        Long leaderId = deleteTeam.getLeader().getId();
        return TeamExitResponse.teamDelete(deleteTeamId, leaderId);
    }

    private void deleteBulletin(Long teamId) {
        Optional<Long> bulletinId = bulletinRepository.findIdByTeamId(teamId);
        if (bulletinId.isPresent()) {
            bulletinRepository.deleteFavoriteBulletinsById(bulletinId.get());
            bulletinRepository.deleteById(bulletinId.get());
        }
    }

    private void deleteTeam(Team deleteTeam) {
        teamQueryRepository.deleteTeamAppliers(deleteTeam.getId());
        teamQueryRepository.deleteTeamMembers(deleteTeam.getId());
        teamQueryRepository.deleteTeamLectures(deleteTeam.getId());
        teamQueryRepository.deleteTeamCategories(deleteTeam.getId());
        teamRepository.deleteById(deleteTeam.getId());
    }

    private String getUsername() {
        return securityUtil.getCurrentUsername().orElseThrow(() -> new MemberNotFoundException());
    }
}
