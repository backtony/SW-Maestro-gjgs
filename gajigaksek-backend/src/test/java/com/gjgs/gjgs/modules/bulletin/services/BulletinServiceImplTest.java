package com.gjgs.gjgs.modules.bulletin.services;

import com.gjgs.gjgs.modules.bulletin.dto.*;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchCondition;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.bulletin.repositories.BulletinRepository;
import com.gjgs.gjgs.modules.dummy.BulletinDtoDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.exception.team.TeamMemberMaxException;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.ACCEPT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulletinServiceImplTest {

    @Mock TeamRepository teamRepository;
    @Mock LectureRepository lectureRepository;
    @Mock BulletinRepository bulletinRepository;
    @Mock SecurityUtil securityUtil;
    @InjectMocks BulletinServiceImpl bulletinService;

    @Test
    @DisplayName("그룹 모집글 생성")
    void create_bulletin_test() throws Exception {

        // given
        CreateBulletinRequest req = BulletinDtoDummy.buildCreateBulletinDto();
        Bulletin savedBulletin = Bulletin.builder().id(10L).build();
        Team team = TeamDummy.createUniqueTeam();
        ReflectionTestUtils.setField(team, "id", 1L);
        Lecture lecture = LectureDummy.createLecture(1);
        ReflectionTestUtils.setField(lecture, "id", 1L);
        when(teamRepository.findById(any())).thenReturn(Optional.of(team));
        when(lectureRepository.existsLectureByIdAndLectureStatus(req.getLectureId(), ACCEPT)).thenReturn(true);
        when(bulletinRepository.save(any(Bulletin.class))).thenReturn(savedBulletin);

        // when
        BulletinIdResponse res = bulletinService.createBulletin(req);

        // then
        assertAll(
                () -> verify(teamRepository).findById(any()),
                () -> verify(lectureRepository).existsLectureByIdAndLectureStatus(req.getLectureId(), ACCEPT),
                () -> assertEquals(res.getBulletinId(), savedBulletin.getId()),
                () -> assertEquals(res.getTeamId(), 1L),
                () -> assertEquals(res.getLectureId(), req.getLectureId())
        );
    }

    @Test
    @DisplayName("그룹 모집글 생성, 만약 팀의 빈자리가 없을 경우 에러 발생")
    void create_bulletin_should_need_exist_team_member_seat() throws Exception {

        // given
        CreateBulletinRequest req = BulletinDtoDummy.buildCreateBulletinDto();
        Team team = createMaxTeam();
        ReflectionTestUtils.setField(team, "id", 1L);
        Lecture lecture = LectureDummy.createLecture(1);
        ReflectionTestUtils.setField(lecture, "id", 1L);
        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(team));
        when(lectureRepository.existsLectureByIdAndLectureStatus(req.getLectureId(), ACCEPT)).thenReturn(true);

        // when, then
        assertThrows(TeamMemberMaxException.class,
                () -> bulletinService.createBulletin(req),
                "현재 팀의 인원이 모두 차있어서 발생.");
    }

    private Team createMaxTeam() {
        return Team.builder()
                .maxPeople(4).currentMemberCount(4)
                .build();
    }

    @Test
    @DisplayName("그룹 게시글 삭제하기(비활성화!)")
    void delete_bulletin_test() throws Exception {

        // given
        Bulletin bulletin = Bulletin.builder()
                .status(true).id(1L)
                .build();
        when(bulletinRepository.findById(any()))
                .thenReturn(Optional.of(bulletin));

        // when
        BulletinIdResponse res = bulletinService.deleteBulletin(1L);

        // then
        assertAll(
                () -> assertEquals(res.getBulletinId(), 1L),
                () -> assertNull(res.getLectureId()),
                () -> assertNull(res.getTeamId()),
                () -> assertFalse(bulletin.isStatus())
        );
    }

    @Test
    @DisplayName("게시글 수정하기")
    void modify_bulletin_test() throws Exception {

        // given
        Team team = Team.builder().id(1L).maxPeople(4).currentMemberCount(1).build();
        Lecture lecture = Lecture.builder().id(1L).build();
        Bulletin bulletin = Bulletin.builder().id(1L).build();
        bulletin.setLecture(lecture);
        bulletin.setTeam(team);
        CreateBulletinRequest req = BulletinDtoDummy.buildCreateBulletinDto();
        when(bulletinRepository.findWithLectureTeam(any()))
                .thenReturn(Optional.of(bulletin));
        when(lectureRepository.existsLectureByIdAndLectureStatus(req.getLectureId(), ACCEPT))
                .thenReturn(true);

        // when
        BulletinIdResponse res = bulletinService.modifyBulletin(1L, req);

        // then
        assertAll(
                () -> verify(lectureRepository).existsLectureByIdAndLectureStatus(req.getLectureId(), ACCEPT),
                () -> assertEquals(bulletin.getTitle(), req.getTitle()),
                () -> assertEquals(bulletin.getAge().name(), req.getAge()),
                () -> assertEquals(bulletin.getTimeType(), req.getTimeType()),
                () -> assertEquals(bulletin.getDayType(), req.getDayType()),
                () -> assertEquals(bulletin.getDescription(), req.getText()),
                () -> assertEquals(res.getLectureId(), req.getLectureId()),
                () -> assertEquals(res.getBulletinId(), 1L)
        );
    }

    @Test
    @DisplayName("게시글 조회하기(페이징)")
    void get_bulletins_test() throws Exception {

        // given
        Pageable pageable = PageRequest.of(0, 12);
        BulletinSearchCondition cond = BulletinSearchCondition
                .builder().build();
        Page<BulletinSearchResponse> resStub = new PageImpl<>(
                BulletinDtoDummy.createBulletinSearchResponseList(),
                pageable,
                100);
        when(bulletinRepository.searchBulletin(pageable, cond))
                .thenReturn(resStub);

        // when
        Page<BulletinSearchResponse> res = bulletinService.getBulletins(pageable, cond);

        // then
        assertAll(
                () -> verify(bulletinRepository).searchBulletin(pageable, cond),
                () -> assertEquals(res.getContent().size(), 12),
                () -> assertEquals(res.getTotalPages(), 9)
        );
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void get_bulletin_details_not_leader() throws Exception {

        // given
        BulletinDetailResponse res = BulletinDtoDummy.createBulletinDetailResponse();
        when(bulletinRepository.findBulletinDetail(any()))
                .thenReturn(res);

        // when
        BulletinDetailResponse result = bulletinService.getBulletinDetails(1L);

        // then
        assertAll(
                () -> verify(bulletinRepository).findBulletinDetail(any()),
                () -> assertFalse(result.getBulletinsTeam().isIAmLeader()),
                () -> assertFalse(result.getBulletinsLecture().isMyFavoriteLecture())
        );
    }

    @Test
    @DisplayName("이미 게시글이 있는 경우(비활성화) 활성화 및 수정시켜주기")
    void create_bulletin_should_activate_if_exist_bulletin_test() throws Exception {

        // given
        Lecture lecture = Lecture.builder().id(20L).build();
        Lecture anotherLecture = Lecture.builder().id(3L).build();
        Team team = Team.builder().id(1L).teamName("테스트팀1").currentMemberCount(1).maxPeople(4).build();
        Bulletin bulletin = Bulletin.builder().id(2L).title("테스트모집글")
                .status(false).build();
        bulletin.setTeam(team);
        bulletin.setLecture(lecture);
        CreateBulletinRequest req = CreateBulletinRequest.builder()
                .timeType("MORNING")
                .dayType("MON")
                .lectureId(3L).teamId(1L).title("두번째모집글").text("test")
                .age("TWENTY_TO_TWENTYFIVE").build();
        when(bulletinRepository.findWithTeamByTeamId(req.getTeamId()))
                .thenReturn(Optional.of(bulletin));
        when(bulletinRepository.findWithLectureTeam(bulletin.getId()))
                .thenReturn(Optional.of(bulletin));
        when(lectureRepository.existsLectureByIdAndLectureStatus(req.getLectureId(), ACCEPT)).thenReturn(true);

        // when
        BulletinIdResponse res = bulletinService.createBulletin(req);

        // then
        assertAll(
                () -> verify(bulletinRepository).findWithTeamByTeamId(req.getTeamId()),
                () -> verify(bulletinRepository).findWithLectureTeam(bulletin.getId()),
                () -> verify(lectureRepository).existsLectureByIdAndLectureStatus(req.getLectureId(), ACCEPT),
                () -> assertTrue(bulletin.isStatus()),
                () -> assertEquals(res.getBulletinId(), bulletin.getId()),
                () -> assertEquals(res.getLectureId(), anotherLecture.getId())
        );
    }

    @Test
    @DisplayName("이미 게시글이 있는 경우(비활성화) 활성화 및 수정시켜주기 / 팀이 이미 다 차있는 상황에서는 예외 발생")
    void create_bulletin_should_deactivate_if_exist_bulletin_full_team_test() throws Exception {

        // given
        Lecture lecture = Lecture.builder().id(20L).build();
        Lecture anotherLecture = Lecture.builder().id(3L).build();
        Team team = Team.builder().id(1L).teamName("테스트팀1").currentMemberCount(4).maxPeople(4).build();
        Bulletin bulletin = Bulletin.builder().id(2L).title("테스트모집글")
                .status(false).build();
        bulletin.setTeam(team);
        bulletin.setLecture(lecture);
        CreateBulletinRequest req = CreateBulletinRequest.builder()
                .timeType("MORNING")
                .dayType("MON")
                .lectureId(3L).teamId(1L).title("두번째모집글").text("test")
                .age("TWENTY_TO_TWENTYFIVE").build();
        when(bulletinRepository.findWithTeamByTeamId(req.getTeamId()))
                .thenReturn(Optional.of(bulletin));
        when(bulletinRepository.findWithLectureTeam(bulletin.getId()))
                .thenReturn(Optional.of(bulletin));
        when(lectureRepository.existsLectureByIdAndLectureStatus(req.getLectureId(), ACCEPT))
                .thenReturn(true);

        // when
        assertThrows(TeamMemberMaxException.class,
                () -> bulletinService.createBulletin(req),
                "모집할 수 있는 멤버의 자리가 있어야 한다.");
    }

    @Test
    @DisplayName("모집글 상태 변경 / 모집중 > 모집 종료")
    void bulletin_close_test() throws Exception {

        // given
        Member leader = Member.builder()
                .id(1L).username("leader")
                .build();
        Bulletin recruitBulletin = Bulletin.builder()
                .id(1L)
                .status(true)
                .team(Team.builder()
                        .leader(leader)
                        .currentMemberCount(3)
                        .maxPeople(4)
                        .build())
                .build();
        Long bulletinId = recruitBulletin.getId();
        when(bulletinRepository.findWithTeamLeaderByBulletinIdLeaderUsername(bulletinId, leader.getUsername()))
                .thenReturn(Optional.of(recruitBulletin));
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(leader.getUsername()));

        // when
        BulletinChangeRecruitResponse response = bulletinService.changeRecruitStatus(bulletinId);

        // then
        assertAll(
                () -> verify(bulletinRepository).findWithTeamLeaderByBulletinIdLeaderUsername(bulletinId, leader.getUsername()),
                () -> verify(securityUtil).getCurrentUsername(),
                () -> assertEquals(response.getBulletinId(), bulletinId),
                () -> assertEquals(response.getRecruitStatus(), RecruitStatus.CLOSE.name())
        );
    }

    @Test
    @DisplayName("모집글 상태 변경 / 모집 종료 > 모집중")
    void bulletin_recruit_test() throws Exception {

        // given
        Member leader = Member.builder()
                .id(1L).username("leader")
                .build();
        Bulletin closeBulletin = Bulletin.builder()
                .id(1L)
                .status(false)
                .team(Team.builder()
                        .leader(leader)
                        .currentMemberCount(3)
                        .maxPeople(4)
                        .build())
                .build();
        Long bulletinId = closeBulletin.getId();
        when(bulletinRepository.findWithTeamLeaderByBulletinIdLeaderUsername(bulletinId, leader.getUsername()))
                .thenReturn(Optional.of(closeBulletin));
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(leader.getUsername()));

        // when
        BulletinChangeRecruitResponse response = bulletinService.changeRecruitStatus(bulletinId);

        // then
        assertAll(
                () -> verify(bulletinRepository).findWithTeamLeaderByBulletinIdLeaderUsername(bulletinId,  leader.getUsername()),
                () -> verify(securityUtil).getCurrentUsername(),
                () -> assertEquals(response.getBulletinId(), bulletinId),
                () -> assertEquals(response.getRecruitStatus(), RecruitStatus.RECRUIT.name())
        );
    }

    @Test
    @DisplayName("모집글 상태 변경 / 모집 종료 > 모집중일 시, 멤버가 모두 차있는 경우 예외 발생")
    void bulletin_recruit_should_not_recruit_team_member_is_full() throws Exception {

        // given
        Member leader = Member.builder()
                .id(1L).username("leader")
                .build();
        Bulletin closeBulletin = Bulletin.builder()
                .id(1L)
                .status(false)
                .team(Team.builder()
                        .leader(leader)
                        .currentMemberCount(4)
                        .maxPeople(4)
                        .build())
                .build();
        Long bulletinId = closeBulletin.getId();
        when(bulletinRepository.findWithTeamLeaderByBulletinIdLeaderUsername(bulletinId, leader.getUsername()))
                .thenReturn(Optional.of(closeBulletin));
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(leader.getUsername()));

        // when, then
        assertThrows(TeamMemberMaxException.class,
                () -> bulletinService.changeRecruitStatus(bulletinId),
                "이미 팀원이 꽉 차있을 경우 모집중으로 변경이 불가능하다.");
    }
}
