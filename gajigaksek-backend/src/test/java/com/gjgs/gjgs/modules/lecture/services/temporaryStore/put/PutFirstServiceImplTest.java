package com.gjgs.gjgs.modules.lecture.services.temporaryStore.put;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.category.repositories.CategoryRepository;
import com.gjgs.gjgs.modules.dummy.CategoryDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.ZoneDummy;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureProcessResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.utils.s3.FilePaths;
import com.gjgs.gjgs.modules.utils.s3.interfaces.SaveDeleteFileManager;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PutFirstServiceImplTest {

    @Mock LectureRepository lectureRepository;
    @Mock SecurityUtil securityUtil;
    @Mock LectureQueryRepository lectureQueryRepository;
    @Mock SaveDeleteFileManager saveDeleteFileManager;
    @Mock ZoneRepository zoneRepository;
    @Mock CategoryRepository categoryRepository;
    @InjectMocks
    PutFirstServiceImpl putFirstService;

    private final FilePaths PATH = FilePaths.LECTURE_IMAGE_PATH;

    @Test
    @DisplayName("처음 저장하기")
    void put_lecture_first_test() throws Exception {

        // given
        Member director = createDirector();
        CreateLecture.FirstRequest firstRequest = createCreateLectureRequestFirst();
        List<MultipartFile> files = getMockMultipartFileList(1);
        Category category = CategoryDummy.createCategory(1L);
        stubbingFindCategory(firstRequest.getCategoryId(), category);
        Zone zone = ZoneDummy.createZone(1L);
        stubbingFindZone(firstRequest.getZoneId(), zone);
        stubbingCurrentUsername(securityUtil, director);
        stubbingFindWithCategoryZoneByDirectorUsername(director);
        List<FileInfoVo> fileInfoList = getFileInfo(1);
        stubbingDeleteSaveFiles(PATH, Collections.emptyList(), files, fileInfoList);
        stubbingGetCurrentMember(director);
        Lecture lecture = Lecture.of(fileInfoList, category, zone, firstRequest, director);
        stubbingSaveLecture(lecture);

        // when
        CreateLectureProcessResponse res = putFirstService.putLectureProcess(firstRequest, files);

        // then
        assertAll(
                () -> verify(saveDeleteFileManager).deleteAndSaveFiles(PATH, Collections.emptyList(), files),
                () -> verify(zoneRepository).findById(firstRequest.getZoneId()),
                () -> verify(categoryRepository).findById(firstRequest.getCategoryId()),
                () -> verify(securityUtil).getCurrentUserOrThrow(),
                () -> verify(lectureRepository).save(any()),
                () -> assertEquals(res.getCompletedStepName(), CreateLectureStep.FIRST.name())
        );
    }

    @Test
    @DisplayName("기존에 저장된 경우")
    void put_lecture_first_exist_lecture_test() throws Exception {

        // given
        Member director = createDirector();
        CreateLecture.FirstRequest firstRequest = createCreateLectureRequestFirst();
        List<MultipartFile> files = getMockMultipartFileList(1);
        Category category = CategoryDummy.createCategory(firstRequest.getCategoryId());
        stubbingFindCategory(firstRequest.getCategoryId(), category);
        Zone zone = ZoneDummy.createZone(firstRequest.getZoneId());
        stubbingFindZone(firstRequest.getZoneId(), zone);
        stubbingCurrentUsername(securityUtil, director);
        Zone previousZone = ZoneDummy.createZone(1L);
        Category previousCategory = CategoryDummy.createCategory(1L);
        Lecture lecture = LectureDummy.createLectureWithZoneCategory(director, previousZone, previousCategory);
        stubbingFindWithCategoryZoneByDirectorUsernameIsExist(director, lecture);
        List<FileInfoVo> fileInfoList = getFileInfo(1);
        stubbingDeleteSaveFiles(PATH, lecture.getThumbnailImageFileNames(), files, fileInfoList);

        // when
        CreateLectureProcessResponse res = putFirstService.putLectureProcess(firstRequest, files);

        // then
        assertAll(
                () -> verify(lectureQueryRepository).findWithCategoryZoneByDirectorUsername(director.getUsername()),
                () -> verify(saveDeleteFileManager).deleteAndSaveFiles(PATH, List.of(lecture.getThumbnailImageFileName()), files),
                () -> verify(zoneRepository).findById(firstRequest.getZoneId()),
                () -> verify(categoryRepository).findById(firstRequest.getCategoryId()),
                () -> verify(lectureRepository, times(0)).save(any()),
                () -> assertEquals(res.getCompletedStepName(), CreateLectureStep.FIRST.name()),
                () -> assertEquals(lecture.getZone().getId(), firstRequest.getZoneId()),
                () -> assertEquals(lecture.getCategory().getId(), firstRequest.getCategoryId())
        );
    }

    private void stubbingSaveLecture(Lecture lecture) {
        when(lectureRepository.save(lecture)).thenReturn(lecture);
    }

    private void stubbingFindZone(Long zoneId, Zone zone) {
        when(zoneRepository.findById(zoneId)).thenReturn(Optional.of(zone));
    }

    private void stubbingFindCategory(Long categoryId, Category category) {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    }

    private void stubbingFindWithCategoryZoneByDirectorUsernameIsExist(Member director, Lecture lecture) {
        when(lectureQueryRepository.findWithCategoryZoneByDirectorUsername(director.getUsername())).thenReturn(Optional.of(lecture));
    }

    private void stubbingGetCurrentMember(Member director) {
        when(securityUtil.getCurrentUserOrThrow()).thenReturn(director);
    }

    private List<FileInfoVo> getFileInfo(int count) {
        List<FileInfoVo> fileInfoList = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            fileInfoList.add(FileInfoVo.builder().fileName("name").fileUrl("url").build());
        }
        return fileInfoList;
    }

    private void stubbingDeleteSaveFiles(FilePaths path, List<String> fileNames, List<MultipartFile> files, List<FileInfoVo> fileInfos) throws IOException {
        when(saveDeleteFileManager.deleteAndSaveFiles(path, fileNames, files)).thenReturn(fileInfos);
    }

    private void stubbingFindWithCategoryZoneByDirectorUsername(Member director) {
        when(lectureQueryRepository.findWithCategoryZoneByDirectorUsername(director.getUsername()))
                .thenReturn(Optional.empty());
    }

    private void stubbingCurrentUsername(SecurityUtil securityUtil, Member director) {
        when(securityUtil.getCurrentUsername())
                .thenReturn(Optional.of(director.getUsername()));
    }

    private CreateLecture.FirstRequest createCreateLectureRequestFirst() {
        return CreateLecture.FirstRequest.builder()
                .title("안녕하세요!안녕하세요!안녕하세요!안녕하세요!")
                .address("서울시 광진구").categoryId(1278L).zoneId(4411L)
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

    private Member createDirector() {
        return Member.builder()
                .id(1L).authority(Authority.ROLE_DIRECTOR).username("director")
                .build();
    }
}