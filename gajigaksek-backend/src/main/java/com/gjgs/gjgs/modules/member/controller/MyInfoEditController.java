package com.gjgs.gjgs.modules.member.controller;

import com.gjgs.gjgs.modules.member.dto.myinfo.*;
import com.gjgs.gjgs.modules.member.service.mypage.interfaces.MyInfoEditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MyInfoEditController {

    private final MyInfoEditService myInfoEditService;


    @GetMapping("/edit")
    public ResponseEntity<MyPageEditResponse> editMyPage() {
        return ResponseEntity.ok(myInfoEditService.editMyPage());
    }


    @PatchMapping("/nickname")
    public ResponseEntity<Void> editNickname(@RequestBody @Valid NicknameModifyRequest nicknameModifyRequest) {
        myInfoEditService.editNickname(nicknameModifyRequest);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/phone")
    public ResponseEntity<Void> editPhone(@RequestBody @Valid PhoneModifyRequest phoneModifyRequest) {
        myInfoEditService.editPhone(phoneModifyRequest);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/category")
    public ResponseEntity<Void> editCategory(@RequestBody @Valid CategoryModifyRequest categoryModifyRequest) {
        myInfoEditService.editCategory(categoryModifyRequest.getCategoryIdList());
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/profile-text")
    public ResponseEntity<Void> editProfileText(@RequestBody @Valid ProfileTextModifyRequest profileTextModifyRequest) {
        myInfoEditService.editProfileText(profileTextModifyRequest);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/image")
    public ResponseEntity<Void> editImage(@RequestPart MultipartFile file) {
        myInfoEditService.editImage(file);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("zone")
    public ResponseEntity<Void> editZone(@RequestBody @Valid ZoneModifyRequest zoneModifyRequest) {
        myInfoEditService.editZone(zoneModifyRequest.getZoneId());
        return ResponseEntity.ok().build();
    }




    @PostMapping("/alarm")
    public ResponseEntity<Void> editMyEventAlarm(@RequestBody AlarmEditRequest alarmEditRequest){
        myInfoEditService.editMyEventAlarm(alarmEditRequest);
        return ResponseEntity.ok().build();
    }

}
