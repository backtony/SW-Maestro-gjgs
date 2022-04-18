package com.gjgs.gjgs.modules.team.services.manage;

import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.bulletin.repositories.BulletinRepository;
import com.gjgs.gjgs.modules.exception.bulletin.BulletinNotFoundException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.team.AlreadyInTeamException;
import com.gjgs.gjgs.modules.exception.team.ApplierInApplierListException;
import com.gjgs.gjgs.modules.exception.team.ApplierNotInTeamApplierListException;
import com.gjgs.gjgs.modules.exception.team.TeamNotFoundException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberTeamRepository;
import com.gjgs.gjgs.modules.team.aop.CheckLeaderBefore;
import com.gjgs.gjgs.modules.team.aop.HasWaitOrder;
import com.gjgs.gjgs.modules.team.dtos.DelegateLeaderResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamAppliersResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamExitResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamManageResponse;
import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.entity.TeamApplier;
import com.gjgs.gjgs.modules.team.repositories.interfaces.MemberTeamQueryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamApplierQueryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamApplierRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamQueryRepository;
import com.gjgs.gjgs.modules.team.services.authority.TeamLeaderAuthorityCheckable;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamManageServiceImpl implements TeamManageService {

    private final TeamQueryRepository teamQueryRepository;
    private final MemberRepository memberRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final BulletinRepository bulletinRepository;
    private final TeamLeaderAuthorityCheckable teamLeaderAuthorityCheckable;
    private final SecurityUtil securityUtil;
    private final TeamApplierQueryRepository teamApplierQueryRepository;
    private final TeamApplierRepository teamApplierRepository;
    private final MemberTeamQueryRepository memberTeamQueryRepository;

    @Override
    public void applyTeam(Long teamId) {
        Bulletin recruitBulletin = bulletinRepository.findWithTeamByTeamId(teamId).orElseThrow(() -> new BulletinNotFoundException());
        recruitBulletin.checkRecruit();
        String username = securityUtil.getCurrentUsername().orElseThrow(() -> new MemberNotFoundException());
        Long applierId = memberRepository.findIdByUsername(username).orElseThrow(() -> new MemberNotFoundException());
        try {
            teamApplierRepository.save(TeamApplier.of(Member.from(applierId), recruitBulletin.getTeam()));
        } catch (DataIntegrityViolationException e) {
            throw new ApplierInApplierListException();
        }
    }

    @CheckLeaderBefore
    @Override
    @Transactional(readOnly = true)
    public TeamAppliersResponse getTeamAppliers(Long teamId) {
        return teamApplierQueryRepository.findAppliers(teamId);
    }

    @CheckLeaderBefore
    @Override
    public TeamManageResponse acceptApplier(Long teamId, Long applierId) {
        validApplyTeam(teamId, applierId);
        deleteApplierSetUpTeamBulletin(teamId, applierId);
        return TeamManageResponse.of(teamId, applierId, true);
    }

    private void validApplyTeam(Long teamId, Long applierId) {
        List<Long> applierMemberIdList = teamApplierQueryRepository.findApplierMemberIdList(teamId);
        if (!applierMemberIdList.contains(applierId)) {
            throw new ApplierNotInTeamApplierListException();
        }
        if (memberTeamQueryRepository.existByApplierInTeamMember(teamId, applierId)) {
            throw new AlreadyInTeamException();
        }
    }

    private void deleteApplierSetUpTeamBulletin(Long teamId, Long applierId) {
        teamApplierQueryRepository.deleteApplier(teamId, applierId);
        setUpTeamBulletin(teamId, applierId);
    }

    private void setUpTeamBulletin(Long teamId, Long applierId) {
        Bulletin bulletin = bulletinRepository.findWithTeamByTeamId(teamId).orElseThrow(() -> new BulletinNotFoundException());
        Team team = bulletin.getTeam();
        team.addTeamMemberCount();
        memberTeamRepository.save(MemberTeam.of(Member.from(applierId), team));
        if (team.isFull()) {
            bulletin.stopRecruit();
        }
    }

    @Override
    public TeamManageResponse rejectApplier(Long teamId, Long applierId) {
        teamLeaderAuthorityCheckable.findLeader(teamId);
        teamApplierQueryRepository.deleteApplier(teamId, applierId);
        return TeamManageResponse.of(teamId, applierId, false);
    }

    @Override
    @HasWaitOrder
    public TeamExitResponse excludeMember(Long teamId, Long memberId) {
        Team team = teamLeaderAuthorityCheckable.findTeamMembers(teamId);

        Member excludeMember = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException());
        team.removeTeamMember(excludeMember);

        teamQueryRepository.deleteTeamMember(teamId, excludeMember.getId());
        return TeamExitResponse.excludeMember(team.getId(), excludeMember.getId());
    }

    @Override
    @HasWaitOrder
    public TeamExitResponse exitMember(Long teamId) {
        Team team = teamQueryRepository.findWithTeamMembers(teamId).orElseThrow(() -> new TeamNotFoundException());

        Member exitMember = securityUtil.getCurrentUserOrThrow();
        team.removeTeamMember(exitMember);

        teamQueryRepository.deleteTeamMember(teamId, exitMember.getId());
        return TeamExitResponse.exitMember(team.getId(), exitMember.getId());
    }

    @Override
    @HasWaitOrder
    public DelegateLeaderResponse changeLeader(Long teamId, Long willLeaderId) {
        Team team = teamLeaderAuthorityCheckable.findTeamMembers(teamId);

        Member willLeader = memberRepository.findById(willLeaderId).orElseThrow(() -> new MemberNotFoundException());
        MemberTeam savedLeaderToMember = memberTeamRepository.save(team.changeLeaderReturnWasLeader(willLeader));
        teamQueryRepository.deleteTeamMember(teamId, willLeaderId);

        return DelegateLeaderResponse.of(team.getId(), team.getLeader().getId(), savedLeaderToMember.getMember().getId());
    }
}
