package com.gjgs.gjgs.modules.lecture.repositories.interfaces;


import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LectureRepositoryTest extends SetUpLectureTeamBulletinRepository {

    @DisplayName("lectureId, username, CREATING 상태의 클래스를 가져온다.")
    @Test
    void find_by_id_username_test() throws Exception {

        // given
        int count = (int) memberRepository.count();
        Member anotherDirector = memberRepository.save(MemberDummy.createDataJpaTestDirector(count + 1, zone, category));
        flushAndClear();

        // when
        Lecture findLecture = lectureRepository.findByIdUsername(lecture.getId(), director.getUsername()).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(findLecture.getId(), lecture.getId()),
                () -> assertEquals(findLecture.getLectureStatus(), LectureStatus.CREATING),
                () -> assertThrows(Exception.class,
                        () -> lectureRepository.findByIdUsername(lecture.getId(), anotherDirector.getUsername()).orElseThrow())
        );
    }

    @DisplayName("디렉터의 username으로 만드는 중인 클래스 가져오기")
    @Test
    void find_creating_lecture_by_director_username_test() throws Exception {

        // given
        flushAndClear();

        // when
        Lecture findLecture = lectureRepository.findCreatingLectureByDirectorUsername(director.getUsername()).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(findLecture.getDirector().getUsername(), director.getUsername()),
                () -> assertEquals(findLecture.getLectureStatus(), LectureStatus.CREATING)
        );
    }

    @DisplayName("클래스 id와 상태로 존재하는지 확인하기")
    @Test
    void exists_lecture_by_id_and_lecture_status() throws Exception {

        //given
        flushAndClear();

        // when then
        assertTrue(lectureRepository.existsLectureByIdAndLectureStatus(lecture.getId(), LectureStatus.CREATING));
    }
}
