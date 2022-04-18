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
class PutIntroServiceImplTest {

    @Mock LectureQueryRepository lectureQueryRepository;
    @Mock SecurityUtil securityUtil;
    @Mock SaveDeleteFileManager saveDeleteFileManager;
    @Mock LectureJdbcRepository lectureJdbcRepository;
    @InjectMocks
    PutIntroServiceImpl putIntroService;

    private final FilePaths PATH = FilePaths.LECTURE_IMAGE_PATH;

    @Test
    @DisplayName("완성작 저장하기 / 기존에 있었던 정보일 경우 사진도 지워줘야 한다.")
    void put_lecture_intro_test() throws Exception{

        // given
        Member director = createDirector();
        stubbingCurrentUsername(director);
        Lecture lecture = LectureDummy.createHaveTwoScheduleThreeCurriculumFourFinishedProduct(1, director);
        stubbingFindLecture(director.getUsername(), lecture);
        CreateLecture.IntroRequest introRequest = createIntroRequest();
        List<MultipartFile> files = getMockMultipartFileList(4);
        List<FileInfoVo> fileInfoList = getFileInfo(4);
        List<String> finishedProductsFileNames = lecture.getFinishedProductsFileNames();
        stubbingDeleteSaveFiles(PATH, finishedProductsFileNames, files, fileInfoList);

        // when
        CreateLectureProcessResponse res = putIntroService.putLectureProcess(introRequest, files);

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(lectureQueryRepository).findWithFinishedProductsByDirectorUsername(director.getUsername()),
                () -> verify(saveDeleteFileManager).deleteAndSaveFiles(PATH, finishedProductsFileNames, files),
                () -> verify(lectureQueryRepository).deleteFinishedProductsByLectureId(lecture.getId()),
                () -> verify(lectureJdbcRepository).insertFinishedProduct(lecture),
                () -> assertEquals(res.getCompletedStepName(), CreateLectureStep.INTRO.name()),
                () -> assertEquals(lecture.getFinishedProductList().size(), 4)
        );
    }


    @Test
    @DisplayName("이미 저장된 Lecture 가 없는 경우 예외 발생")
    void put_lecture_intro_not_exist_test() throws Exception {

        // given
        Member director = createDirector();
        stubbingCurrentUsername(director);
        stubbingFindLectureIsEmpty(director);
        CreateLecture.IntroRequest introRequest = createIntroRequest();
        List<MultipartFile> files = getMockMultipartFileList(4);

        // when, then
        assertThrows(TemporaryNotSaveLectureException.class,
                () -> putIntroService.putLectureProcess(introRequest, files),
                "기존에 임시 저장된 Lecture가 없을 경우 예외 발생");
    }

    @Test
    @DisplayName("file 갯수와 FinishedProduct 갯수가 맞지 않을 때 예외 발생")
    void put_lecture_intro_should_equal_file_finished_product_test() throws Exception {

        // given
        Member director = createDirector();
        CreateLecture.IntroRequest introRequest = createIntroRequest();
        List<MultipartFile> files = getMockMultipartFileList(2);

        // when, then
        assertThrows(ProductAndFileNotEqualException.class,
                () -> putIntroService.putLectureProcess(introRequest, files),
                "file 갯수와 request의 완성작 정보 리스트의 갯수가 같아야 한다.");
    }

    private void stubbingFindLectureIsEmpty(Member director) {
        when(lectureQueryRepository.findWithFinishedProductsByDirectorUsername(director.getUsername()))
                .thenReturn(Optional.empty());
    }

    private List<FileInfoVo> getFileInfo(int count) {
        List<FileInfoVo> fileInfoList = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            fileInfoList.add(FileInfoVo.builder().fileName("name").fileUrl("url").build());
        }
        return fileInfoList;
    }

    private void stubbingFindLecture(String username, Lecture lecture) {
        when(lectureQueryRepository.findWithFinishedProductsByDirectorUsername(username))
                .thenReturn(Optional.of(lecture));
    }

    private void stubbingCurrentUsername(Member director) {
        when(securityUtil.getCurrentUsername())
                .thenReturn(Optional.of(director.getUsername()));
    }

    private void stubbingDeleteSaveFiles(FilePaths path, List<String> fileNames, List<MultipartFile> files, List<FileInfoVo> fileInfos) throws IOException {
        when(saveDeleteFileManager.deleteAndSaveFiles(path, fileNames, files))
                .thenReturn(fileInfos);
    }

    private CreateLecture.IntroRequest createIntroRequest() {
        return CreateLecture.IntroRequest.builder()
                .createLectureStep(CreateLectureStep.INTRO)
                .mainText("testtest")
                .finishedProductInfoList(List.of(
                        CreateLecture.FinishedProductInfoDto.builder().text("test").order(1).build(),
                        CreateLecture.FinishedProductInfoDto.builder().text("test").order(2).build(),
                        CreateLecture.FinishedProductInfoDto.builder().text("test").order(3).build(),
                        CreateLecture.FinishedProductInfoDto.builder().text("test").order(4).build()
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
