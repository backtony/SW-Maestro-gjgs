package com.gjgs.gjgs.modules.lecture.dtos.director.schedule;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse.PostDelete.Result.CREATE;
import static com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse.PostDelete.Result.DELETE;
import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @Builder
public class DirectorScheduleResponse {

    private Long lectureId;
    private int progressTime;
    private int minParticipants;
    private int maxParticipants;
    @Builder.Default
    private Set<ScheduleDto> scheduleList = new LinkedHashSet<>();

    @QueryProjection
    public DirectorScheduleResponse(Long lectureId, int progressTime, int minParticipants, int maxParticipants, Set<ScheduleDto> scheduleList) {
        this.lectureId = lectureId;
        this.progressTime = progressTime;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.scheduleList = scheduleList;
    }

    @Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
    @EqualsAndHashCode(of = "scheduleId")
    public static class ScheduleDto {
        private Long scheduleId;
        private LocalDate lectureDate;
        private int startHour;
        private int startMinute;
        private int endHour;
        private int endMinute;
        private int currentParticipants;
        private boolean canDelete;

        @QueryProjection
        public ScheduleDto(Long scheduleId, LocalDate lectureDate, LocalTime startTime, LocalTime endTime, int currentParticipants) {
            this.scheduleId = scheduleId;
            this.lectureDate = lectureDate;
            this.startHour = startTime.getHour();
            this.startMinute = startTime.getMinute();
            this.endHour = endTime.getHour();
            this.endMinute = endTime.getMinute();
            this.currentParticipants = currentParticipants;
            this.canDelete = currentParticipants == 0;
        }
    }

    @Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
    public static class PostDelete {

        private Long scheduleId;
        private Result result;

        @Getter @AllArgsConstructor
        public enum Result implements EnumType {
            CREATE("스케줄 추가 완료"),
            DELETE("스케줄 삭제 완료")
            ;

            private String description;

            @Override
            public String getName() {
                return this.name();
            }
        }

        public static DirectorScheduleResponse.PostDelete post(Long scheduleId) {
            return DirectorScheduleResponse.PostDelete.builder()
                    .scheduleId(scheduleId)
                    .result(CREATE)
                    .build();
        }

        public static DirectorScheduleResponse.PostDelete delete(Long scheduleId) {
            return DirectorScheduleResponse.PostDelete.builder()
                    .scheduleId(scheduleId)
                    .result(DELETE)
                    .build();
        }
    }
}
