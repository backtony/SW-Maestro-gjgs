package com.gjgs.gjgs.modules.coupon.controllers;

import com.gjgs.gjgs.modules.coupon.dto.EnableMemberCouponResponse;
import com.gjgs.gjgs.modules.coupon.services.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures/{lectureId}")
@PreAuthorize("hasAnyRole('USER,DIRECTOR')")
public class MemberCouponController {

    private final MemberCouponService memberCouponService;

    @PostMapping("/coupon")
    public ResponseEntity<Void> getLectureCoupon(@PathVariable Long lectureId) {
        memberCouponService.giveMemberCoupon(lectureId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/coupon")
    public ResponseEntity<EnableMemberCouponResponse> getMembersCoupon(@PathVariable Long lectureId) {
        return ResponseEntity.ok(memberCouponService.getMemberCoupon(lectureId));
    }
}
