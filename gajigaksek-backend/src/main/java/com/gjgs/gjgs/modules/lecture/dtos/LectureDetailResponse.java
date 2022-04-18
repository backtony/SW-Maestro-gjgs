package com.gjgs.gjgs.modules.lecture.dtos;

import com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class LectureDetailResponse extends FavoriteResponse {

    private Long lectureId;
    private String thumbnailImageUrl;
    private String lectureTitle;
    private Long zoneId;
    private Long categoryId;
    private int progressTime;
    private int priceOne;
    private int priceTwo;
    private int priceThree;
    private int priceFour;
    private int regularPrice;
    private String mainText;
    private int minParticipants;
    private int maxParticipants;
    private DirectorResponse directorResponse;
    @Builder.Default
    private Set<CurriculumResponse> curriculumResponseList = new LinkedHashSet<>();
    @Builder.Default
    private Set<FinishedProductResponse> finishedProductResponseList = new LinkedHashSet<>();
    @Builder.Default
    private Set<ScheduleResponse> scheduleResponseList = new LinkedHashSet<>();

    @QueryProjection
    public LectureDetailResponse(Long lectureId, String thumbnailImageUrl, String lectureTitle, Long zoneId, Long categoryId, int progressTime, int priceOne, int priceTwo, int priceThree, int priceFour, int regularPrice, String mainText, int minParticipants, int maxParticipants, DirectorResponse directorResponse, Set<CurriculumResponse> curriculumResponseList, Set<FinishedProductResponse> finishedProductResponseList, Set<ScheduleResponse> scheduleResponseList) {
        this.lectureId = lectureId;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.lectureTitle = lectureTitle;
        this.zoneId = zoneId;
        this.categoryId = categoryId;
        this.progressTime = progressTime;
        this.priceOne = priceOne;
        this.priceTwo = priceTwo;
        this.priceThree = priceThree;
        this.priceFour = priceFour;
        this.regularPrice = regularPrice;
        this.mainText = mainText;
        this.directorResponse = directorResponse;
        this.curriculumResponseList = curriculumResponseList;
        this.finishedProductResponseList = finishedProductResponseList;
        this.scheduleResponseList = scheduleResponseList;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.myFavorite = false;
    }

    @Override
    public void changeMyFavoriteContents(List<Long> favoriteContentsIdList) {
        if (favoriteContentsIdList.contains(lectureId)) {
            myFavorite = true;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class DirectorResponse {

        private Long directorId;
        private String directorProfileText;
        private String directorProfileImageUrl;

        @QueryProjection
        public DirectorResponse(Long directorId, String directorProfileText, String directorProfileImageUrl) {
            this.directorId = directorId;
            this.directorProfileText = directorProfileText;
            this.directorProfileImageUrl = directorProfileImageUrl;
        }
    }

    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED) @Builder
    @EqualsAndHashCode(of = "curriculumId")
    public static class CurriculumResponse {
        private Long curriculumId;
        private int order;
        private String title;
        private String detailText;
        private String curriculumImageUrl;

        @QueryProjection
        public CurriculumResponse(Long curriculumId, int order, String title, String detailText, String curriculumImageUrl) {
            this.curriculumId = curriculumId;
            this.order = order;
            this.title = title;
            this.detailText = detailText;
            this.curriculumImageUrl = curriculumImageUrl;
        }
    }

    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED) @Builder
    @EqualsAndHashCode(of = "finishedProductId")
    public static class FinishedProductResponse {
        private Long finishedProductId;
        private int order;
        private String finishedProductImageUrl;
        private String text;

        @QueryProjection
        public FinishedProductResponse(Long finishedProductId, int order, String finishedProductImageUrl, String text) {
            this.finishedProductId = finishedProductId;
            this.order = order;
            this.finishedProductImageUrl = finishedProductImageUrl;
            this.text = text;
        }
    }

    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @Builder
    @EqualsAndHashCode(of = "scheduleId")
    public static class ScheduleResponse {
        private Long scheduleId;
        private LocalDate lectureDate;
        private int currentParticipants;
        private int startHour;
        private int startMinute;
        private int endHour;
        private int endMinute;
        private int progressMinutes;
        private String scheduleStatus;

        @QueryProjection
        public ScheduleResponse(Long scheduleId, LocalDate lectureDate, int currentParticipants, LocalTime startTime, LocalTime endTime, int progressMinutes, ScheduleStatus scheduleStatus) {
            this.scheduleId = scheduleId;
            this.lectureDate = lectureDate;
            this.currentParticipants = currentParticipants;
            this.startHour = startTime.getHour();
            this.startMinute = startTime.getMinute();
            this.endHour = endTime.getHour();
            this.endMinute = endTime.getMinute();
            this.progressMinutes = progressMinutes;
            this.scheduleStatus = scheduleStatus.name();
        }
    }
}
