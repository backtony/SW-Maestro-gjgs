package com.gjgs.gjgs.modules.favorite.repository.impl;


import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.favorite.dto.LectureMemberDto;
import com.gjgs.gjgs.modules.favorite.entity.LectureMember;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.LectureMemberQueryRepository;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.LectureMemberRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LectureMemberQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired LectureMemberQueryRepository lectureMemberQueryRepository;
    @Autowired LectureMemberRepository lectureMemberRepository;

    Member member;
    LectureMember lectureMember;

    @BeforeEach
    void setup(){
        member = anotherMembers.get(0);
        lectureMember = lectureMemberRepository.save(LectureMember.of(lecture, member));
    }

    @AfterEach
    void teardown(){
        lectureMemberRepository.deleteAll();
    }

    @DisplayName("내가 찜한 강의 찾기")
    @Test
    void find_lecture_by_username() throws Exception {

        //given
        lectureRepository.save(LectureDummy.createDataJpaTestAcceptLecture(zone, category, director));
        flushAndClear();

        //when
        List<LectureMemberDto> lectureMemberDtoList
                = lectureMemberQueryRepository.findNotFinishedLectureByUsername(member.getUsername());
        LectureMemberDto dto = lectureMemberDtoList.get(0);

        //then
        assertAll(
                () -> assertEquals(1, lectureMemberDtoList.size()),
                () -> assertEquals(lectureMember.getId(), dto.getLectureMemberId()),
                () -> assertEquals(zone.getId(), dto.getZoneId()),
                () -> assertEquals(lecture.getThumbnailImageFileUrl(), dto.getThumbnailImageFileUrl()),
                () -> assertEquals(lecture.getTitle(), dto.getTitle()),
                () -> assertEquals(lecture.getPrice(), dto.getPrice())
        );
    }

    @DisplayName("username과 lectureId로 lectureMember의 Id 가져오기")
    @Test
    void find_with_member_and_favoriteLecture_by_lectureId_and_memberUsername() throws Exception {
        // given
        flushAndClear();

        //when
        Long lectureMemberId = lectureMemberQueryRepository
                .findIdByLectureIdAndUsername(lecture.getId(), member.getUsername()).get();

        //then
        assertEquals(lectureMember.getId(),lectureMemberId);
    }

    @DisplayName("username과 lectureId로 lectureMember의 Id 가져오기")
    @Test
    void find_favoriteLectureIdList_by_memberId() throws Exception {
        // given
        flushAndClear();

        // when
        List<Long> favoriteLectureIdList = lectureMemberQueryRepository.findFavoriteLectureIdListByMemberId(member.getId());

        // then
        assertEquals(favoriteLectureIdList.size(), 1);
    }
}
