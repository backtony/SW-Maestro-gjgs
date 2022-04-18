package com.gjgs.gjgs.modules.lecture.services.temporaryStore.manage;

import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.PutLectureResponseDummy;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.TemporaryStorageLectureManageResponse;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.lecture.repositories.temporaryStore.TemporaryStoredLectureQueryRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.s3.interfaces.AmazonS3Service;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep.TERMS;
import static com.gjgs.gjgs.modules.lecture.dtos.create.TemporaryStorageLectureManageResponse.ResultResponse.DELETE;
import static com.gjgs.gjgs.modules.lecture.dtos.create.TemporaryStorageLectureManageResponse.ResultResponse.SAVE;
import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.CONFIRM;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemporaryStorageLectureManageServiceImplTest {

    @Mock LectureRepository lectureRepository;
    @Mock TemporaryStoredLectureQueryRepository tempStoreQueryRepository;
    @Mock SecurityUtil securityUtil;
    @Mock LectureQueryRepository lectureQueryRepository;
    @Mock AmazonS3Service amazonS3Service;
    @InjectMocks TemporaryStorageLectureManageServiceImpl saveLectureService;

    @Test
    @DisplayName("임시 저장했던 Lecture를 DB에 저장하기")
    void put_lecture_process_test() throws Exception {

        // given
        CreateLecture.TermsRequest req = createTermsRequest();
        Member director = MemberDummy.createTestDirectorMember();
        stubbingCurrentUsername(director);
        Lecture lecture = LectureDummy.createLecture(1, director);
        stubbingTempLecture(director, lecture);

        // when
        TemporaryStorageLectureManageResponse res = saveLectureService.saveLecture(req);

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(lectureRepository).findCreatingLectureByDirectorUsername(director.getUsername()),
                () -> assertEquals(res.getResultResponse(), SAVE),
                () -> assertEquals(lecture.getLectureStatus(), CONFIRM)
        );
    }

    @Test
    @DisplayName("임시 저장했던 Lecture를 삭제하기")
    void delete_temporary_storage_lecture_test() throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        stubbingCurrentUsername(director);
        // 사진 파일 총 다섯개
        PutLectureResponse lectureResponse = PutLectureResponseDummy.create();
        stubbingGetTempLecture(director, lectureResponse);

        // when
        TemporaryStorageLectureManageResponse res = saveLectureService.deleteTemporaryStorageLecture();

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(tempStoreQueryRepository).findByDirectorUsername(director.getUsername()),
                () -> verify(lectureQueryRepository).deleteSchedulesByLectureId(lectureResponse.getLectureId()),
                () -> verify(lectureQueryRepository).deleteCurriculumsByLectureId(lectureResponse.getLectureId()),
                () -> verify(lectureQueryRepository).deleteFinishedProductsByLectureId(lectureResponse.getLectureId()),
                () -> verify(lectureRepository).deleteById(lectureResponse.getLectureId()),
                () -> verify(amazonS3Service, times(lectureResponse.getAllFilenames().size())).delete(any(), any()),
                () -> assertEquals(res.getResultResponse(), DELETE)
        );
    }

    private void stubbingGetTempLecture(Member director, PutLectureResponse lectureResponse) {
        when(tempStoreQueryRepository.findByDirectorUsername(director.getUsername())).thenReturn(Optional.of(lectureResponse));
    }

    private void stubbingTempLecture(Member director, Lecture lecture) {
        when(lectureRepository.findCreatingLectureByDirectorUsername(director.getUsername())).thenReturn(Optional.of(lecture));
    }

    private void stubbingCurrentUsername(Member director) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(director.getUsername()));
    }

    private CreateLecture.TermsRequest createTermsRequest() {
        return CreateLecture.TermsRequest.builder()
                .createLectureStep(TERMS)
                .termsOne(true)
                .termsTwo(true)
                .termsThree(true)
                .termsFour(true)
                .build();
    }
}