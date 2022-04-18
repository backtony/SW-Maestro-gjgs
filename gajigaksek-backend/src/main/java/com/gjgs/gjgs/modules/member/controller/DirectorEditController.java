package com.gjgs.gjgs.modules.member.controller;


import com.gjgs.gjgs.modules.member.dto.myinfo.DirectorTextModifyRequest;
import com.gjgs.gjgs.modules.member.dto.mypage.DirectorMyPageResponse;
import com.gjgs.gjgs.modules.member.service.mypage.interfaces.DirectorEditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN,DIRECTOR')")
public class DirectorEditController {

    private final DirectorEditService directorEditService;

    @GetMapping("/directors/edit")
    public ResponseEntity<DirectorMyPageResponse> editDirector() {
        return ResponseEntity.ok(directorEditService.getDirectorEditPage());
    }

    @PatchMapping("/directors/director-text")
    public ResponseEntity<Void> editDirectorText(@RequestBody @Valid DirectorTextModifyRequest directorTextModifyRequest) {
        directorEditService.editDirectorText(directorTextModifyRequest);
        return ResponseEntity.ok().build();
    }
}
