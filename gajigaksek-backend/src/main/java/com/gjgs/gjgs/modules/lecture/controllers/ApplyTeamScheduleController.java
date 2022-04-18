package com.gjgs.gjgs.modules.lecture.controllers;

import com.gjgs.gjgs.modules.lecture.dtos.apply.ApplyLectureTeamRequest;
import com.gjgs.gjgs.modules.lecture.dtos.apply.ApplyLectureTeamResponse;
import com.gjgs.gjgs.modules.lecture.services.apply.ApplyScheduleTeamService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/schedules/{scheduleId}")
@PreAuthorize("hasAnyRole('USER,DIRECTOR')")
public class ApplyTeamScheduleController {

    private final ApplyScheduleTeamService applyScheduleTeamService;

    @PostMapping("")
    public ResponseEntity<ApplyLectureTeamResponse> applyLecture(@PathVariable Long scheduleId,
                                                                 @RequestBody @Validated ApplyLectureTeamRequest request) {

        return new ResponseEntity<>(applyScheduleTeamService.apply(scheduleId, request), HttpStatus.OK);
    }

    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity<Void> cancelApplyLecture(@PathVariable Long scheduleId,
                                                    @PathVariable Long teamId) throws IamportResponseException, IOException {

        applyScheduleTeamService.cancel(scheduleId, teamId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
