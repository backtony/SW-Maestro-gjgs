package com.gjgs.gjgs.modules.notice.repository.interfaces;

import com.gjgs.gjgs.modules.notice.dto.NoticeResponse;
import com.gjgs.gjgs.modules.notice.enums.NoticeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeQueryRepository {
    Page<NoticeResponse> findPagingNotice(Pageable pageable, NoticeType noticeType);

    long deleteById(Long id);
}
