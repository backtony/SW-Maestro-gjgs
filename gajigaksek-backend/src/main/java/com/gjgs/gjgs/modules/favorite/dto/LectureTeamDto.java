package com.gjgs.gjgs.modules.favorite.dto;

import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class LectureTeamDto {

    private Long lectureTeamId;

    private Long lectureId;

    @Lob
    private String thumbnailImageFileUrl;

    private Long zoneId;

    private String title;

    private Price price;

    @QueryProjection
    public LectureTeamDto(Long lectureTeamId, Long lectureId, String thumbnailImageFileUrl, Long zoneId, String title, Price price) {
        this.lectureTeamId = lectureTeamId;
        this.lectureId = lectureId;
        this.thumbnailImageFileUrl = thumbnailImageFileUrl;
        this.zoneId = zoneId;
        this.title = title;
        this.price = price;
    }
}
