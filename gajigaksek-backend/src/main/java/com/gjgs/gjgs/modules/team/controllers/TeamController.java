package com.gjgs.gjgs.modules.team.controllers;

import com.gjgs.gjgs.modules.category.validators.CategoryValidator;
import com.gjgs.gjgs.modules.exception.validate.ValidatedException;
import com.gjgs.gjgs.modules.team.dtos.*;
import com.gjgs.gjgs.modules.team.services.crud.TeamCrudService;
import com.gjgs.gjgs.modules.utils.validators.dayTimeAge.CreateRequestTimeDayAgeValidator;
import com.gjgs.gjgs.modules.zone.validators.ZoneValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
@PreAuthorize("hasAnyRole('USER,DIRECTOR')")
public class TeamController {

    private final TeamCrudService teamCrudService;
    private final CreateRequestTimeDayAgeValidator createRequestTimeDayAgeValidator;
    private final CategoryValidator categoryValidator;
    private final ZoneValidator zoneValidator;

    @InitBinder("createTeamRequest")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(createRequestTimeDayAgeValidator, categoryValidator, zoneValidator);
    }

    @PostMapping("")
    public ResponseEntity<CreateTeamResponse> createTeam(@RequestBody @Validated CreateTeamRequest createTeamRequest,
                                                         BindingResult errors)  {

        if (errors.hasErrors()) {
            throw new ValidatedException(errors);
        }

        return new ResponseEntity<>(teamCrudService.createTeam(createTeamRequest), HttpStatus.OK);
    }

    @PatchMapping("/{teamId}")
    public ResponseEntity<ModifyTeamResponse> modifyTeam(@PathVariable("teamId") Long teamId,
                                                         @Validated @RequestBody CreateTeamRequest createTeamRequest,
                                                         BindingResult errors)  {

        if (errors.hasErrors()) {
            throw new ValidatedException(errors);
        }

        return new ResponseEntity<>(teamCrudService.modifyTeam(teamId, createTeamRequest), HttpStatus.OK);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDetailResponse> getTeamDetail(@PathVariable("teamId") Long teamId) {

        return new ResponseEntity<>(teamCrudService.getTeamDetail(teamId), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<MyTeamListResponse> getMyTeams() {

        return new ResponseEntity<>(teamCrudService.getMyTeamList(), HttpStatus.OK);
    }

    @GetMapping("/lead")
    public ResponseEntity<MyLeadTeamsResponse> getMyLeadTeams() {

        return new ResponseEntity<>(teamCrudService.getMyLeadTeamWithBulletinLecture(), HttpStatus.OK);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<TeamExitResponse> deleteTeam(@PathVariable("teamId") Long teamId) {

        return new ResponseEntity<>(teamCrudService.deleteTeam(teamId), HttpStatus.OK);
    }
}
