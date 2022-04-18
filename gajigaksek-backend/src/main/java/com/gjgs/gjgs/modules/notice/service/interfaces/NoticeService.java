package com.gjgs.gjgs.modules.notice.service.interfaces;

import com.gjgs.gjgs.modules.notice.dto.NoticeForm;
import com.gjgs.gjgs.modules.notice.dto.NoticeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    Page<NoticeResponse> getNotice(String noticeType, Pageable pageable);

    void createNotice(NoticeForm noticeForm);

    void updateNotice(NoticeForm noticeForm, Long noticeId);

    void deleteNotice(Long noticeId);

    NoticeResponse getOneNotice(Long noticeId);
}
