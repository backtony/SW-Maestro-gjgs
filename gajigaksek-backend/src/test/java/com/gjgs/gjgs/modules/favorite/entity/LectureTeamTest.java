package com.gjgs.gjgs.modules.favorite.entity;

import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LectureTeamTest {

    @DisplayName("LectureTeam 생성")
    @Test
    void create_lectureTeam() throws Exception {
        //given
        Lecture lecture = LectureDummy.createLecture(1);
        Team team = TeamDummy.createUniqueTeam();

        //when
        LectureTeam lectureTeam = LectureTeam.of(lecture, team);

        //then
        assertAll(
                () -> assertEquals(lecture, lectureTeam.getLecture()),
                () -> assertEquals(team, lectureTeam.getTeam())
        );
    }
}
