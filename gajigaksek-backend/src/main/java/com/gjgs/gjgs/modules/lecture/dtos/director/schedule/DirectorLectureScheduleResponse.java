package com.gjgs.gjgs.modules.lecture.dtos.director.schedule;

import com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE)
@Builder
public class DirectorLectureScheduleResponse {

    private Long lectureId;
    private Long scheduleId;
    private String title;
    private int currentParticipants;
    private int maxParticipants;
    private LocalDate scheduleDate;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int regularPrice;
    private int priceOne;
    private int priceTwo;
    private int priceThree;
    private int priceFour;
    private String scheduleStatus;

    @QueryProjection
    public DirectorLectureScheduleResponse(Long lectureId, Long scheduleId, String title, int currentParticipants, int maxParticipants, LocalDate scheduleDate, LocalTime startTime, LocalTime endTime, int regularPrice, int priceOne, int priceTwo, int priceThree, int priceFour, ScheduleStatus scheduleStatus) {
        this.lectureId = lectureId;
        this.scheduleId = scheduleId;
        this.title = title;
        this.currentParticipants = currentParticipants;
        this.maxParticipants = maxParticipants;
        this.scheduleDate = scheduleDate;
        this.startHour = startTime.getHour();
        this.startMinute = startTime.getMinute();
        this.endHour = endTime.getHour();
        this.endMinute = endTime.getMinute();
        this.regularPrice = regularPrice;
        this.priceOne = priceOne;
        this.priceTwo = priceTwo;
        this.priceThree = priceThree;
        this.priceFour = priceFour;
        this.scheduleStatus = scheduleStatus.name();
    }
}
