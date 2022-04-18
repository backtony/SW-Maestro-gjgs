package com.gjgs.gjgs.modules.team.repositories;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.dummy.CategoryDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.favorite.entity.LectureTeam;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.LectureTeamRepository;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberTeamRepository;
import com.gjgs.gjgs.modules.team.dtos.MyLeadTeamsResponse;
import com.gjgs.gjgs.modules.team.dtos.MyTeamListResponse;
import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamJdbcRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TeamQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired TeamQueryRepository teamQueryRepository;
    @Autowired MemberTeamRepository memberTeamRepository;
    @Autowired TeamJdbcRepository teamJdbcRepository;
    @Autowired LectureTeamRepository lectureTeamRepository;

    @Test
    @DisplayName("Zone을 한번에 가져온다.")
    void find_with_zone_categories() throws Exception {

        // given
        Category category2 = categoryRepository.save(CategoryDummy.createCategory());
        Team team = teamRepository.save(TeamDummy.createTeamWithZoneCategories(leader, zone, List.of(category, category2)));
        teamJdbcRepository.insertTeamCategoryList(team.getId(), List.of(category.getId(), category2.getId()));
        flushAndClear();

        // when
        Team findTeam = teamQueryRepository.findWithLeaderZoneCategories(team.getId())
                .orElseThrow();

        // then
        assertEquals(findTeam.getZone().getMainZone(), zone.getMainZone());
    }

    @Test
    @DisplayName("Team의 카테고리 벌크 delete")
    void delete_team_categories_test() throws Exception {

        // given
        Category category1 = categoryRepository.save(CategoryDummy.createCategory());
        Category category2 = categoryRepository.save(CategoryDummy.createCategory());
        Category category3 = categoryRepository.save(CategoryDummy.createCategory());
        Team team = teamRepository.save(TeamDummy.createTeamWithZoneCategories(leader, zone, List.of(category1, category2, category3)));
        teamJdbcRepository.insertTeamCategoryList(team.getId(), List.of(category1.getId(), category2.getId(), category3.getId()));
        flushAndClear();

        // when
        long deleteTeamCategories = teamQueryRepository.deleteTeamCategories(team.getId());

        // then
        assertEquals(deleteTeamCategories, 3);
    }

    @DisplayName("내가 속한 모든 팀 찾기")
    @Test
    void find_my_all_team_by_username() throws Exception {

        // given
        Member member2 = anotherMembers.get(1);
        Team team2 = teamRepository.save(TeamDummy.createTeamOfManyMembers(zone, leader, member2));
        teamJdbcRepository.insertMemberTeamList(team2.getId(), List.of(member2.getId()));
        flushAndClear();

        // when
        List<Team> member2Teams = teamQueryRepository.findMyAllTeamByUsername(leader.getUsername());

        //then
        assertAll(
                () -> assertEquals(2, member2Teams.size()),
                () -> assertThat(member2Teams.stream().map(Team::getId).collect(toList())).contains(team.getId(), team2.getId())
        );
    }

    @Test
    @DisplayName("팀 조회시 팀에 속한 멤버들과 리더 가져오기")
    void find_with_team_members_leader_test() throws Exception {

        // given
        Member member1 = anotherMembers.get(0);
        Member member2 = anotherMembers.get(1);
        teamJdbcRepository.insertMemberTeamList(team.getId(), List.of(member1.getId(), member2.getId()));
        flushAndClear();

        // when
        Team findTeam = teamQueryRepository
                .findWithTeamMembers(team.getId())
                .orElseThrow();

        // then
        List<Member> teamMembersWithLeader = new ArrayList<>();
        teamMembersWithLeader.add(findTeam.getLeader());
        teamMembersWithLeader.addAll(findTeam.getTeamMembers().stream().map(MemberTeam::getMember).collect(toList()));
        assertAll(
                () -> assertEquals(teamMembersWithLeader.size(), 3),
                () -> assertEquals(findTeam.getLeader().getNickname(), leader.getNickname()),
                () -> assertThat(findTeam.getTeamMembers().stream()
                        .map(memberTeam -> memberTeam.getMember().getNickname()).sorted().collect(toList()))
                        .contains(member1.getNickname(), member2.getNickname())
        );
    }

    @Test
    @DisplayName("팀이 찜한 클래스, TeamLecture List 지우기")
    void delete_team_lectures_test() throws Exception {

        // given
        Lecture lecture1 = lectureRepository.save(LectureDummy.createDataJpaTestLecture(zone, category, director));
        Lecture lecture2 = lectureRepository.save(LectureDummy.createDataJpaTestLecture(zone, category, director));
        Lecture lecture3 = lectureRepository.save(LectureDummy.createDataJpaTestLecture(zone, category, director));
        LectureTeam lectureTeam1 = LectureTeam.of(lecture1, team);
        lectureTeamRepository.save(lectureTeam1);
        LectureTeam lectureTeam2 = LectureTeam.of(lecture2, team);
        lectureTeamRepository.save(lectureTeam2);
        LectureTeam lectureTeam3 = LectureTeam.of(lecture3, team);
        lectureTeamRepository.save(lectureTeam3);
        flushAndClear();

        // when
        long delete = teamQueryRepository.deleteTeamLectures(team.getId());

        // then
        assertEquals(delete, 3);
    }

    @Test
    @DisplayName("팀 가입신청 멤버들, TeamAppliers List 지우기")
    void delete_team_appliers_test() throws Exception {

        // given
        int count = (int) (memberRepository.count());
        Member applier1 = memberRepository.save(MemberDummy.createDataJpaTestMember(count + 1, zone, category));
        Member applier2 = memberRepository.save(MemberDummy.createDataJpaTestMember(count + 2, zone, category));
        addApplier(applier1);
        addApplier(applier2);
        flushAndClear();

        // when
        long deleteCount = teamQueryRepository.deleteTeamAppliers(team.getId());

        // then
        assertEquals(deleteCount, 2);
    }

    @Test
    @DisplayName("팀에 속한 팀원들, TeamMembers List 지우기")
    void delete_team_members_test() throws Exception {

        // given
        teamJdbcRepository.insertMemberTeamList(team.getId(), anotherMembers.stream().map(Member::getId).collect(toList()));
        flushAndClear();

        // when
        long deleteCount = teamQueryRepository.deleteTeamMembers(team.getId());

        // then
        assertEquals(deleteCount, 3);
    }

    @Test
    @DisplayName("팀과 리더를 함께 가져오기")
    void find_with_leader_test() throws Exception {

        // given
        flushAndClear();

        // when
        Team findTeam = teamQueryRepository.findWithLeader(team.getId()).orElseThrow();

        // then
        assertEquals(findTeam.getLeader().getNickname(), leader.getNickname());
    }

    @Test
    @DisplayName("팀에 속해있는 멤버 삭제하기")
    void delete_member_test() throws Exception {

        // given
        Member member1 = anotherMembers.get(0);
        teamJdbcRepository.insertMemberTeamList(team.getId(), List.of(member1.getId()));
        flushAndClear();

        // when
        long count = teamQueryRepository.deleteTeamMember(team.getId(), member1.getId());

        // then
        assertEquals(count, 1);
    }

    @Test
    @DisplayName("내가 리더인 팀의 ID와 이름 가져오기")
    void find_my_lead_team_with_bulletin_lecture_test() throws Exception {

        // given
        teamRepository.save(TeamDummy.createUniqueTeam(2, leader, zone));
        teamRepository.save(TeamDummy.createUniqueTeam(3, leader, zone));
        flushAndClear();

        // when
        MyLeadTeamsResponse res = teamQueryRepository
                .findMyTeamByUsername(leader.getUsername());

        // then
        List<MyLeadTeamsResponse.MyLeadTeamsWithBulletin> myLeadTeamList = res.getMyLeadTeams();
        assertEquals(myLeadTeamList.size(), 3);
    }

    @Test
    @DisplayName("내가 리더인 팀의 ID와 이름 가져오기, 아무것도 없을 경우")
    void find_my_lead_team_with_bulletin_lecture_null_test() throws Exception {

        // given
        int count = (int) memberRepository.count();
        Member member = memberRepository.save(MemberDummy.createDataJpaTestMember(count + 1, zone, category));
        flushAndClear();

        // when
        MyLeadTeamsResponse res = teamQueryRepository.findMyTeamByUsername(member.getUsername());
        assertEquals(res.getMyLeadTeams().size(), 0);
    }

    @Test
    @DisplayName("나의 팀 리스트 가져오기 개선쿼리 / 내가 속한 팀, 내가 팀장인 팀 총 두개가 나와야함")
    void find_my_team_list_by_username_test() throws Exception {

        // given
        int count = (int) memberRepository.count();
        Member onlyLeader = memberRepository.save(MemberDummy.createDataJpaTestMember(count + 2, zone, category));
        Team team2 = teamRepository.save(TeamDummy.createTeamWithZoneCategories(onlyLeader, zone, List.of(category)));
        teamJdbcRepository.insertTeamCategoryList(team2.getId(), List.of(category.getId()));
        teamJdbcRepository.insertTeamCategoryList(team.getId(), List.of(category.getId()));
        MemberTeam memberTeam = team2.addTeamMember(leader);
        memberTeamRepository.save(memberTeam);
        flushAndClear();

        // when
        MyTeamListResponse response = teamQueryRepository.findMyTeamListByUsername(leader.getUsername());

        // then
        Set<MyTeamListResponse.MyTeam> myTeamList = response.getMyTeamList();
        Set<MyTeamListResponse.MyTeam> isLeader = myTeamList.stream()
                .filter(MyTeamListResponse.MyTeam::isIAmLeader)
                .collect(toSet());
        Set<MyTeamListResponse.MyTeam> isNotLeader = myTeamList.stream()
                .filter(myTeam -> !myTeam.isIAmLeader())
                .collect(toSet());
        assertAll(
                () -> assertEquals(myTeamList.size(), 2),
                () -> assertEquals(isLeader.size(), 1),
                () -> assertEquals(isNotLeader.size(), 1)
        );
    }

    @Test
    @DisplayName("나의 팀 리스트 가져오기 개선쿼리 / 아무 팀도 속하지 않은 멤버의 경우")
    void find_my_team_list_by_username_empty_response_test() throws Exception {

        // given
        int count = (int) memberRepository.count();
        Member member = memberRepository.save(MemberDummy.createDataJpaTestMember(count + 1, zone, category));
        flushAndClear();

        // when
        MyTeamListResponse response = teamQueryRepository.findMyTeamListByUsername(member.getUsername());

        // then
        Set<MyTeamListResponse.MyTeam> myTeamList = response.getMyTeamList();
        assertTrue(myTeamList.isEmpty());
    }

    @Test
    @DisplayName("teamId와 username으로 리더 권한이 있는 팀 확인하기")
    void existByIdLeaderUsernameTest() throws Exception {

        // when, then
        assertTrue(teamQueryRepository.existByIdLeaderUsername(team.getId(), leader.getUsername()));
    }
}
