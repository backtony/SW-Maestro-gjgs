package com.gjgs.gjgs.modules.lecture.dtos.apply;

import lombok.*;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
public class ApplyLectureTeamResponse {

    private Long scheduleId;

    public static ApplyLectureTeamResponse of(Long scheduleId) {
        return ApplyLectureTeamResponse.builder()
                .scheduleId(scheduleId)
                .build();
    }
}
