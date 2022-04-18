package com.gjgs.gjgs.modules.bulletin.controller;

import com.gjgs.gjgs.modules.bulletin.dto.BulletinChangeRecruitResponse;
import com.gjgs.gjgs.modules.bulletin.dto.BulletinDetailResponse;
import com.gjgs.gjgs.modules.bulletin.dto.BulletinIdResponse;
import com.gjgs.gjgs.modules.bulletin.dto.CreateBulletinRequest;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchCondition;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.bulletin.services.BulletinService;
import com.gjgs.gjgs.modules.bulletin.validators.AgeValidator;
import com.gjgs.gjgs.modules.exception.validate.ValidatedException;
import com.gjgs.gjgs.modules.utils.validators.dayTimeAge.CreateRequestTimeDayAgeValidator;
import com.gjgs.gjgs.modules.utils.validators.search.CheckKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bulletins")
@RequiredArgsConstructor
@Validated
public class BulletinController {

    private final BulletinService bulletinService;
    private final CreateRequestTimeDayAgeValidator createRequestTimeDayAgeValidator;
    private final AgeValidator ageValidator;

    @InitBinder("createBulletinRequest")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(createRequestTimeDayAgeValidator, ageValidator);
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @PostMapping("")
    public ResponseEntity<BulletinIdResponse> createBulletin(@RequestBody @Validated CreateBulletinRequest createBulletinRequest,
                                                             BindingResult errors) {

        if (errors.hasErrors()) {
            throw new ValidatedException(errors);
        }

        return new ResponseEntity<>(bulletinService.createBulletin(createBulletinRequest), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<BulletinSearchResponse>> getBulletins(Pageable pageable,
                                                                     @CheckKeyword BulletinSearchCondition condition) {
        return new ResponseEntity<>(bulletinService.getBulletins(pageable, condition), HttpStatus.OK);
    }

    @GetMapping("/{bulletinId}")
    public ResponseEntity<BulletinDetailResponse> getBulletin(@PathVariable("bulletinId") Long bulletinId) {
        return new ResponseEntity<>(bulletinService.getBulletinDetails(bulletinId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @DeleteMapping("/{bulletinId}")
    public ResponseEntity<BulletinIdResponse> deleteBulletin(@PathVariable("bulletinId") Long bulletinId) {
        return new ResponseEntity<>(bulletinService.deleteBulletin(bulletinId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @PatchMapping("/{bulletinId}")
    public ResponseEntity<BulletinIdResponse> modifyBulletin(@PathVariable("bulletinId") Long bulletinId,
                                                             @RequestBody @Validated CreateBulletinRequest request,
                                                             BindingResult errors) {
        if (errors.hasErrors()) {
            throw new ValidatedException(errors);
        }

        return new ResponseEntity<>(bulletinService.modifyBulletin(bulletinId, request), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @PatchMapping("/{bulletinId}/recruit")
    public ResponseEntity<BulletinChangeRecruitResponse> changeRecruitStatus(@PathVariable("bulletinId") Long bulletinId) {
        return new ResponseEntity<>(bulletinService.changeRecruitStatus(bulletinId), HttpStatus.OK);
    }
}
