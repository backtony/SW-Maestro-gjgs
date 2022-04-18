package com.gjgs.gjgs.modules.lecture.services.director.lecture;

import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.CREATING;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectorLectureServiceImplTest {

    @Mock LectureQueryRepository lectureQueryRepository;
    @Mock SecurityUtil securityUtil;
    @InjectMocks DirectorLectureServiceImpl directorLectureService;

    @Test
    @DisplayName("검수 거절된 클래스 재작성하기")
    void change_lecture_creating_test() throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        Lecture lecture = LectureDummy.createLecture(1, director);
        stubbingGetCurrentUsername(director);
        stubbingExistCreatingLecture(director, lecture);

        // when
        directorLectureService.changeLectureCreating(lecture.getId());

        // then
        assertAll(
                () -> verify(lectureQueryRepository).existCreatingLectureByUsername(director.getUsername()),
                () -> verify(lectureQueryRepository).findRejectLectureEntityById(lecture.getId()),
                () -> assertEquals(lecture.getLectureStatus(), CREATING)
        );
    }

    private void stubbingGetCurrentUsername(Member director) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(director.getUsername()));
    }

    private void stubbingExistCreatingLecture(Member director, Lecture lecture) {
        when(lectureQueryRepository.existCreatingLectureByUsername(director.getUsername())).thenReturn(false);
        when(lectureQueryRepository.findRejectLectureEntityById(lecture.getId())).thenReturn(Optional.of(lecture));
    }
}