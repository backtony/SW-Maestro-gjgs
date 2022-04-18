package com.gjgs.gjgs.modules.lecture.services.temporaryStore.put;

import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.coupon.repositories.CouponQueryRepository;
import com.gjgs.gjgs.modules.coupon.repositories.CouponRepository;
import com.gjgs.gjgs.modules.dummy.CouponDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureProcessResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PutPriceCouponServiceImplTest {

    @Mock SecurityUtil securityUtil;
    @Mock LectureRepository lectureRepository;
    @Mock CouponQueryRepository couponQueryRepository;
    @Mock CouponRepository couponRepository;
    @InjectMocks PutPriceCouponServiceImpl putPriceCouponService;

    @Test
    @DisplayName("가격, 쿠폰 저장 / 쿠폰이 존재하지 않는 경우")
    void put_lecture_price_coupon_test() throws Exception {

        // given
        Member director = createDirector();
        stubbingCurrentUsername(securityUtil, director);
        CreateLecture.PriceCouponRequest request = createPriceCouponRequest();
        Lecture lecture = LectureDummy.createHaveTwoScheduleThreeCurriculumFourFinishedProduct(1, director);
        stubbingGetLecture(director, lecture);
        stubbingEmptyCoupon(director, lecture);

        // when
        CreateLectureProcessResponse res = putPriceCouponService.putLectureProcess(request, getMockMultipartFileList(1));

        // then
        assertAll(
                () -> verify(lectureRepository).findCreatingLectureByDirectorUsername(director.getUsername()),
                () -> verify(couponQueryRepository).findByLectureIdDirectorUsername(lecture.getId(), director.getUsername()),
                () -> verify(couponRepository).save(any()),
                () -> assertEquals(res.getCompletedStepName(), CreateLectureStep.PRICE_COUPON.name())
        );
    }

    @Test
    @DisplayName("가격, 쿠폰 저장 / 저장된 쿠폰이 이미 있는 경우")
    void put_lecture_price_should_not_exist_coupon_test() throws Exception {

        // given
        Member director = createDirector();
        stubbingCurrentUsername(securityUtil, director);
        CreateLecture.PriceCouponRequest request = createPriceCouponRequest();
        Lecture lecture = LectureDummy.createHaveTwoScheduleThreeCurriculumFourFinishedProduct(1, director);
        stubbingGetLecture(director, lecture);
        Coupon coupon = CouponDummy.createCoupon(lecture);
        stubbingGetCoupon(director, lecture, coupon);

        // when
        CreateLectureProcessResponse res = putPriceCouponService.putLectureProcess(request, getMockMultipartFileList(1));

        // then
        assertAll(
                () -> verify(lectureRepository).findCreatingLectureByDirectorUsername(director.getUsername()),
                () -> verify(couponQueryRepository).findByLectureIdDirectorUsername(lecture.getId(), director.getUsername()),
                () -> assertEquals(coupon.getDiscountPrice(), request.getCoupon().getCouponPrice()),
                () -> assertEquals(res.getCompletedStepName(), CreateLectureStep.PRICE_COUPON.name())
        );
    }

    private void stubbingEmptyCoupon(Member director, Lecture lecture) {
        when(couponQueryRepository.findByLectureIdDirectorUsername(lecture.getId(), director.getUsername())).thenReturn(Optional.empty());
    }

    private void stubbingGetCoupon(Member director, Lecture lecture, Coupon coupon) {
        when(couponQueryRepository.findByLectureIdDirectorUsername(lecture.getId(), director.getUsername())).thenReturn(Optional.of(coupon));
    }

    private CreateLecture.PriceCouponRequest createPriceCouponRequest() {
        return CreateLecture.PriceCouponRequest.builder().createLectureStep(CreateLectureStep.PRICE_COUPON)
                .price(CreateLecture.PriceDto.builder()
                        .regularPrice(50000).priceOne(50000).priceTwo(50000).priceThree(50000).priceFour(50000)
                        .build())
                .coupon(CreateLecture.CouponDto.builder()
                        .couponPrice(1000).couponCount(20)
                        .build())
                .build();
    }

    private void stubbingGetLecture(Member director, Lecture lecture) {
        when(lectureRepository.findCreatingLectureByDirectorUsername(director.getUsername())).thenReturn(Optional.of(lecture));
    }

    private void stubbingCurrentUsername(SecurityUtil securityUtil, Member director) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(director.getUsername()));
    }

    private Member createDirector() {
        return Member.builder()
                .id(1L).authority(Authority.ROLE_DIRECTOR).username("director")
                .build();
    }

    private List<MultipartFile> getMockMultipartFileList(int count) {
        List<MultipartFile> files = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            files.add(getMockMultipartFile());
        }
        return files;
    }

    private MultipartFile getMockMultipartFile() {
        return new MockMultipartFile("files",
                "image.img",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "image".getBytes());
    }
}