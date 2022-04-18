package com.gjgs.gjgs.modules.matching.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MatchingRequest {


    @NotNull(message = "zoneId는 반드시 입력되어야 하는 값입니다.")
    private Long zoneId;

    @NotNull(message = "categoryId는 반드시 입력되어야 하는 값입니다.")
    private Long categoryId;


    @NotBlank(message = "dayType은 반드시 입력되어야 하는 값입니다.")
    private String dayType;


    @NotBlank(message = "timeType은 반드시 입력되어야 하는 값입니다.")
    private String timeType;


    @Min(value = 2,message = "매칭 인원의 최소값은 2명입니다.")
    @Max(value = 4,message = "매칭 인원의 최대값은 4명입니다.")
    private int preferMemberCount;

}
