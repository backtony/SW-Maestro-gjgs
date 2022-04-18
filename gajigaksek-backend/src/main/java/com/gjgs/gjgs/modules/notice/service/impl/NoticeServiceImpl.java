package com.gjgs.gjgs.modules.notice.service.impl;

import com.gjgs.gjgs.modules.exception.member.InvalidAuthorityException;
import com.gjgs.gjgs.modules.exception.notice.NoticeNotFoundException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.notice.aop.CheckAuthority;
import com.gjgs.gjgs.modules.notice.dto.NoticeForm;
import com.gjgs.gjgs.modules.notice.dto.NoticeResponse;
import com.gjgs.gjgs.modules.notice.entity.Notice;
import com.gjgs.gjgs.modules.notice.enums.NoticeType;
import com.gjgs.gjgs.modules.notice.repository.interfaces.NoticeQueryRepository;
import com.gjgs.gjgs.modules.notice.repository.interfaces.NoticeRepository;
import com.gjgs.gjgs.modules.notice.service.interfaces.NoticeService;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeQueryRepository noticeQueryRepository;
    private final NoticeRepository noticeRepository;
    private final SecurityUtil securityUtil;

    @Override
    @CheckAuthority
    @Transactional(readOnly = true)
    public Page<NoticeResponse> getNotice(String noticeType, Pageable pageable) {
        return noticeQueryRepository.findPagingNotice(pageable,NoticeType.valueOf(noticeType));
    }

    @Override
    public void createNotice(NoticeForm noticeForm) {
        noticeRepository.save(Notice.from(noticeForm));
    }

    @Override
    public void updateNotice(NoticeForm noticeForm, Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeNotFoundException());
        notice.changeNotice(noticeForm);
    }

    @Override
    public void deleteNotice(Long noticeId) {
        long count = noticeQueryRepository.deleteById(noticeId);
        if (count == 0){
            throw new NoticeNotFoundException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeResponse getOneNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeNotFoundException());
        Member currentUser = securityUtil.getCurrentUserOrThrow();

        if (isNotMatchNoticeTypeAndUserAuthority(notice, currentUser)){
                throw new InvalidAuthorityException();
        }

        return notice.toNoticeResponse();
    }

    private boolean isNotMatchNoticeTypeAndUserAuthority(Notice notice, Member currentUser) {
        return notice.getNoticeType().equals(NoticeType.DIRECTOR) &&
                currentUser.getAuthority().equals(Authority.ROLE_USER);
    }

}

