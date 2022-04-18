package com.gjgs.gjgs.modules.lecture.dtos.apply;

import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class ScheduleIdTeamIdDto {

    private Long scheduleId;
    private Long teamId;

    public static ScheduleIdTeamIdDto of(Long scheduleId, Long teamId) {
        return ScheduleIdTeamIdDto.builder()
                .teamId(teamId)
                .scheduleId(scheduleId)
                .build();
    }
}
