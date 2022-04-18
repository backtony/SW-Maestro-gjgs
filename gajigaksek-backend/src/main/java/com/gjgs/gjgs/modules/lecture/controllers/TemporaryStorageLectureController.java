package com.gjgs.gjgs.modules.lecture.controllers;

import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureProcessResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.TemporaryStorageLectureManageResponse;
import com.gjgs.gjgs.modules.lecture.services.temporaryStore.manage.TemporaryStorageLectureManageService;
import com.gjgs.gjgs.modules.lecture.services.temporaryStore.put.PutLectureServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/director/lectures")
@PreAuthorize("hasAnyRole('DIRECTOR')")
public class TemporaryStorageLectureController {

    private final PutLectureServiceFactory putLectureServiceFactory;
    private final TemporaryStorageLectureManageService temporaryStorageLectureManageService;

    @PutMapping(value = "", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<CreateLectureProcessResponse> putLecture(@RequestPart("request") @Validated CreateLecture request,
                                                                   @RequestPart(value = "files", required = false) List<MultipartFile> files) throws Exception {

        return new ResponseEntity<>(putLectureServiceFactory.getService(request.getCreateLectureStep())
                .putLectureProcess(request, files), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<TemporaryStorageLectureManageResponse> saveLecture(@RequestBody @Validated CreateLecture.TermsRequest request) {

        return new ResponseEntity<>(temporaryStorageLectureManageService.saveLecture(request), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<PutLectureResponse> getTemporaryStorageLecture() {

        return new ResponseEntity<>(temporaryStorageLectureManageService.getTemporaryStoredLecture(), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<TemporaryStorageLectureManageResponse> deleteTemporaryStorageLecture() {

        return new ResponseEntity<>(temporaryStorageLectureManageService.deleteTemporaryStorageLecture(), HttpStatus.OK);
    }
}
