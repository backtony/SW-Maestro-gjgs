package com.gjgs.gjgs.modules.bulletin.dto;

import com.gjgs.gjgs.modules.utils.base.CreateRequest;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreateBulletinRequest implements CreateRequest {

    @NotNull(message = "게시글을 작성할 경우, 그룹이 존재해야 합니다.")
    private Long teamId;

    @NotBlank(message = "게시글 제목을 작성해주세요.")
    @Length(min = 5, message = "최소 5자 이상 작성해주세요.")
    private String title;

    @NotBlank(message = "나이대를 선택해주세요.")
    private String age;

    @NotBlank(message = "시간대를 입력해주세요.")
    private String timeType;

    @NotBlank(message = "소개글을 입력해주세요.")
    @Length(min = 10, max = 500, message = "10~500자 사이의 소개글을 입력해주세요.")
    private String text;

    @NotNull(message = "수강하고 싶은 클래스를 선택해주세요.")
    private Long lectureId;

    @NotBlank(message = "시간대를 입력해주세요.")
    private String dayType;
}
