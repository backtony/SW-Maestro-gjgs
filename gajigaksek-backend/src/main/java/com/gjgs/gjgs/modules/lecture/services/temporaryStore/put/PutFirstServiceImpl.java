package com.gjgs.gjgs.modules.lecture.services.temporaryStore.put;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.category.repositories.CategoryRepository;
import com.gjgs.gjgs.modules.exception.category.CategoryNotFoundException;
import com.gjgs.gjgs.modules.exception.lecture.ThumbnailIsNotOneException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.exception.zone.ZoneNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureProcessResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.s3.FilePaths;
import com.gjgs.gjgs.modules.utils.s3.interfaces.SaveDeleteFileManager;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PutFirstServiceImpl extends AbstractFileCheck implements PutLectureService {

    private final FilePaths PATH = FilePaths.LECTURE_IMAGE_PATH;

    private final LectureRepository lectureRepository;
    private final SecurityUtil securityUtil;
    private final LectureQueryRepository lectureQueryRepository;
    private final SaveDeleteFileManager saveDeleteFileManager;
    private final ZoneRepository zoneRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public CreateLectureStep getCreateLectureStep() {
        return CreateLectureStep.FIRST;
    }

    @Override
    public CreateLectureProcessResponse putLectureProcess(CreateLecture request, List<MultipartFile> files) throws IOException {
        super.fileCheck(files, 1);
        String directorUsername = securityUtil.getCurrentUsername().orElseThrow(() -> new MemberNotFoundException());
        Optional<Lecture> creatingLecture = lectureQueryRepository.findWithCategoryZoneByDirectorUsername(directorUsername);

        if (creatingLecture.isEmpty()) {
            return saveLecture((CreateLecture.FirstRequest) request, files);
        }
        return putLecture(creatingLecture.get(),(CreateLecture.FirstRequest) request, files);
    }

    @Override
    protected void checkFileSize(List<MultipartFile> files, int targetSize) {
        if (files.size() != targetSize) {
            throw new ThumbnailIsNotOneException();
        }
    }

    private CreateLectureProcessResponse saveLecture(CreateLecture.FirstRequest request, List<MultipartFile> files) throws IOException {
        List<FileInfoVo> fileInfo = saveDeleteFileManager.deleteAndSaveFiles(PATH, Collections.emptyList(), files);
        Category category = getCategory(request.getCategoryId());
        Zone zone = getZone(request.getZoneId());
        Member director = securityUtil.getCurrentUserOrThrow();
        Lecture saved = lectureRepository.save(Lecture.of(fileInfo, category, zone, request, director));
        return CreateLectureProcessResponse.completeSaveFirst(saved.getId());
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException());
    }

    private Zone getZone(Long zoneId) {
        return zoneRepository.findById(zoneId).orElseThrow(() -> new ZoneNotFoundException());
    }

    private CreateLectureProcessResponse putLecture(Lecture lecture, CreateLecture.FirstRequest request, List<MultipartFile> files) throws IOException {
        List<FileInfoVo> saveFiles = saveDeleteFileManager.deleteAndSaveFiles(PATH, List.of(lecture.getThumbnailImageFileName()), files);
        isDifferentZone(lecture, request.getZoneId());
        isDifferentCategory(lecture, request.getCategoryId());
        lecture.putFirst(request, saveFiles);
        return CreateLectureProcessResponse.completeSaveFirst(lecture.getId());
    }

    private void isDifferentZone(Lecture lecture, Long zoneId) {
        if (lecture.isDifferentZone(zoneId)) {
            lecture.setZone(getZone(zoneId));
        }
    }

    private void isDifferentCategory(Lecture lecture, Long categoryId) {
        if (lecture.isDifferentCategory(categoryId)) {
            lecture.setCategory(getCategory(categoryId));
        }
    }
}
