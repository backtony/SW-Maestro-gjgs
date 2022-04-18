package com.gjgs.gjgs.modules.team.dtos;

import com.gjgs.gjgs.modules.utils.base.CreateRequest;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CreateTeamRequest implements CreateRequest {

    @NotBlank(message = "그룹명을 입력해주세요.")
    private String teamName;

    // 1일 경우 4명으로 픽스
    @NotNull(message = "그룹의 최대 인원을 입력해주세요.")
    @Range(min = 1, max = 4, message = "그룹은 2명 ~ 4명으로 구성됩니다.")
    private int maxPeople;

    @NotNull(message = "지역을 선택해주세요.")
    private Long zoneId;

    @NotBlank(message = "요일을 선택해주세요. 무관일 경우 무관을 선택해주세요")
    private String dayType;

    @NotBlank(message = "시간을 선택해주세요. 무관일 경우 무관을 선택해주세요")
    private String timeType;

    @Builder.Default
    @NotEmpty(message = "취미 카테고리를 선택해주세요.")
    private List<Long> categoryList = new ArrayList<>();
}
