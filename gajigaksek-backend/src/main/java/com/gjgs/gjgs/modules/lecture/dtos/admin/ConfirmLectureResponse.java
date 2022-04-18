package com.gjgs.gjgs.modules.lecture.dtos.admin;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @Builder
public class ConfirmLectureResponse {

    private Long lectureId;
    private String lectureTitle;
    private LocalDateTime confirmDateTime;
    private String directorNickname;
    private Long categoryId;
    private String categoryName;
    private Long zoneId;
    private String zoneName;

    @QueryProjection
    public ConfirmLectureResponse(Long lectureId, String lectureTitle, LocalDateTime confirmDateTime, String directorNickname, Long categoryId, String categoryName, Long zoneId, String zoneName) {
        this.lectureId = lectureId;
        this.lectureTitle = lectureTitle;
        this.confirmDateTime = confirmDateTime;
        this.directorNickname = directorNickname;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.zoneId = zoneId;
        this.zoneName = zoneName;
    }
}
