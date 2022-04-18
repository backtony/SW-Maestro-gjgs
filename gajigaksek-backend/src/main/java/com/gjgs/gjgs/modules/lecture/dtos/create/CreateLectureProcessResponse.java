package com.gjgs.gjgs.modules.lecture.dtos.create;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreateLectureProcessResponse {

    private Long lectureId;
    private String completedStepName;

    public static CreateLectureProcessResponse completeSaveFirst(Long lectureId) {
        return CreateLectureProcessResponse.builder()
                .lectureId(lectureId)
                .completedStepName(CreateLectureStep.FIRST.name())
                .build();
    }

    public static CreateLectureProcessResponse completeIntro(Long lectureId) {
        return CreateLectureProcessResponse.builder()
                .lectureId(lectureId)
                .completedStepName(CreateLectureStep.INTRO.name())
                .build();
    }

    public static CreateLectureProcessResponse completeCurriculum(Long lectureId) {
        return CreateLectureProcessResponse.builder()
                .lectureId(lectureId)
                .completedStepName(CreateLectureStep.CURRICULUM.name())
                .build();
    }

    public static CreateLectureProcessResponse completeSchedule(Long lectureId) {
        return CreateLectureProcessResponse.builder()
                .lectureId(lectureId)
                .completedStepName(CreateLectureStep.SCHEDULE.name())
                .build();
    }

    public static CreateLectureProcessResponse completePriceCoupon(Long lectureId) {
        return CreateLectureProcessResponse.builder()
                .lectureId(lectureId)
                .completedStepName(CreateLectureStep.PRICE_COUPON.name())
                .build();
    }

    public static CreateLectureProcessResponse completeTerms(Long lectureId) {
        return CreateLectureProcessResponse.builder()
                .lectureId(lectureId)
                .completedStepName(CreateLectureStep.TERMS.name())
                .build();
    }
}
