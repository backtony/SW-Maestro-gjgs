package com.gjgs.gjgs.modules.team.services.implement;

import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.bulletin.repositories.BulletinRepository;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.exception.team.NotMemberOfTeamException;
import com.gjgs.gjgs.modules.exception.team.TeamLeaderCanNotExitTeamException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Sex;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberTeamRepository;
import com.gjgs.gjgs.modules.team.dtos.DelegateLeaderResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamExitResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamManageResponse;
import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.MemberTeamQueryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamApplierQueryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamApplierRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamQueryRepository;
import com.gjgs.gjgs.modules.team.services.authority.TeamLeaderAuthorityCheckable;
import com.gjgs.gjgs.modules.team.services.manage.TeamManageServiceImpl;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamManageServiceImplTest {

    @Mock TeamQueryRepository teamQueryRepository;
    @Mock MemberRepository memberRepository;
    @Mock MemberTeamRepository memberTeamRepository;
    @Mock BulletinRepository bulletinRepository;
    @Mock TeamLeaderAuthorityCheckable teamLeaderAuthorityCheckable;
    @Mock SecurityUtil securityUtil;
    @Mock TeamApplierRepository teamApplierRepository;
    @Mock TeamApplierQueryRepository teamApplierQueryRepository;
    @Mock MemberTeamQueryRepository memberTeamQueryRepository;
    @InjectMocks TeamManageServiceImpl teamManageService;

    @Test
    @DisplayName("그룹 가입 신청하기")
    void apply_team_test() throws Exception {

        // given
        Member applier = createMember(1);
        stubbingGetCurrentUsername(applier);
        stubbingFindMemberId(applier);
        Team applyTeam = TeamDummy.createUniqueTeam();
        Bulletin bulletin = createBulletin();
        bulletin.setTeam(applyTeam);
        applyTeam.delegateLeader(Member.builder().id(1234L).build());
        when(bulletinRepository.findWithTeamByTeamId(applyTeam.getId()))
                .thenReturn(Optional.of(bulletin));

        // when
        teamManageService.applyTeam(applyTeam.getId());

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(bulletinRepository).findWithTeamByTeamId(applyTeam.getId()),
                () -> verify(memberRepository).findIdByUsername(applier.getUsername()),
                () -> verify(teamApplierRepository).save(any())
        );
    }

    @Test
    @DisplayName("그룹 가입 승인하기 및 팀이 다 차있을 경우 모집중 해제하기")
    void accept_member_test() throws Exception {

        // given
        Team team = createTeamApplierAndTeamMembers();
        Long teamId = team.getId();
        Bulletin bulletin = Bulletin
                .builder()
                .team(team)
                .status(true).build();
        Member member = Member.builder().id(1L).build();
        when(teamApplierQueryRepository.findApplierMemberIdList(team.getId())).thenReturn(List.of(member.getId()));
        when(memberTeamQueryRepository.existByApplierInTeamMember(teamId, member.getId())).thenReturn(false);
        when(bulletinRepository.findWithTeamByTeamId(teamId)).thenReturn(Optional.of(bulletin));

        // when
        TeamManageResponse res = teamManageService.acceptApplier(team.getId(), member.getId());

        // then
        assertAll(
                () -> verify(teamApplierQueryRepository).findApplierMemberIdList(teamId),
                () -> verify(memberTeamQueryRepository).existByApplierInTeamMember(teamId, member.getId()),
                () -> verify(teamApplierQueryRepository).deleteApplier(teamId, member.getId()),
                () -> verify(bulletinRepository).findWithTeamByTeamId(teamId),
                () -> verify(memberTeamRepository).save(any()),
                () -> assertFalse(bulletin.isStatus()),
                () -> assertEquals(team.getCurrentMemberCount(), 4),
                () -> assertEquals(res.getMemberId(), 1L),
                () -> assertEquals(res.getTeamId(), teamId),
                () -> assertTrue(res.isAccept()),
                () -> assertFalse(res.isReject())
        );
    }

    @Test
    @DisplayName("그룹 가입 거절하기")
    void refuse_member_test() throws Exception {

        // given
        Team team = createTeamApplierAndTeamMembers();
        Member applier = Member.builder().id(1L).build();

        // when
        TeamManageResponse res = teamManageService.rejectApplier(team.getId(), 1L);

        // then
        assertAll(
                () -> verify(teamLeaderAuthorityCheckable).findLeader(any()),
                () -> verify(teamApplierQueryRepository).deleteApplier(team.getId(), applier.getId()),
                () -> assertEquals(team.getTeamMembers().size(), 2),
                () -> assertEquals(team.getCurrentMemberCount(), 3),
                () -> assertEquals(res.getMemberId(), 1L),
                () -> assertEquals(res.getTeamId(), team.getId()),
                () -> assertFalse(res.isAccept()),
                () -> assertTrue(res.isReject()));
    }

    @Test
    @DisplayName("팀에 속한 멤버중 한명을 추방할 때, 팀원이 아닌 멤버는 추방할 수 없다.")
    void exclude_member_should_team_member_test() throws Exception {

        // given
        Team team = createTeamApplierAndTeamMembers();
        Member leader = team.getLeader();
        Long teamId = team.getId();
        when(teamLeaderAuthorityCheckable.findTeamMembers(teamId)).thenReturn(team);
        Member notTeamMember = Member.builder().id(25L).username("notLeader").build();
        Long excludeMemberId = notTeamMember.getId();
        when(memberRepository.findById(excludeMemberId)).thenReturn(Optional.of(notTeamMember));

        // when, then
        assertThrows(NotMemberOfTeamException.class,
                () -> teamManageService.excludeMember(teamId, excludeMemberId),
                "추방하는 사람은 팀에 속해있어야 한다.");
    }

    @Test
    @DisplayName("팀에 속한 멤버중 한명을 추방한다.")
    void exclude_member_test() throws Exception {

        // given
        Team team = createTeamApplierAndTeamMembers();
        Long teamId = team.getId();
        Member leader = team.getLeader();
        Member exitMember = team.getTeamMembers().get(0).getMember();
        Long exitMemberId = exitMember.getId();
        when(teamLeaderAuthorityCheckable.findTeamMembers(teamId)).thenReturn(team);
        when(memberRepository.findById(exitMemberId)).thenReturn(Optional.of(exitMember));

        // when
        TeamExitResponse res = teamManageService.excludeMember(teamId, exitMemberId);

        // then
        assertAll(
                () -> verify(teamLeaderAuthorityCheckable).findTeamMembers(teamId),
                () -> verify(memberRepository).findById(exitMemberId),
                () -> verify(teamQueryRepository).deleteTeamMember(teamId, exitMemberId),
                () -> assertEquals(res.getTeamId(), teamId),
                () -> assertEquals(res.getMemberId(), exitMember.getId()),
                () -> assertEquals(team.getTeamMembers().size(), 1)
        );
    }

    @Test
    @DisplayName("팀원이 팀을 나가기, 팀원이 아닐 경우 예외 발생")
    void exit_member_should_team_members_test() throws Exception {

        // given
        Team team = createTeamApplierAndTeamMembers();
        Long teamId = team.getId();
        Member notTeamMember = Member.builder().id(13879L).username("test").build();
        when(teamQueryRepository.findWithTeamMembers(teamId)).thenReturn(Optional.of(team));
        when(securityUtil.getCurrentUserOrThrow()).thenReturn(notTeamMember);

        // when, then
        assertThrows(NotMemberOfTeamException.class,
                () -> teamManageService.exitMember(teamId),
                "현재 멤버는 팀에 속해있지 않음");
    }

    @Test
    @DisplayName("팀원이 팀을 나가기, 팀장일 경우 예외 발생")
    void exit_member_should_not_iam_leader_test() throws Exception {

        // given
        Team team = createTeamApplierAndTeamMembers();
        Long teamId = team.getId();
        Member leader = team.getLeader();
        when(teamQueryRepository.findWithTeamMembers(teamId))
                .thenReturn(Optional.of(team));
        when(securityUtil.getCurrentUserOrThrow()).thenReturn(leader);

        // when, then
        assertThrows(TeamLeaderCanNotExitTeamException.class,
                () -> teamManageService.exitMember(teamId),
                "나갈 팀원이 팀장일 수 없음");
    }

    @Test
    @DisplayName("팀원이 팀을 나가기")
    void exit_member_test() throws Exception {

        // given
        Team team = createTeamApplierAndTeamMembers();
        Long teamId = team.getId();
        Member exitMember = team.getTeamMembers().get(1).getMember();
        when(teamQueryRepository.findWithTeamMembers(teamId)).thenReturn(Optional.of(team));
        when(securityUtil.getCurrentUserOrThrow()).thenReturn(exitMember);

        // when
        TeamExitResponse res = teamManageService.exitMember(teamId);

        // then
        assertAll(
                () -> assertEquals(res.getTeamId(), teamId),
                () -> assertEquals(res.getMemberId(), exitMember.getId()),
                () -> assertEquals(team.getCurrentMemberCount(), 2),
                () -> assertEquals(team.getTeamMembers().size(), 1),
                () -> verify(teamQueryRepository).findWithTeamMembers(teamId),
                () -> verify(securityUtil).getCurrentUserOrThrow(),
                () -> verify(teamQueryRepository).deleteTeamMember(team.getId(), exitMember.getId())
        );
    }

    @Test
    @DisplayName("리더 위임하기")
    void change_leader_test() throws Exception {

        // given
        Team team = createTeamApplierAndTeamMembers();
        Member currentLeader = team.getLeader();
        Member willLeader = team.getTeamMembers().get(0).getMember();
        when(teamLeaderAuthorityCheckable.findTeamMembers(team.getId()))
                .thenReturn(team);
        when(memberRepository.findById(willLeader.getId()))
                .thenReturn(Optional.of(willLeader));
        MemberTeam savedMemberTeam = MemberTeam.builder()
                .member(currentLeader).team(team)
                .build();
        when(memberTeamRepository.save(any()))
                .thenReturn(savedMemberTeam);

        // when
        DelegateLeaderResponse response = teamManageService.changeLeader(team.getId(), willLeader.getId());
        List<Long> teamMemberIdList = team.getTeamMembers()
                .stream().map(memberTeam -> memberTeam.getMember().getId())
                .collect(toList());
        assertAll(
                () -> verify(teamLeaderAuthorityCheckable).findTeamMembers(team.getId()),
                () -> verify(memberRepository).findById(willLeader.getId()),
                () -> verify(memberTeamRepository).save(MemberTeam.of(currentLeader, team)),
                () -> verify(teamQueryRepository).deleteTeamMember(team.getId(), willLeader.getId()),
                () -> assertEquals(team.getLeader().getId(), willLeader.getId()),
                () -> assertThat(teamMemberIdList).contains(currentLeader.getId()),
                () -> assertEquals(teamMemberIdList.size(), 2),
                () -> assertEquals(response.getTeamId(), team.getId()),
                () -> assertEquals(response.getChangedLeaderId(), willLeader.getId()),
                () -> assertEquals(response.getToTeamMemberId(), currentLeader.getId())
        );
    }

    private void stubbingFindMemberId(Member applier) {
        when(memberRepository.findIdByUsername(applier.getUsername())).thenReturn(Optional.of(applier.getId()));
    }

    private Team createTeamApplierAndTeamMembers() {
        return Team.builder()
                .id(12L)
                .currentMemberCount(3)
                .maxPeople(4)
                .teamMembers(new ArrayList<>(List.of(
                        MemberTeam.builder().member(Member.builder().id(5L).username("test5").build()).build(),
                        MemberTeam.builder().member(Member.builder().id(6L).username("test6").build()).build())))
                .leader(Member.builder().id(137L).username("leader").build())
                .build();
    }

    private void stubbingGetCurrentUsername(Member applier) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(applier.getUsername()));
    }

    private Bulletin createBulletin() {
        return Bulletin.builder().status(true).build();
    }

    private Member createMember(int i) {
        return Member.builder().id((long) i).username("테스트유저" + String.valueOf(i)).sex(Sex.F).build();
    }
}