package com.gjgs.gjgs.modules.lecture.services.temporaryStore.manage;

import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.TemporaryStorageLectureManageResponse;

public interface TemporaryStorageLectureManageService {

    TemporaryStorageLectureManageResponse saveLecture(CreateLecture.TermsRequest request);

    TemporaryStorageLectureManageResponse deleteTemporaryStorageLecture();

    PutLectureResponse getTemporaryStoredLecture();
}
