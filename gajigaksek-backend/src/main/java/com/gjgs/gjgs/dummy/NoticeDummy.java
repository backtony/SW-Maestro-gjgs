package com.gjgs.gjgs.dummy;

import com.gjgs.gjgs.modules.notice.entity.Notice;
import com.gjgs.gjgs.modules.notice.enums.NoticeType;
import com.gjgs.gjgs.modules.notice.repository.interfaces.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class NoticeDummy {

    private final NoticeRepository noticeRepository;

    public Notice createNoticeForAll(int i){
        Notice notice = Notice.builder()
                .title("NoticeTitleForAll" + i)
                .text("NoticeTextForAll" + i)
                .noticeType(NoticeType.ALL)
                .build();
        return noticeRepository.save(notice);
    }

    public Notice createNoticeForDirector(int i){
        Notice notice = Notice.builder()
                .title("NoticeTitleForDirector" + i)
                .text("NoticeTextForDirector" + i)
                .noticeType(NoticeType.DIRECTOR)
                .build();
        return noticeRepository.save(notice);
    }
}
