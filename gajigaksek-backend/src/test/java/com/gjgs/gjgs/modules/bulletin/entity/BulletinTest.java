package com.gjgs.gjgs.modules.bulletin.entity;

import com.gjgs.gjgs.modules.bulletin.dto.CreateBulletinRequest;
import com.gjgs.gjgs.modules.dummy.BulletinDtoDummy;
import com.gjgs.gjgs.modules.dummy.BulletinDummy;
import com.gjgs.gjgs.modules.exception.team.NotTeamLeaderException;
import com.gjgs.gjgs.modules.exception.team.TeamMemberMaxException;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BulletinTest {

    @Test
    @DisplayName("bulletin과 lecture 연관관계 맺어주기")
    void set_bulletin_and_lecture_test() throws Exception {

        // given
        Bulletin bulletin = Bulletin.builder().id(1L).build();
        Lecture lecture = Lecture.builder().id(2L).build();

        // when
        bulletin.setLecture(lecture);

        // then
        assertThat(bulletin.getLecture()).isEqualTo(lecture);
    }

    @Test
    @DisplayName("bulletin과 team 연관관계 맺어주기")
    void set_bulletin_and_team_test() throws Exception {

        // given
        Bulletin bulletin = Bulletin.builder().id(1L).build();
        Team team = Team.builder().id(2L).build();

        // when
        bulletin.setTeam(team);

        // then
        assertThat(bulletin.getTeam()).isEqualTo(team);
    }

    @Test
    @DisplayName("Bulletin 비활성화")
    void change_status_test() throws Exception {

        // given
        Bulletin bulletin = Bulletin.builder().id(1L).status(true).build();

        // when
        bulletin.stopRecruit();

        // then
        assertFalse(bulletin.isStatus());
    }

    @Test
    @DisplayName("Bulletin 수정하기")
    void modify_bulletin_test() throws Exception {

        // given
        CreateBulletinRequest req = BulletinDtoDummy.buildCreateBulletinDto();
        Bulletin bulletin = Bulletin.builder()
                .team(Team.builder().currentMemberCount(1).maxPeople(4).build())
                .build();

        // when
        bulletin.modify(req);

        // then
        assertAll(
                () -> assertEquals(bulletin.getTitle(), req.getTitle()),
                () -> assertEquals(bulletin.getDescription(), req.getText()),
                () -> assertEquals(bulletin.getAge().name(), req.getAge()),
                () -> assertEquals(bulletin.getTimeType(), req.getTimeType()),
                () -> assertEquals(bulletin.getDayType(), req.getDayType())
        );
    }

    @Test
    @DisplayName("해당 모집글의 클래스와 수정을 원하는 클래스가 다른지 확인하기")
    void is_difference_lecture_test() throws Exception {

        // given
        Team team = Team.builder()
                .id(20L)
                .build();
        Lecture lecture = Lecture.builder()
                .id(20L)
                .build();
        Bulletin bulletin = BulletinDummy.createBulletin(team, lecture);
        Long changeLectureId = 129738L;

        // when, then
        assertTrue(bulletin.isDifferenceLecture(changeLectureId));
    }

    @Test
    @DisplayName("해당 모집글의 클래스 ID를 반환하기")
    void get_lecture_id_test() throws Exception {

        // given
        Team team = Team.builder()
                .id(20L)
                .build();
        Lecture lecture = Lecture.builder()
                .id(20L)
                .build();
        Bulletin bulletin = BulletinDummy.createBulletin(team, lecture);

        // when, then
        assertEquals(bulletin.getLectureId(), lecture.getId());
    }

    @Test
    @DisplayName("모집 상태 변경하기 / 팀원이 이미 찼을 경우")
    void check_leader_change_recruit_status_should_team_is_not_full() {

        // given
        Member leader = Member.builder()
                .id(1L).username("leader")
                .build();
        Bulletin bulletin = Bulletin.builder()
                .status(false)
                .team(Team.builder()
                        .leader(leader)
                        .currentMemberCount(4).maxPeople(4)
                        .build())
                .build();

        // when, then
        assertThrows(TeamMemberMaxException.class,
                bulletin::changeRecruitStatus,
                "팀 멤버가 이미 찼을 경우 활성상태로 불가능하다.");
    }

    @Test
    @DisplayName("모집 상태 변경하기 / 모집 상태 활성화")
    void check_leader_change_recruit_status_test() {

        // given
        Member leader = Member.builder()
                .id(1L).username("leader")
                .build();
        Bulletin bulletin = Bulletin.builder()
                .status(false)
                .team(Team.builder()
                        .leader(leader)
                        .currentMemberCount(2).maxPeople(4)
                        .build())
                .build();

        // when
        bulletin.changeRecruitStatus();

        // then
        assertAll(
                () -> assertTrue(bulletin.isStatus()),
                () -> assertDoesNotThrow(() -> NotTeamLeaderException.class),
                () -> assertDoesNotThrow(() -> TeamMemberMaxException.class)
        );
    }
}
