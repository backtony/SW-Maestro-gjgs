package com.gjgs.gjgs.modules.payment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
public class TeamMemberPaymentResponse {

    private Long orderId;
    private int price;
    private Long scheduleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String lectureThumbnailUrl;
    private String lectureTitle;
    private Long teamId;
    private String teamName;
    private int haveReward;

    @QueryProjection
    public TeamMemberPaymentResponse(Long orderId, int price, Long scheduleId, LocalDate lectureDate, LocalTime startTime, LocalTime endTime, String lectureThumbnailUrl, String lectureTitle, Long teamId, String teamName, int haveReward) {
        this.orderId = orderId;
        this.price = price;
        this.scheduleId = scheduleId;
        this.startTime = LocalDateTime.of(lectureDate, startTime);
        this.endTime = LocalDateTime.of(lectureDate, endTime);
        this.lectureThumbnailUrl = lectureThumbnailUrl;
        this.lectureTitle = lectureTitle;
        this.teamId = teamId;
        this.teamName = teamName;
        this.haveReward = haveReward;
    }
}
