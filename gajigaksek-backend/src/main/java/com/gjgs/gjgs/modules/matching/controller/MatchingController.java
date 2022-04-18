package com.gjgs.gjgs.modules.matching.controller;

import com.gjgs.gjgs.modules.exception.validate.ValidatedException;
import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.matching.dto.MatchingStatusResponse;
import com.gjgs.gjgs.modules.matching.service.interfaces.MatchingService;
import com.gjgs.gjgs.modules.matching.validator.MatchingRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MatchingController {

    private final MatchingService matchingService;
    private final MatchingRequestValidator matchingRequestValidator;


    @InitBinder("matchingRequest")
    public void initBinderNicknameForm(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(matchingRequestValidator);
    }

    @PostMapping
    public ResponseEntity<Void> matching(@RequestBody @Validated MatchingRequest matchingRequest, BindingResult errors) {

        if (errors.hasErrors()) {
            throw new ValidatedException(errors);
        }

        matchingService.matching(matchingRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelMatching() {
        matchingService.cancel();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MatchingStatusResponse> getMatchingStatus() {
        return ResponseEntity.ok(matchingService.status());
    }


}
