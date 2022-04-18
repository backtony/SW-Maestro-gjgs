package com.gjgs.gjgs.modules.favorite.repository.impl;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.favorite.dto.LectureTeamDto;
import com.gjgs.gjgs.modules.favorite.entity.LectureTeam;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.LectureTeamQueryRepository;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.LectureTeamRepository;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


class LectureTeamQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired LectureTeamRepository lectureTeamRepository;
    @Autowired LectureTeamQueryRepository lectureTeamQueryRepository;

    @AfterEach
    void tearDown() throws Exception {
        lectureTeamRepository.deleteAll();
    }

    @DisplayName("팀이 찜한 클래스 정보 가져오기")
    @Test
    void find_lecture_by_team() throws Exception {

        //given
        LectureTeam lectureTeam = lectureTeamRepository.save(LectureTeam.of(lecture, team));
        flushAndClear();

        //when
        List<LectureTeamDto> lectureTeamDtoList
                = lectureTeamQueryRepository.findNotFinishedLectureByTeamId(team.getId());
        LectureTeamDto dto = lectureTeamDtoList.get(0);

        //then
        assertAll(
                () -> assertEquals(1, lectureTeamDtoList.size()),
                () -> assertEquals(lectureTeam.getId(), dto.getLectureTeamId()),
                () -> assertEquals(lecture.getId(), dto.getLectureId()),
                () -> assertEquals(lecture.getThumbnailImageFileUrl(), dto.getThumbnailImageFileUrl()),
                () -> assertEquals(zone.getId(), dto.getZoneId()),
                () -> assertEquals(lecture.getTitle(), dto.getTitle()),
                () -> assertEquals(lecture.getPrice(), dto.getPrice())
        );
    }

    @DisplayName("팀 Id 리스트 중에서 lectureId를 찜한 팀 Id 찾기")
    @Test
    void find_team_by_lectureId_and_teamIdList() throws Exception {

        //given
        Team team2 = teamRepository.save(TeamDummy.createTeam(leader, zone));
        Team team3 = teamRepository.save(TeamDummy.createTeam(leader, zone));
        Lecture lecture = lectureRepository.save(LectureDummy.createDataJpaTestLecture(zone, category, director));
        lectureTeamRepository.save(LectureTeam.of(lecture, team));
        flushAndClear();

        //when
        List<Long> teamIdList = lectureTeamQueryRepository
                .findTeamByLectureIdAndTeamIdList(lecture.getId(),
                        Arrays.asList(team.getId(), team2.getId(), team3.getId()));
        //then
        assertAll(
                () -> assertEquals(1, teamIdList.size()),
                () -> assertEquals(team.getId(), teamIdList.get(0))
        );
    }
}
