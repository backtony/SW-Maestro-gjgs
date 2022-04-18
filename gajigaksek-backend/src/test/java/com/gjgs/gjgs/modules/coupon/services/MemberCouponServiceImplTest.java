package com.gjgs.gjgs.modules.coupon.services;

import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.coupon.repositories.CouponQueryRepository;
import com.gjgs.gjgs.modules.coupon.repositories.MemberCouponQueryRepository;
import com.gjgs.gjgs.modules.coupon.validators.CouponValidator;
import com.gjgs.gjgs.modules.dummy.CouponDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberQueryRepository;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberCouponServiceImplTest {

    @Mock CouponQueryRepository couponQueryRepository;
    @Mock SecurityUtil securityUtil;
    @Mock MemberQueryRepository memberQueryRepository;
    @Mock MemberCouponQueryRepository memberCouponQueryRepository;
    @Mock CouponValidator couponValidator;
    @InjectMocks MemberCouponServiceImpl memberCouponService;

    @Test
    @DisplayName("쿠폰 발급받기")
    void publish_coupon_test() throws Exception {

        // given
        Member director = MemberDummy.createTestDirectorMember();
        Lecture lecture = LectureDummy.createLectureWithCoupon(director);
        Coupon coupon = CouponDummy.createCoupon(lecture);
        stubbingFindCoupon(lecture, coupon);
        Member couponReceiver = MemberDummy.createTestUniqueMember(1);
        stubbingGetUsername(couponReceiver);
        stubbingFindMemberWithCoupon(couponReceiver);

        // when
        memberCouponService.giveMemberCoupon(lecture.getId());

        // then
        assertAll(
                () -> verify(couponQueryRepository).findByLectureId(lecture.getId()),
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(couponValidator).validateDuplicate(couponReceiver, coupon),
                () -> verify(memberQueryRepository).findWithCouponByUsername(couponReceiver.getUsername()),
                () -> assertEquals(couponReceiver.getCoupons().size(), 1),
                () -> assertFalse(coupon.isAvailable()),
                () -> assertEquals(coupon.getRemainCount(), 0)
        );
    }

    @Test
    @DisplayName("쿠폰 아이디가 있을 경우 멤버와 한번에 가져오기")
    void get_member_or_with_coupon_test() throws Exception {

        // given
        MemberCoupon memberCoupon = MemberCoupon.builder().id(1L).serialNumber("test").build();
        Member paymentMember = Member.builder().id(1L).username("test")
                .coupons(List.of(memberCoupon)).build();
        Lecture lecture = Lecture.builder().id(1L).build();
        Coupon coupon = Coupon.builder()
                .serialNumber("test").issueDate(LocalDateTime.now()).closeDate(LocalDateTime.now().plusDays(30))
                .available(true).build();
        PaymentRequest request = PaymentRequest.builder().memberCouponId(memberCoupon.getId()).lectureId(lecture.getId()).build();
        stubbingGetUsername(paymentMember);
        stubbingFindWithCouponLectureByMemberCouponId(memberCoupon, paymentMember, lecture, coupon);

        // when
        memberCouponService.getMemberOrWithCoupon(request);

        // then
        assertAll(
                () -> verify(memberCouponQueryRepository).findByMemberCouponByIdUsername(memberCoupon.getId(),
                        paymentMember.getUsername()),
                () -> verify(couponQueryRepository).findByLectureId(lecture.getId()),
                () -> verify(couponValidator).validateAvailableMemberCoupon(memberCoupon, coupon)
        );
    }

    @Test
    @DisplayName("쿠폰 아이디가 없을 경우 멤버만 가져오기")
    void get_member_or_with_coupon_null_member_coupon_id_test() throws Exception {

        // given
        PaymentRequest request = PaymentRequest.builder().build();
        stubbingFindMember();

        // when
        memberCouponService.getMemberOrWithCoupon(request);

        // then
        verify(securityUtil).getCurrentUserOrThrow();;
    }

    private void stubbingFindMember() {
        when(securityUtil.getCurrentUserOrThrow()).thenReturn(Member.builder().build());
    }

    private void stubbingFindWithCouponLectureByMemberCouponId(MemberCoupon memberCoupon, Member member, Lecture lecture, Coupon coupon) {
        when(memberCouponQueryRepository
                .findByMemberCouponByIdUsername(memberCoupon.getId(), member.getUsername()))
                .thenReturn(Optional.of(member));
        when(couponQueryRepository.findByLectureId(any())).thenReturn(Optional.of(coupon));
    }

    private void stubbingFindMemberWithCoupon(Member couponReceiver) {
        when(memberQueryRepository.findWithCouponByUsername(couponReceiver.getUsername()))
                .thenReturn(Optional.of(couponReceiver));
    }

    private void stubbingFindCoupon(Lecture lecture, Coupon coupon) {
        when(couponQueryRepository.findByLectureId(lecture.getId()))
                .thenReturn(Optional.of(coupon));
    }

    private void stubbingGetUsername(Member couponReceiver) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(couponReceiver.getUsername()));
    }
}
