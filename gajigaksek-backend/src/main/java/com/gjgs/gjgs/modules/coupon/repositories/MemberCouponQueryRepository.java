package com.gjgs.gjgs.modules.coupon.repositories;

import com.gjgs.gjgs.modules.coupon.dto.EnableMemberCouponResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.MyAvailableCouponResponse;
import com.gjgs.gjgs.modules.member.entity.Member;

import java.util.Optional;

public interface MemberCouponQueryRepository {

    Optional<Member> findByMemberCouponByIdUsername(Long memberCouponId, String username);

    EnableMemberCouponResponse findMyEnableCouponByLectureIdUsername(Long lectureId, String username);

    MyAvailableCouponResponse findMyAvailableCouponsByUsername(String username);
}
