package com.gjgs.gjgs.modules.lecture.services.admin;

import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.lecture.dtos.admin.RejectReason;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RejectLectureTest {

    @Mock LectureRepository lectureRepository;
    @InjectMocks RejectLecture rejectLecture;

    @Test
    @DisplayName("클래스 검수 거절하기")
    void reject_lecture_test() throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        Lecture lecture = LectureDummy.createLecture(1, director);
        RejectReason reason = RejectReason.builder().rejectReason("취소테스트").build();
        stubbingFindLecture(lecture);

        // when
        rejectLecture.decide(lecture.getId(), reason);

        // then
        assertAll(
                () -> verify(lectureRepository).findById(lecture.getId()),
                () -> assertEquals(lecture.getLectureStatus(), LectureStatus.REJECT),
                () -> assertEquals(lecture.getRejectReason(), reason.getRejectReason())
        );
    }

    private void stubbingFindLecture(Lecture lecture) {
        when(lectureRepository.findById(lecture.getId())).thenReturn(Optional.of(lecture));
    }
}