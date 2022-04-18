package com.gjgs.gjgs.modules.lecture.dtos.create;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gjgs.gjgs.modules.lecture.validators.minute.CheckMinuteDivideThirty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "createLectureStep",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "FIRST", value = CreateLecture.FirstRequest.class),
        @JsonSubTypes.Type(name = "INTRO", value = CreateLecture.IntroRequest.class),
        @JsonSubTypes.Type(name = "CURRICULUM", value = CreateLecture.CurriculumRequest.class),
        @JsonSubTypes.Type(name = "SCHEDULE", value = CreateLecture.ScheduleRequest.class),
        @JsonSubTypes.Type(name = "PRICE_COUPON", value = CreateLecture.PriceCouponRequest.class),
        @JsonSubTypes.Type(name = "TERMS", value = CreateLecture.TermsRequest.class)
})
public abstract class CreateLecture {

    private CreateLectureStep createLectureStep;

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @SuperBuilder
    public static class FirstRequest extends CreateLecture {

        private Long lectureId;

        @NotNull(message = "취미 카테고리를 선택해주세요.")
        private Long categoryId;

        @NotNull(message = "지역을 선택해주세요.")
        private Long zoneId;

        @NotBlank(message = "제목을 입력해주세요.")
        @Length(min = 10, max = 100, message = "10자 이상 100자 이하로 제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "주소를 입력해주세요.")
        private String address;

        private String thumbnailImageFileName;
        private String thumbnailImageFileUrl;
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @SuperBuilder
    public static class IntroRequest extends CreateLecture {

        @NotBlank(message = "클래스 소개글을 입력해주세요.")
        @Length(min = 10, max = 100, message = "10자 이상 500자 이하로 소개글을 입력해주세요.")
        private String mainText;

        @Builder.Default
        @NotEmpty(message = "완성 작품에 대한 정보들을 입력해주세요.")
        @Size(min = 1, max = 4, message = "완성작은 최소 한개 최대 네개를 입력해주셔야 합니다.")
        @Valid
        private List<FinishedProductInfoDto> finishedProductInfoList = new ArrayList<>();
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @SuperBuilder
    public static class FinishedProductInfoDto {

        @NotNull(message = "완성작의 순서를 입력해주세요.")
        private int order;
        @NotBlank(message = "완성작의 제목을 입력해주세요.")
        private String text;

        private String finishedProductImageName;
        private String finishedProductImageUrl;
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @SuperBuilder
    public static class CurriculumRequest extends CreateLecture  {

        @Valid
        @NotEmpty(message = "커리큘럼 리스트를 작성해주세요.")
        @Builder.Default
        private List<CurriculumDto> curriculumList = new ArrayList<>();
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @SuperBuilder
    public static class CurriculumDto {

        @NotNull(message = "순서를 입력해주세요.")
        private int order;

        @NotBlank(message = "제목을 입력해주세요.")
        @Length(min = 5, max = 50, message = "제목을 5자에서 50자 사이로 입력해주세요.")
        private String title;

        @NotBlank(message = "상세 내용을 입력해주세요.")
        @Length(min = 10, max = 300, message = "상세 내용을 10자에서 300자 사이로 입력해주세요.")
        private String detailText;

        private String curriculumImageName;
        private String curriculumImageUrl;
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @SuperBuilder
    public static class ScheduleRequest extends CreateLecture  {

        @Min(value = 0, message = "0명(무제한) 또는 1명 이상을 입력해주세요.")
        private int minParticipants;

        @Min(value = 0, message = "0명(무제한) 또는 1명 이상을 입력해주세요.")
        private int maxParticipants;

        @Valid
        @NotEmpty(message = "스케줄을 작성해주세요.")
        @Builder.Default
        private List<ScheduleDto> scheduleList = new ArrayList<>();
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @SuperBuilder
    public static class ScheduleDto {

        @Min(value = 60, message = "최소 1시간 이상 클래스를 진행해야합니다.")
        @CheckMinuteDivideThirty
        private int progressMinute;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "날짜를 yyyy-MM-dd 형식에 맞게 입력해주세요.")
        private LocalDate lectureDate;

        @Range(min = 0, max = 23, message = "0시 부터 23시까지 입력 가능합니다.")
        private int startHour;

        @Range(min = 0, max = 59, message = "0분 부터 59분까지 입력 가능합니다.")
        @CheckMinuteDivideThirty
        private int startMinute;

        // 안줘도 되는 것들
        private int endHour;
        private int endMinute;

        public ScheduleDto(int progressMinute, LocalDate lectureDate, int startHour, int startMinute, int endHour, int endMinute) {
            this.progressMinute = progressMinute;
            this.lectureDate = lectureDate;
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.endHour = endHour;
            this.endMinute = endMinute;
        }

        public void setEndTimes() {
            LocalTime startTime = LocalTime.of(this.startHour, this.startMinute);
            LocalTime plusTime = LocalTime.of(Math.floorDiv(this.progressMinute, 60), Math.floorMod(this.progressMinute, 60));
            LocalTime endTime = startTime.plusHours(plusTime.getHour()).plusMinutes(plusTime.getMinute());
            this.endHour = endTime.getHour();
            this.endMinute = endTime.getMinute();
        }
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @SuperBuilder
    public static class PriceCouponRequest extends CreateLecture  {

        private PriceDto price;
        private CouponDto coupon;
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @SuperBuilder
    public static class PriceDto {

        private int regularPrice;
        private int priceOne;
        private int priceTwo;
        private int priceThree;
        private int priceFour;
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @SuperBuilder
    public static class CouponDto {

        private int couponPrice;
        private int couponCount;
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor @SuperBuilder
    public static class TermsRequest extends CreateLecture  {

        @AssertTrue(message = "모든 이용약관에 동의해야 합니다.")
        private boolean termsOne;

        @AssertTrue(message = "모든 이용약관에 동의해야 합니다.")
        private boolean termsTwo;

        @AssertTrue(message = "모든 이용약관에 동의해야 합니다.")
        private boolean termsThree;

        @AssertTrue(message = "모든 이용약관에 동의해야 합니다.")
        private boolean termsFour;
    }
}
