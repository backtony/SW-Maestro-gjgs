package com.gjgs.gjgs.modules.lecture.dtos.review;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class CreateReviewRequest {

    @NotNull(message = "작성할 클래스를 선택해주세요.")
    private Long lectureId;

    @NotNull(message = "본인이 들었던 스케줄을 입력해주세요.")
    private Long scheduleId;

    @Range(min = 1, max = 5, message = "별점은 1점 ~ 5점까지 줄 수 있습니다.")
    private int score;

    @NotBlank(message = "클래스의 후기를 작성해주세요.")
    @Length(min = 10, message = "후기는 최소 10자 이상 입력해주세요.")
    private String text;
}
