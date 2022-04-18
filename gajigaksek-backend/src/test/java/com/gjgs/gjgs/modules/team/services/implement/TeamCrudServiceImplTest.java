package com.gjgs.gjgs.modules.team.services.implement;

import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.bulletin.repositories.BulletinRepository;
import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.category.services.interfaces.CategoryService;
import com.gjgs.gjgs.modules.dummy.BulletinDummy;
import com.gjgs.gjgs.modules.dummy.TeamDtoDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Sex;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.team.dtos.*;
import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamJdbcRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamQueryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import com.gjgs.gjgs.modules.team.services.authority.TeamLeaderAuthorityCheckable;
import com.gjgs.gjgs.modules.team.services.crud.TeamCrudServiceImpl;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamCrudServiceImplTest {

    @Mock ZoneRepository zoneRepository;
    @Mock TeamRepository teamRepository;
    @Mock SecurityUtil securityUtil;
    @Mock TeamQueryRepository teamQueryRepository;
    @Mock TeamJdbcRepository teamJdbcRepository;
    @Mock BulletinRepository bulletinRepository;
    @Mock CategoryService categoryService;
    @Mock TeamLeaderAuthorityCheckable teamLeaderAuthorityCheckable;
    @Mock MemberRepository memberRepository;
    @InjectMocks TeamCrudServiceImpl teamService;

    @Test
    @DisplayName("그룹 생성하기")
    void create_group() throws Exception {

        // given
        CreateTeamRequest req = TeamDtoDummy.buildCreateTeamRequest();

        Member member = createMember(1);
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(member.getUsername()));
        when(memberRepository.findIdByUsername(member.getUsername())).thenReturn(Optional.of(member.getId()));

        List<Long> categoryIdList = createCategoryList();
        when(categoryService.findAndValidCategories(req.getCategoryList()))
                .thenReturn(categoryIdList);

        Zone zone = mock(Zone.class);
        when(zoneRepository.findById(req.getZoneId()))
                .thenReturn(Optional.of(zone));

        Team team = mock(Team.class);
        when(teamRepository.save(any())).thenReturn(team);

        // when
        CreateTeamResponse response = teamService.createTeam(req);

        // then
        assertAll(
                () -> assertThat(response).isInstanceOf(CreateTeamResponse.class),
                () -> verify(teamJdbcRepository).insertTeamCategoryList(team.getId(), categoryIdList),
                () -> verify(categoryService).findAndValidCategories(any()),
                () -> verify(securityUtil).getCurrentUsername()
        );
    }

    @Test
    @DisplayName("그룹 수정하기")
    void modify_group() throws Exception {

        // given
        Member leader = createMember(1287);
        Team team = Team.builder()
                .leader(leader)
                .zone(Zone.builder().id(10L).build())
                .build();
        CreateTeamRequest req = TeamDtoDummy.buildCreateTeamRequest();
        List<Long> categoryIdList = createCategoryList();
        when(teamLeaderAuthorityCheckable.findZone(any())).thenReturn(team);
        when(categoryService.findAndValidCategories(any())).thenReturn(categoryIdList);
        when(zoneRepository.findById(any())).thenReturn(Optional.of(mock(Zone.class)));

        // when
        ModifyTeamResponse res = teamService.modifyTeam(1L, req);

        // then
        assertAll(
                () -> verify(teamLeaderAuthorityCheckable).findZone(any()),
                () -> verify(zoneRepository).findById(any()),
                () -> verify(teamQueryRepository).deleteTeamCategories(any()),
                () -> verify(teamJdbcRepository).insertTeamCategoryList(any(), any()),
                () -> verify(categoryService).findAndValidCategories(any()),
                () -> assertThat(res).isInstanceOf(ModifyTeamResponse.class));
    }

    @Test
    @DisplayName("그룹 상세 조회")
    void get_team_details_test() throws Exception {

        // given
        when(teamQueryRepository.findTeamDetail(any()))
                .thenReturn(createTeamDetailResponse());

        //when
        TeamDetailResponse response = teamService.getTeamDetail(1L);

        // then
        List<TeamDetailResponse.FavoriteLecture> favoriteLectureList = response.getFavoriteLectureList().stream()
                .filter(TeamDetailResponse.FavoriteLecture::isMyFavoriteLecture).collect(toList());
        verify(teamQueryRepository).findTeamDetail(any());
        assertAll(
                () -> assertFalse(response.isIAmLeader()),
                () -> assertThat(favoriteLectureList.size()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("내가 속한 그룹 리스트 가져오기")
    void get_my_team_list_test() throws Exception {

        // given
        String username = "username";
        when(securityUtil.getCurrentUsername())
                .thenReturn(Optional.of(username));
        when(teamQueryRepository.findMyTeamListByUsername(any()))
                .thenReturn(MyTeamListResponse.builder()
                        .myTeamList(Set.of(
                                MyTeamListResponse.MyTeam.builder()
                                        .teamId(1L)
                                        .teamName("test")
                                        .applyPeople(2)
                                        .maxPeople(3)
                                        .categoryList(Set.of(1L, 2L))
                                        .iAmLeader(true)
                                        .build()
                        ))
                        .build());

        // when
        MyTeamListResponse res = teamService.getMyTeamList();

        // then
        Set<MyTeamListResponse.MyTeam> teamList = res.getMyTeamList();
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(teamQueryRepository).findMyTeamListByUsername(any())
        );
    }

    @Test
    @DisplayName("게시글 생성 시, 내 팀 가져오기 / 내 팀이 없을 경우 bulletin을 찾지 않는다.")
    void get_my_team_if_not_exist_bulletin_then_not_call_bulletin_repository() throws Exception {

        // given
        Member loginMember = Member.builder()
                .id(1L).username("test")
                .build();
        MyLeadTeamsResponse response = MyLeadTeamsResponse.of(new ArrayList<>());
        when(securityUtil.getCurrentUsername())
                .thenReturn(Optional.of(loginMember.getUsername()));
        when(teamQueryRepository.findMyTeamByUsername(loginMember.getUsername()))
                .thenReturn(response);

        // when
        MyLeadTeamsResponse res = teamService.getMyLeadTeamWithBulletinLecture();

        // then
        assertAll(
                () -> verify(teamQueryRepository).findMyTeamByUsername(loginMember.getUsername()),
                () -> verify(bulletinRepository, never()).findWithLectureByMemberId(any()),
                () -> assertEquals(res.getMyLeadTeams().size(), 0)
        );
    }

    @Test
    @DisplayName("게시글 생성 시, 내 팀 가져오기 / 팀이 존재할 경우")
    void my_team_with_bulletin_if_exist_team_then_find_with_lecture() throws Exception {

        // given
        Member loginMember = Member.builder()
                .id(1L).username("test")
                .build();
        MyLeadTeamsResponse response = MyLeadTeamsResponse.of(new ArrayList<>(List.of(
                MyLeadTeamsResponse.MyLeadTeamsWithBulletin.builder().teamId(1L).teamName("test").build(),
                MyLeadTeamsResponse.MyLeadTeamsWithBulletin.builder().teamId(2L).teamName("test").build()
        )));
        when(securityUtil.getCurrentUsername())
                .thenReturn(Optional.of(loginMember.getUsername()));
        when(teamQueryRepository.findMyTeamByUsername(loginMember.getUsername()))
                .thenReturn(response);
        when(bulletinRepository.findWithLectureByMemberId(response))
                .thenReturn(response);

        // when
        MyLeadTeamsResponse res = teamService.getMyLeadTeamWithBulletinLecture();

        // then
        assertAll(
                () -> verify(teamQueryRepository).findMyTeamByUsername(loginMember.getUsername()),
                () -> verify(bulletinRepository).findWithLectureByMemberId(response),
                () -> assertEquals(res.getMyLeadTeams().size(), 2)
        );
    }

    @Test
    @DisplayName("팀장이 팀을 나간다.(팀 삭제)")
    void delete_team_test() throws Exception {

        // given
        Team team = createTeamApplierAndTeamMembers();
        Long teamId = team.getId();
        Member leader = team.getLeader();

        Bulletin bulletin = BulletinDummy.createBulletinWithId(20L, "테스트 모집글");
        bulletin.setTeam(team);

        Member addMember1 = Member.builder().id(35L).nickname("찜1").build();
        Member addMember2 = Member.builder().id(36L).nickname("찜2").build();
        when(teamLeaderAuthorityCheckable.findLeader(teamId))
                .thenReturn(team);
        when(bulletinRepository.findIdByTeamId(teamId)).thenReturn(Optional.of(bulletin.getId()));

        // when
        TeamExitResponse res = teamService.deleteTeam(teamId);

        // then
        assertAll(
                () -> verify(teamLeaderAuthorityCheckable).findLeader(teamId),
                () -> verify(bulletinRepository).findIdByTeamId(teamId),
                () -> verify(bulletinRepository).deleteFavoriteBulletinsById(bulletin.getId()),
                () -> verify(bulletinRepository).deleteById(bulletin.getId()),
                () -> verify(teamQueryRepository).deleteTeamAppliers(teamId),
                () -> verify(teamQueryRepository).deleteTeamLectures(teamId),
                () -> verify(teamQueryRepository).deleteTeamMembers(teamId),
                () -> verify(teamQueryRepository).deleteTeamCategories(teamId),
                () -> verify(teamRepository).deleteById(teamId),
                () -> assertEquals(res.getTeamId(), teamId),
                () -> assertEquals(res.getMemberId(), leader.getId())
        );
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

    private Member createMember(int i) {
        return Member.builder().id((long) i).username("테스트유저" + String.valueOf(i)).sex(Sex.F).build();
    }

    private List<Long> createCategoryList() {
        List<Category> categoryList = List.of(
                Category.builder().id(1L).build(),
                Category.builder().id(2L).build(),
                Category.builder().id(3L).build());
        return categoryList.stream().map(Category::getId).collect(toList());
    }

    private TeamDetailResponse createTeamDetailResponse() {
        return TeamDetailResponse.builder()
                .teamName("테스트1")
                .day("MON")
                .time("MORNING")
                .applyPeople(2)
                .maxPeople(3)
                .zoneId(1L)
                .iAmLeader(false)
                .teamsLeader(
                        TeamDetailResponse.TeamMembers.builder()
                                .imageURL("test")
                                .nickname("leader")
                                .sex("M")
                                .age(20)
                                .text("test")
                                .build())
                .categoryList(Set.of(1L, 2L))
                .teamMemberList(Set.of(
                        TeamDetailResponse.TeamMembers.builder()
                                .memberId(1L)
                                .imageURL("test")
                                .nickname("member1")
                                .sex("M")
                                .age(20)
                                .text("test")
                                .build(),
                        TeamDetailResponse.TeamMembers.builder()
                                .memberId(2L)
                                .imageURL("test")
                                .nickname("member2")
                                .sex("M")
                                .age(20)
                                .text("test")
                                .build()))
                .favoriteLectureList(Set.of(
                        TeamDetailResponse.FavoriteLecture.builder()
                                .lectureId(1L)
                                .lecturesZoneId(1L)
                                .lecturesTitle("lecture1")
                                .lecturesPrice(20000)
                                .lecturesImageURL("test")
                                .myFavoriteLecture(false)
                                .build(),
                        TeamDetailResponse.FavoriteLecture.builder()
                                .lectureId(2L)
                                .lecturesZoneId(2L)
                                .lecturesTitle("lecture2")
                                .lecturesPrice(20000)
                                .lecturesImageURL("test")
                                .myFavoriteLecture(false)
                                .build(),
                        TeamDetailResponse.FavoriteLecture.builder()
                                .lectureId(3L)
                                .lecturesZoneId(3L)
                                .lecturesTitle("lecture3")
                                .lecturesPrice(20000)
                                .lecturesImageURL("test")
                                .myFavoriteLecture(true)
                                .build())).build();
    }
}
