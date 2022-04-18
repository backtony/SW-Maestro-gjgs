package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.notice.dto.NoticeForm;
import com.gjgs.gjgs.modules.notice.dto.NoticeResponse;
import com.gjgs.gjgs.modules.notice.entity.Notice;
import com.gjgs.gjgs.modules.notice.enums.NoticeType;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

public class NoticeDummy {
    public static Notice createAllNotice(){
        return Notice.builder()
                .noticeType(NoticeType.ALL)
                .title("AllTitle")
                .text("AllText")
                .build();
    }

    public static Notice createDirectorNotice(){
        return Notice.builder()
                .noticeType(NoticeType.DIRECTOR)
                .title("DirectorTitle")
                .text("DirectorText")
                .build();
    }
    public static List<NoticeResponse> createNoticeDtoList() {
        List<NoticeResponse> resList = new ArrayList<>();
        Notice allNotice = createAllNotice();
        resList.add(NoticeResponse.builder()
                .noticeId((long) 1)
                .text(allNotice.getText())
                .title(allNotice.getTitle())
                .createdDate(now())
                .build());
        return resList;
    }

    public static NoticeForm createNoticeForm(){
        return NoticeForm.builder()
                .title("title")
                .text("text")
                .noticeType(NoticeType.ALL)
                .build();
    }

    public static NoticeForm createDirectorNoticeForm(){
        return NoticeForm.builder()
                .title("title")
                .text("text")
                .noticeType(NoticeType.DIRECTOR)
                .build();
    }

}
