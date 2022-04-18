package com.gjgs.gjgs.modules.member.dto.mypage;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class CompleteLecturePaymentResponse {

    private Long lectureId;
    private String lectureThumbnailUrl;
    private String lectureTitle;
    private String lectureAddress;
    private Long scheduleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int paymentPrice;

    @QueryProjection
    public CompleteLecturePaymentResponse(Long lectureId, String lectureThumbnailUrl, String lectureTitle, String lectureAddress, Long scheduleId, LocalDate lectureDate, LocalTime startTime, LocalTime endTime, int paymentPrice) {
        this.lectureId = lectureId;
        this.lectureThumbnailUrl = lectureThumbnailUrl;
        this.lectureTitle = lectureTitle;
        this.lectureAddress = lectureAddress;
        this.scheduleId = scheduleId;
        this.startDateTime = LocalDateTime.of(lectureDate, startTime);
        this.endDateTime = LocalDateTime.of(lectureDate, endTime);
        this.paymentPrice = paymentPrice;
    }
}
