package com.gjgs.gjgs.modules.lecture.services.temporaryStore.put;

import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.coupon.repositories.CouponQueryRepository;
import com.gjgs.gjgs.modules.coupon.repositories.CouponRepository;
import com.gjgs.gjgs.modules.exception.lecture.TemporaryNotSaveLectureException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureProcessResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PutPriceCouponServiceImpl implements PutLectureService {

    private final SecurityUtil securityUtil;
    private final LectureRepository lectureRepository;
    private final CouponQueryRepository couponQueryRepository;
    private final CouponRepository couponRepository;

    @Override
    public CreateLectureStep getCreateLectureStep() {
        return CreateLectureStep.PRICE_COUPON;
    }

    @Override
    public CreateLectureProcessResponse putLectureProcess(CreateLecture request, List<MultipartFile> files) {
        String username = securityUtil.getCurrentUsername().orElseThrow(() -> new MemberNotFoundException());
        Lecture lecture = lectureRepository.findCreatingLectureByDirectorUsername(username).orElseThrow(() -> new TemporaryNotSaveLectureException());
        CreateLecture.PriceCouponRequest priceCouponRequest = (CreateLecture.PriceCouponRequest) request;
        lecture.putPrice(priceCouponRequest.getPrice());
        putCoupon(lecture, username, priceCouponRequest.getCoupon());
        return CreateLectureProcessResponse.completePriceCoupon(lecture.getId());
    }

    private void putCoupon(Lecture lecture, String username, CreateLecture.CouponDto couponRequest) {
        Optional<Coupon> coupon = couponQueryRepository.findByLectureIdDirectorUsername(lecture.getId(), username);

        if (coupon.isEmpty()) {
            couponRepository.save(Coupon.of(lecture, couponRequest));
        } else {
            coupon.get().update(couponRequest);
        }
    }
}
