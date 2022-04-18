package com.gjgs.gjgs.modules.lecture.services.temporaryStore.put;

import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.exception.lecture.ProductAndFileNotEqualException;
import com.gjgs.gjgs.modules.exception.lecture.TemporaryNotSaveLectureException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureProcessResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.utils.s3.FilePaths;
import com.gjgs.gjgs.modules.utils.s3.interfaces.SaveDeleteFileManager;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PutCurriculumServiceImplTest {

    @Mock SecurityUtil securityUtil;
    @Mock LectureQueryRepository lectureQueryRepository;
    @Mock SaveDeleteFileManager saveDeleteFileManager;
    @Mock LectureJdbcRepository lectureJdbcRepository;
    @InjectMocks
    PutCurriculumServiceImpl putCurriculumService;

    private final FilePaths PATH = FilePaths.LECTURE_IMAGE_PATH;

    @Test
    @DisplayName("사진의 갯수와 요청한 커리큘럼의 갯수가 맞지 않을 경우")
    void put_lecture_curriculum_should_equals_file_curriculum_count() throws Exception {

        // given
        List<MultipartFile> files = getMockMultipartFileList(5);
        CreateLecture.CurriculumRequest request = createCurriculumRequest();

        // when, then
        assertThrows(ProductAndFileNotEqualException.class,
                () -> putCurriculumService.putLectureProcess(request, files),
                "요청한 커리큘럼의 리스트 사이즈와 파일의 갯수는 같아야 한다.");
    }

    @Test
    @DisplayName("저장된 Lecture 가 없을 경우")
    void put_lecture_curriculum_lecture_not_found_test() throws Exception {

        // given
        Member director = createDirector();
        stubbingCurrentUsername(securityUtil, director);
        List<MultipartFile> files = getMockMultipartFileList(4);
        CreateLecture.CurriculumRequest request = createCurriculumRequest();
        stubbingFindLectureIsEmpty(director);

        // when
        assertThrows(TemporaryNotSaveLectureException.class,
                () -> putCurriculumService.putLectureProcess(request, files),
                "저장된 Lecture 가 있어야 한다.");
    }

    @Test
    @DisplayName("커리큘럼 저장하기")
    void put_lecture_curriculum() throws Exception {

        // given
        Member director = createDirector();
        stubbingCurrentUsername(securityUtil, director);
        List<MultipartFile> files = getMockMultipartFileList(4);
        CreateLecture.CurriculumRequest request = createCurriculumRequest();
        Lecture lecture = LectureDummy.createHaveTwoScheduleThreeCurriculumFourFinishedProduct(1, director);
        stubbingFindLecture(director, lecture);
        List<FileInfoVo> fileInfoList = getFileInfo(files.size());
        List<String> curriculumFileNames = lecture.getCurriculumFileNames();
        stubbingDeleteSaveFiles(PATH, curriculumFileNames, files, fileInfoList);

        // when
        CreateLectureProcessResponse res = putCurriculumService.putLectureProcess(request, files);

        //then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(lectureQueryRepository).findWithCurriculumsByDirectorUsername(director.getUsername()),
                () -> verify(saveDeleteFileManager).deleteAndSaveFiles(PATH, curriculumFileNames, files),
                () -> verify(lectureJdbcRepository).insertCurriculum(lecture),
                () -> verify(lectureQueryRepository).deleteCurriculumsByLectureId(lecture.getId()),
                () -> assertEquals(res.getCompletedStepName(), CreateLectureStep.CURRICULUM.name()),
                () -> assertEquals(lecture.getCurriculumList().size(), 4)
        );
    }

    private void stubbingDeleteSaveFiles(FilePaths path, List<String> filenames, List<MultipartFile> files, List<FileInfoVo> fileInfos) throws IOException {
        when(saveDeleteFileManager.deleteAndSaveFiles(path, filenames, files)).thenReturn(fileInfos);
    }

    private void stubbingFindLecture(Member director, Lecture lecture) {
        when(lectureQueryRepository.findWithCurriculumsByDirectorUsername(director.getUsername())).thenReturn(Optional.of(lecture));
    }

    private List<FileInfoVo> getFileInfo(int count) {
        List<FileInfoVo> fileInfoList = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            fileInfoList.add(FileInfoVo.builder().fileName("name").fileUrl("url").build());
        }
        return fileInfoList;
    }

    private void stubbingFindLectureIsEmpty(Member director) {
        when(lectureQueryRepository.findWithCurriculumsByDirectorUsername(director.getUsername())).thenReturn(Optional.empty());
    }

    private void stubbingCurrentUsername(SecurityUtil securityUtil, Member director) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(director.getUsername()));
    }

    private CreateLecture.CurriculumRequest createCurriculumRequest() {
        return CreateLecture.CurriculumRequest.builder()
                .createLectureStep(CreateLectureStep.CURRICULUM)
                .curriculumList(List.of(
                        CreateLecture.CurriculumDto.builder().order(1).title("test").detailText("test").build(),
                        CreateLecture.CurriculumDto.builder().order(2).title("test").detailText("test").build(),
                        CreateLecture.CurriculumDto.builder().order(3).title("test").detailText("test").build(),
                        CreateLecture.CurriculumDto.builder().order(4).title("test").detailText("test").build()
                ))
                .build();
    }

    private Member createDirector() {
        return Member.builder()
                .id(1L).authority(Authority.ROLE_DIRECTOR).username("director")
                .build();
    }

    private List<MultipartFile> getMockMultipartFileList(int count) {
        List<MultipartFile> files = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            files.add(getMockMultipartFile());
        }
        return files;
    }

    private MultipartFile getMockMultipartFile() {
        return new MockMultipartFile("files",
                "image.img",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "image".getBytes());
    }
}