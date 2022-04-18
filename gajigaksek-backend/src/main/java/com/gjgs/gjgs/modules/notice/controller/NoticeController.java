package com.gjgs.gjgs.modules.notice.controller;

import com.gjgs.gjgs.modules.notice.dto.NoticeForm;
import com.gjgs.gjgs.modules.notice.dto.NoticeResponse;
import com.gjgs.gjgs.modules.notice.service.interfaces.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ResponseEntity<Page<NoticeResponse>> getNotice(
                                                     @PageableDefault(size = 4,sort = "createdDate",direction = Sort.Direction.DESC)
                                                             Pageable pageable,
                                                     @RequestParam("type") String noticeType) {
        return ResponseEntity.ok(noticeService.getNotice(noticeType,pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createNotice(@RequestBody @Validated NoticeForm noticeForm) {
        noticeService.createNotice(noticeForm);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{noticeId}")
    public ResponseEntity<Void> updateNotice(@RequestBody @Validated NoticeForm noticeForm,
                                                        @PathVariable Long noticeId) {
        noticeService.updateNotice(noticeForm,noticeId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getOneNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getOneNotice(noticeId));
    }
}
