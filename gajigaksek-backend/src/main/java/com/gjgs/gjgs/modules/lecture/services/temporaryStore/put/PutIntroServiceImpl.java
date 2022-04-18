package com.gjgs.gjgs.modules.lecture.services.temporaryStore.put;

import com.gjgs.gjgs.modules.exception.lecture.ProductAndFileNotEqualException;
import com.gjgs.gjgs.modules.exception.lecture.TemporaryNotSaveLectureException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureProcessResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.utils.s3.FilePaths;
import com.gjgs.gjgs.modules.utils.s3.interfaces.SaveDeleteFileManager;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PutIntroServiceImpl extends AbstractFileCheck implements PutLectureService {

    private final FilePaths PATH = FilePaths.LECTURE_IMAGE_PATH;

    private final LectureQueryRepository lectureQueryRepository;
    private final SecurityUtil securityUtil;
    private final SaveDeleteFileManager saveDeleteFileManager;
    private final LectureJdbcRepository lectureJdbcRepository;

    @Override
    public CreateLectureStep getCreateLectureStep() {
        return CreateLectureStep.INTRO;
    }

    @Override
    public CreateLectureProcessResponse putLectureProcess(CreateLecture request, List<MultipartFile> files) throws IOException {
        CreateLecture.IntroRequest intro = (CreateLecture.IntroRequest)request;
        super.fileCheck(files, intro.getFinishedProductInfoList().size());

        String directorUsername = securityUtil.getCurrentUsername().orElseThrow(() -> new MemberNotFoundException());
        Lecture lecture = lectureQueryRepository.findWithFinishedProductsByDirectorUsername(directorUsername).orElseThrow(() -> new TemporaryNotSaveLectureException());
        List<FileInfoVo> saveFiles = saveDeleteFileManager.deleteAndSaveFiles(PATH, lecture.getFinishedProductsFileNames(), files);

        lecture.putIntro(intro, saveFiles);
        saveFinishedProducts(lecture);
        return CreateLectureProcessResponse.completeIntro(lecture.getId());
    }

    private void saveFinishedProducts(Lecture lecture) {
        if (!lecture.getFinishedProductList().isEmpty()) {
            lectureQueryRepository.deleteFinishedProductsByLectureId(lecture.getId());
        }
        lectureJdbcRepository.insertFinishedProduct(lecture);
    }

    @Override
    protected void checkFileSize(List<MultipartFile> files, int targetSize) {
        if (files.size() != targetSize) {
            throw new ProductAndFileNotEqualException();
        }
    }

    private boolean lessThanOneGreaterThanFour(List<MultipartFile> files) {
        return files.size() < 1 || files.size() > 4;
    }
}
