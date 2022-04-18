package com.gjgs.gjgs.modules.lecture.dtos.create;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
public class PutLectureResponse {

    private Long lectureId;
    private Long categoryId;
    private Long zoneId;
    private String title;
    private String address;
    private String thumbnailImageFileName;
    private String thumbnailImageFileUrl;
    private int minParticipants;
    private int maxParticipants;
    private String mainText;
    private String lectureStatus;

    @Builder.Default
    private Set<FinishedProductResponse> finishedProductList = new LinkedHashSet<>();

    @Builder.Default
    private Set<CurriculumResponse> curriculumList = new LinkedHashSet<>();

    @Builder.Default
    private Set<ScheduleResponse> scheduleList = new LinkedHashSet<>();

    private PriceResponse price;
    private CouponResponse coupon;

    @QueryProjection
    public PutLectureResponse(Long lectureId, Long categoryId, Long zoneId, String title, String address, String thumbnailImageFileName, String thumbnailImageFileUrl, int minParticipants, int maxParticipants, String mainText, LectureStatus lectureStatus, Set<FinishedProductResponse> finishedProductList, Set<CurriculumResponse> curriculumList, Set<ScheduleResponse> scheduleList, PriceResponse price) {
        this.lectureId = lectureId;
        this.categoryId = categoryId;
        this.zoneId = zoneId;
        this.title = title;
        this.address = address;
        this.thumbnailImageFileName = thumbnailImageFileName;
        this.thumbnailImageFileUrl = thumbnailImageFileUrl;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.mainText = mainText;
        this.lectureStatus = lectureStatus.name();
        this.finishedProductList = finishedProductList;
        this.curriculumList = curriculumList;
        this.scheduleList = scheduleList;
        this.price = price;
    }

    @JsonIgnore
    public List<String> getAllFilenames() {
        List<String> filenames = new ArrayList<>();
        filenames.add(this.getThumbnailImageFileName());
        filenames.addAll(this.getFinishedProductList().stream()
                .map(FinishedProductResponse::getFinishedProductImageName).collect(toList()));
        filenames.addAll(this.getCurriculumList().stream()
                .map(CurriculumResponse::getCurriculumImageName).collect(toList()));
        return filenames;
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) @Builder
    @EqualsAndHashCode(of = "finishedProductId")
    public static class FinishedProductResponse {
        private Long finishedProductId;
        private int orders;
        private String text;
        private String finishedProductImageName;
        private String finishedProductImageUrl;

        @QueryProjection
        public FinishedProductResponse(Long finishedProductId, int orders, String text, String finishedProductImageName, String finishedProductImageUrl) {
            this.finishedProductId = finishedProductId;
            this.orders = orders;
            this.text = text;
            this.finishedProductImageName = finishedProductImageName;
            this.finishedProductImageUrl = finishedProductImageUrl;
        }
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) @Builder
    @EqualsAndHashCode(of = "curriculumId")
    public static class CurriculumResponse {
        private Long curriculumId;
        private int orders;
        private String title;
        private String detailText;
        private String curriculumImageName;
        private String curriculumImageUrl;

        @QueryProjection
        public CurriculumResponse(Long curriculumId, int orders, String title, String detailText, String curriculumImageName, String curriculumImageUrl) {
            this.curriculumId = curriculumId;
            this.orders = orders;
            this.title = title;
            this.detailText = detailText;
            this.curriculumImageName = curriculumImageName;
            this.curriculumImageUrl = curriculumImageUrl;
        }
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
    @EqualsAndHashCode(of = "scheduleId")
    public static class ScheduleResponse {
        private Long scheduleId;
        private LocalDate lectureDate;
        private int startHour;
        private int startMinute;
        private int endHour;
        private int endMinute;
        private int progressMinute;

        @QueryProjection
        public ScheduleResponse(Long scheduleId, LocalDate lectureDate, LocalTime startTime, LocalTime endTime, int progressMinute) {
            this.scheduleId = scheduleId;
            this.lectureDate = lectureDate;
            this.startHour = startTime == null ? 0 : startTime.getHour();
            this.startMinute = startTime == null ? 0 : startTime.getMinute();
            this.endHour = endTime == null ? 0 : endTime.getHour();
            this.endMinute = endTime == null ? 0 : endTime.getMinute();
            this.progressMinute = progressMinute;
        }
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) @Builder
    public static class PriceResponse {
        private int regularPrice;
        private int priceOne;
        private int priceTwo;
        private int priceThree;
        private int priceFour;

        @QueryProjection
        public PriceResponse(int regularPrice, int priceOne, int priceTwo, int priceThree, int priceFour) {
            this.regularPrice = regularPrice;
            this.priceOne = priceOne;
            this.priceTwo = priceTwo;
            this.priceThree = priceThree;
            this.priceFour = priceFour;
        }
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) @Builder
    public static class CouponResponse {
        private int couponPrice;
        private int couponCount;

        @QueryProjection
        public CouponResponse(int couponPrice, int couponCount) {
            this.couponPrice = couponPrice;
            this.couponCount = couponCount;
        }
    }
}
