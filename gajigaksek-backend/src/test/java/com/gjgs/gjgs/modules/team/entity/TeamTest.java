package com.gjgs.gjgs.modules.team.entity;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.TeamDtoDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.dummy.ZoneDummy;
import com.gjgs.gjgs.modules.exception.team.NotMemberOfTeamException;
import com.gjgs.gjgs.modules.exception.team.TeamLeaderCanNotExitTeamException;
import com.gjgs.gjgs.modules.exception.team.TeamMemberCountLargerThanEditCountException;
import com.gjgs.gjgs.modules.exception.team.TeamMemberMaxException;
import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.dtos.CreateTeamRequest;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @Test
    @DisplayName("팀장으로 위임하기(그룹 만든사람이 팀장되기)")
    void delegate_leader_test() throws Exception {

        // given
        Member member = createMember();
        Team team = createTeam();

        // when
        team.delegateLeader(member);

        // then
        assertEquals(team.getLeader().getId(), member.getId());
    }

    private Team createTeam() {
        return Team.builder()
                .id(1L)
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("팀의 지역 설정")
    void put_zone_test() throws Exception {

        // given
        Team team = createTeam();
        Zone zone = createZone();

        // when
        team.putZone(zone);

        // then
        assertEquals(team.getZone().getId(), zone.getId());
    }

    private Zone createZone() {
        return Zone.builder()
                .id(1L)
                .mainZone("서울시")
                .subZone("건대/구의/성수")
                .build();
    }

    private Category createCategory(Long categoryId) {
        return Category.builder()
                .id(categoryId).mainCategory("test").subCategory("test/test")
                .build();
    }

    @Test
    @DisplayName("팀 수정하기")
    void modify_team() throws Exception {

        // given
        Member leader = Member.builder().id(1L).build();
        Team team = TeamDummy.createTeam(leader, ZoneDummy.createZone(116L));
        CreateTeamRequest req = TeamDtoDummy.buildCreateTeamRequest();

        // when
        team.modify(req);

        // then
        assertAll(
                () -> assertThat(team.getTeamName()).isEqualTo(req.getTeamName()),
                () -> assertThat(team.getMaxPeople()).isEqualTo(req.getMaxPeople()),
                () -> assertThat(team.getDayType()).isEqualTo(req.getDayType()),
                () -> assertThat(team.getTimeType()).isEqualTo(req.getTimeType()));
    }

    @Test
    @DisplayName("수정할 인원이 현재 인원보다 많을 경우 예외 발생")
    void current_people_is_more_than_modify_max_people() throws Exception {

        // given
        Member leader = Member.builder().id(1L).build();
        Team team = Team.builder()
                .leader(leader).currentMemberCount(2).maxPeople(4)
                .build();
        CreateTeamRequest req = TeamDtoDummy.createModifyTeamRequestLowerthanCurrentPeople();

        // when, then
        assertThrows(TeamMemberCountLargerThanEditCountException.class,
                () -> team.modify(req),
                "팀의 현재 인원이 수정할 최대 인원 보다 큽니다.");
    }

    @Test
    @DisplayName("팀(그룹)에 멤버 들어오기")
    void add_member_in_team_test() throws Exception {

        // given
        Member member = Member.builder().build();
        Member member2 = Member.builder().build();
        Team team = Team.builder().maxPeople(4).build();

        // when
        team.addTeamMember(member);
        team.addTeamMember(member2);

        // then
        assertThat(team.getTeamMembers().size()).isEqualTo(2);
        assertThat(team.getCurrentMemberCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("팀에 속한 멤버가 팀을 나가기")
    void exit_team_in_members_test() throws Exception {

        // given
        Member leader = Member.builder().id(1L).build();
        Member member1 = Member.builder().id(2L).build();
        Member member2 = Member.builder().id(3L).build();
        Member member3 = Member.builder().id(4L).build();
        Team team = Team.builder()
                .leader(leader)
                .teamMembers(new ArrayList<>(List.of(
                        MemberTeam.builder().member(member1).build(),
                        MemberTeam.builder().member(member2).build(),
                        MemberTeam.builder().member(member3).build())))
                .currentMemberCount(4)
                .build();

        // when
        team.removeTeamMember(member2);

        // then
        List<Member> remainMembers = team.getTeamMembers().stream()
                .map(MemberTeam::getMember)
                .collect(toList());

        assertAll(
                () -> assertEquals(remainMembers.size(), 2),
                () -> assertThat(remainMembers.stream()
                        .map(Member::getId)
                        .sorted()
                        .collect(toList())).containsExactly(2L, 4L),
                () -> assertEquals(team.getCurrentMemberCount(), 3)
        );
    }

    @Test
    @DisplayName("팀에 속한 멤버가 팀을 나가기, 해당 멤버가 팀에 속하지 않을 경우 예외 확인")
    void exit_team_should_in_team_members() throws Exception {

        // given
        Member otherMember = Member.builder().id(123L).build();
        Member leader = Member.builder().id(1L).build();
        Member member1 = Member.builder().id(2L).build();
        Member member2 = Member.builder().id(3L).build();
        Member member3 = Member.builder().id(4L).build();
        Team team = Team.builder()
                .leader(leader)
                .teamMembers(new ArrayList<>(List.of(
                        MemberTeam.builder().member(member1).build(),
                        MemberTeam.builder().member(member2).build(),
                        MemberTeam.builder().member(member3).build())))
                .currentMemberCount(4)
                .build();

        // when, then
        assertThrows(NotMemberOfTeamException.class,
                () -> team.removeTeamMember(otherMember),
                "해당 멤버는 팀에 속해있지 않습니다.");
    }

    @Test
    @DisplayName("팀에 속한 멤버가 팀을 나가기, 팀장인 경우 그룹을 삭제해야 한다.")
    void exit_team_should_not_leader() throws Exception {

        // given
        Member leader = Member.builder().id(1L).build();
        Member member1 = Member.builder().id(2L).build();
        Member member2 = Member.builder().id(3L).build();
        Member member3 = Member.builder().id(4L).build();
        Team team = Team.builder()
                .leader(leader)
                .teamMembers(new ArrayList<>(List.of(
                        MemberTeam.builder().member(member1).build(),
                        MemberTeam.builder().member(member2).build(),
                        MemberTeam.builder().member(member3).build())))
                .currentMemberCount(4)
                .build();

        // when, then
        assertThrows(TeamLeaderCanNotExitTeamException.class,
                () -> team.removeTeamMember(leader),
                "해당 멤버는 팀장입니다. 그룹을 삭제해주세요.");
    }

    @Test
    @DisplayName("팀원이 이미 다 찼을 경우 예외 발생")
    void team_is_full_do_throw_exception() throws Exception {

        // given
        Team team = Team.builder().maxPeople(4).currentMemberCount(4).build();

        // when, then
        assertThrows(TeamMemberMaxException.class,
                team::checkTeamIsFullDoThrow,
                "팀이 찼을 경우 예외 발생");
    }

    @DisplayName("매칭 팀 만들기")
    @Test
    void create_match_team() throws Exception {
        //given
        MatchingRequest matchingRequest = MatchingRequest.builder()
                .zoneId(1L)
                .categoryId(1L)
                .dayType("MON|TUE")
                .timeType("AFTERNOON")
                .preferMemberCount(2)
                .build();
        Member m1 = MemberDummy.createTestMember();
        Member m2 = MemberDummy.createTestDirectorMember();

        String teamName = m1.getNickname() + ", " + m2.getNickname();

        //when
        Team matchTeam = Team.createMatchTeam(matchingRequest, m1, Arrays.asList(m2));


        //then
        assertAll(
                () -> assertEquals(m1, matchTeam.getLeader()),
                () -> assertEquals(1L, matchTeam.getZone().getId()),
                () -> assertEquals(teamName, matchTeam.getTeamName()),
                () -> assertEquals(matchingRequest.getPreferMemberCount(), matchTeam.getMaxPeople()),
                () -> assertEquals(matchingRequest.getPreferMemberCount(), matchTeam.getCurrentMemberCount()),
                () -> assertEquals(matchingRequest.getPreferMemberCount(), matchTeam.getCurrentMemberCount()),
                () -> assertEquals(matchingRequest.getDayType(), matchTeam.getDayType()),
                () -> assertEquals(matchingRequest.getTimeType(), matchTeam.getTimeType())
        );
    }

    @Test
    @DisplayName("모든 팀의 멤버들 반환하기")
    void get_all_members_test() throws Exception {

        // given
        Team team = Team.builder()
                .leader(Member.builder().id(1L).build())
                .teamMembers(List.of(
                        MemberTeam.builder().member(Member.builder().id(2L).build()).build(),
                        MemberTeam.builder().member(Member.builder().id(3L).build()).build()
                ))
                .build();

        // when
        List<Member> allMembers = team.getAllMembers();

        // then
        assertEquals(allMembers.size(), 3);
    }

    @Test
    @DisplayName("팀장 변경하기 / 위임할 리더가 팀에 없는 경우")
    void change_leader_should_team_member_test()throws Exception {

        // given
        Member willLeader = Member.builder().id(1L).build();
        Team team = Team.builder().build();

        // when, then
        assertThrows(NotMemberOfTeamException.class,
                () -> team.changeLeaderReturnWasLeader(willLeader),
                "위임할 리더는 팀에 속해있어야 한다.");
    }

    @Test
    @DisplayName("팀장 변경하기 / 위임할 리더가 자신인 경우")
    void change_leader_should_not_iam_leader_test()throws Exception {

        // given
        Member willLeader = Member.builder().id(1L).build();
        Team team = Team.builder().leader(willLeader)
                .teamMembers(List.of(MemberTeam.builder()
                        .member(willLeader)
                        .build())).build();

        // when, then
        assertThrows(TeamLeaderCanNotExitTeamException.class,
                () -> team.changeLeaderReturnWasLeader(willLeader),
                "위임할 리더가 본인이면 안된다.");
    }

    @Test
    @DisplayName("팀장 변경하기")
    void change_leader_test()throws Exception {

        // given
        Member willLeader = Member.builder().id(1L).build();
        Member currentLeader = Member.builder().id(2L).build();
        Member teamMember1 = Member.builder().id(3L).build();
        Member teamMember2 = Member.builder().id(4L).build();

        Team team = Team.builder().leader(currentLeader)
                .currentMemberCount(4)
                .maxPeople(4)
                .teamMembers(new ArrayList<>(List.of(
                        MemberTeam.builder().member(willLeader).build(),
                        MemberTeam.builder().member(teamMember1).build(),
                        MemberTeam.builder().member(teamMember2).build())))
                .build();

        // when
        team.changeLeaderReturnWasLeader(willLeader);

        // then
        List<Long> memberTeamIdList = team.getTeamMembers().stream().map(memberTeam -> memberTeam.getMember().getId()).collect(toList());
        assertAll(
                () -> assertEquals(team.getCurrentMemberCount(), 4),
                () -> assertEquals(team.getLeader().getId(), willLeader.getId()),
                () -> assertThat(memberTeamIdList).contains(currentLeader.getId(), teamMember1.getId(), teamMember2.getId())
        );
    }

    @Test
    @DisplayName("팀 최대 인원이 넘어갈 경우 예외 발생")
    void add_team_member_should_not_over_team_member_count() throws Exception {

        // given
        Team team = Team.builder()
                .maxPeople(4)
                .currentMemberCount(4)
                .build();

        // when, then
        assertThrows(TeamMemberMaxException.class,
                team::addTeamMemberCount);
    }
}
