package com.gjgs.gjgs.modules.lecture.dtos.director.lecture;

import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) @Builder
public class DirectorLectureResponse {

    @Builder.Default
    private List<LectureResponse> lectureList = new ArrayList<>();

    @QueryProjection
    public DirectorLectureResponse(List<LectureResponse> lectureList) {
        this.lectureList = lectureList;
    }

    @Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) @AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
    public static class LectureResponse {
        private Long savedLectureId;
        private String thumbnailImageUrl;
        private String title;
        private String mainText;
        private String lectureStatus;
        private boolean finished;
        private String rejectReason;

        @QueryProjection
        public LectureResponse(Long savedLectureId, String thumbnailImageUrl, String title, String mainText, LectureStatus lectureStatus, boolean finished, String rejectReason) {
            this.savedLectureId = savedLectureId;
            this.thumbnailImageUrl = thumbnailImageUrl;
            this.title = title;
            this.mainText = mainText;
            this.lectureStatus = lectureStatus.name();
            this.finished = finished;
            this.rejectReason = rejectReason;
        }
    }
}
