package com.gjgs.gjgs.modules.member.dto.mypage;

import com.gjgs.gjgs.modules.payment.enums.OrderStatus;
import com.gjgs.gjgs.modules.utils.document.EnumType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.gjgs.gjgs.modules.member.dto.mypage.MyLectureResponse.CurrentLectureStatus.*;
import static java.time.LocalDateTime.now;
import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class MyLectureResponse {

    private Long orderId;
    private Long lectureId;
    private String lectureThumbnailUrl;
    private String lectureTitle;
    private Long scheduleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long teamId;
    private String orderStatus;
    private boolean leader;
    private CurrentLectureStatus currentLectureStatus;
    private boolean reviewed;

    @QueryProjection
    public MyLectureResponse(Long orderId, Long lectureId, String lectureThumbnailUrl, String lectureTitle, Long scheduleId, LocalDate lectureDate, LocalTime startTime, LocalTime endTime, Long teamId, OrderStatus orderStatus, boolean leader/*, boolean reviewed*/) {
        LocalDateTime startDateTime = LocalDateTime.of(lectureDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(lectureDate, endTime);

        this.orderId = orderId;
        this.lectureId = lectureId;
        this.lectureThumbnailUrl = lectureThumbnailUrl;
        this.lectureTitle = lectureTitle;
        this.scheduleId = scheduleId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.teamId = teamId;
        this.orderStatus = orderStatus.name();
        this.leader = leader;
        this.currentLectureStatus = checkLectureStatus(startDateTime, endDateTime);
    }

    private CurrentLectureStatus checkLectureStatus(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDateTime now = now();
        if (now.isBefore(startDateTime)) {
            return BEFORE;
        } else if (now.isAfter(startDateTime) && now.isBefore(endDateTime)) {
            return PROCEEDING;
        }
        return DONE;
    }

    @Getter @AllArgsConstructor(access = PRIVATE)
    public enum CurrentLectureStatus implements EnumType {

        BEFORE("시작 전"),
        PROCEEDING("클래스 진행 중"),
        DONE("진행 완료")
        ;

        private String description;

        @Override
        public String getName() {
            return this.name();
        }
    }
}
