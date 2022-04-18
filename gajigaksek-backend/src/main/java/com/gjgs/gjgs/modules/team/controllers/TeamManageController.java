package com.gjgs.gjgs.modules.team.controllers;

import com.gjgs.gjgs.modules.team.dtos.DelegateLeaderResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamAppliersResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamExitResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamManageResponse;
import com.gjgs.gjgs.modules.team.services.manage.TeamManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}")
@PreAuthorize("hasAnyRole('USER,DIRECTOR')")
public class  TeamManageController {

    private final TeamManageService teamManageService;

    @PostMapping("/appliers")
    public ResponseEntity<Void> applyTeam(@PathVariable("teamId") Long teamId) {

        teamManageService.applyTeam(teamId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/appliers")
    public ResponseEntity<TeamAppliersResponse> getTeamAppliers(@PathVariable("teamId") Long teamId) {

        return new ResponseEntity<>(teamManageService.getTeamAppliers(teamId), HttpStatus.OK);
    }

    @PostMapping("/appliers/{applierId}")
    public ResponseEntity<TeamManageResponse> acceptMember(@PathVariable("teamId") Long teamId,
                                                           @PathVariable("applierId") Long applierId) {

        return new ResponseEntity<>(teamManageService.acceptApplier(teamId, applierId), HttpStatus.OK);
    }

    @DeleteMapping("/appliers/{applierId}")
    public ResponseEntity<TeamManageResponse> rejectMember(@PathVariable("teamId") Long teamId,
                                                           @PathVariable("applierId") Long applierId) {

        return new ResponseEntity<>(teamManageService.rejectApplier(teamId, applierId), HttpStatus.OK);
    }

    @DeleteMapping("/members")
    public ResponseEntity<TeamExitResponse> exitMember(@PathVariable("teamId") Long teamId) {

        return new ResponseEntity<>(teamManageService.exitMember(teamId), HttpStatus.OK);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<TeamExitResponse> excludeMember(@PathVariable("teamId") Long teamId,
                                                          @PathVariable("memberId") Long memberId) {

        return new ResponseEntity<>(teamManageService.excludeMember(teamId, memberId), HttpStatus.OK);
    }

    @PatchMapping("/members/{memberId}")
    public ResponseEntity<DelegateLeaderResponse> changeLeader(@PathVariable("teamId") Long teamId,
                                          @PathVariable("memberId") Long memberId) {

        return new ResponseEntity<>(teamManageService.changeLeader(teamId, memberId), HttpStatus.OK);
    }
}
