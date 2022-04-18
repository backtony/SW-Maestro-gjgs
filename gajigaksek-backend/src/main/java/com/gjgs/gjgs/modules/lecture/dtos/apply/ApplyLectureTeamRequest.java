package com.gjgs.gjgs.modules.lecture.dtos.apply;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED) @Builder
public class ApplyLectureTeamRequest {

    @NotNull(message = "신청하거나 취소할 팀의 ID를 입력해주세요.")
    private Long teamId;

    @NotNull(message = "팀이 신청하거나 취소할 클래스 ID를 입력해주세요.")
    private Long lectureId;
}
