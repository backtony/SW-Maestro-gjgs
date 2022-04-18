package com.gjgs.gjgs.modules.notice.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class NoticeResponse {

    private Long noticeId;

    private String title;

    private String text;

    private LocalDateTime createdDate;

    @QueryProjection
    public NoticeResponse(Long noticeId, String title, String text, LocalDateTime createdDate) {
        this.noticeId = noticeId;
        this.title = title;
        this.text = text;
        this.createdDate = createdDate;
    }

}
