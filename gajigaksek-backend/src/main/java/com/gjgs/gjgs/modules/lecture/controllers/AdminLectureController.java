package com.gjgs.gjgs.modules.lecture.controllers;

import com.gjgs.gjgs.modules.lecture.dtos.admin.ConfirmLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.admin.DecideLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.admin.RejectReason;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import com.gjgs.gjgs.modules.lecture.services.admin.AdminLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping("/api/v1/admin/lectures")
@Valid
public class AdminLectureController {

    private final AdminLectureService adminLectureService;

    @GetMapping("")
    public ResponseEntity<Page<ConfirmLectureResponse>> getConfirmLectures(Pageable pageable) {
        return ResponseEntity.ok(adminLectureService.getConfirmLectures(pageable));
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<PutLectureResponse> getConfirmLecture(@PathVariable Long lectureId) {
        return ResponseEntity.ok(adminLectureService.getConfirmLecture(lectureId));
    }

    @PostMapping("/{lectureId}/{decideType}")
    public ResponseEntity<Void> acceptOrRejectLecture(@PathVariable Long lectureId,
                                                      @PathVariable DecideLectureType decideType,
                                                      @RequestBody(required = false) @Validated RejectReason rejectReason) {
        adminLectureService.decideLecture(lectureId, decideType, rejectReason);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
