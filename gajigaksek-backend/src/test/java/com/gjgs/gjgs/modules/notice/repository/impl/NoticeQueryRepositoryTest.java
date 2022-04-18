package com.gjgs.gjgs.modules.notice.repository.impl;

import com.gjgs.gjgs.config.repository.RepositoryTest;
import com.gjgs.gjgs.modules.dummy.NoticeDummy;
import com.gjgs.gjgs.modules.notice.dto.NoticeResponse;
import com.gjgs.gjgs.modules.notice.entity.Notice;
import com.gjgs.gjgs.modules.notice.enums.NoticeType;
import com.gjgs.gjgs.modules.notice.repository.interfaces.NoticeQueryRepository;
import com.gjgs.gjgs.modules.notice.repository.interfaces.NoticeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

class NoticeQueryRepositoryTest extends RepositoryTest {

    @Autowired NoticeQueryRepository noticeQueryRepository;
    @Autowired NoticeRepository noticeRepository;

    @AfterEach
    void tearDown() throws Exception {
        noticeRepository.deleteAll();
    }

    @DisplayName("페이징 공지사항")
    @Test
    void find_paging_notice() throws Exception{
        //given
        Notice save = NoticeDummy.createAllNotice();
        for(int i=0;i<30;i++){
            noticeRepository.save(NoticeDummy.createAllNotice());
            noticeRepository.save(NoticeDummy.createDirectorNotice());
        }

        PageRequest pageRequest = PageRequest.of(1, 4, Sort.by(Sort.Direction.DESC,"createdDate"));

        flushAndClear();

        //when
        Page<NoticeResponse> dtos = noticeQueryRepository.findPagingNotice(pageRequest, NoticeType.ALL);

        //then
        assertAll(
                () -> assertEquals(8,dtos.getTotalPages()),
                () -> assertEquals(30,dtos.getTotalElements()),
                () -> assertEquals(4,dtos.getSize()),
                () -> assertEquals(1,dtos.getPageable().getPageNumber()),
                () -> assertEquals(save.getText(),dtos.getContent().get(0).getText()),
                () -> assertEquals(save.getTitle(),dtos.getContent().get(0).getTitle()),
                () -> assertNotNull(dtos.getContent().get(0).getNoticeId())
        );
    }

    @DisplayName("특정 공지사항 삭제")
    @Test
    void delete_by_id() throws Exception{
        //given
        Notice save = noticeRepository.save(NoticeDummy.createAllNotice());

        //when
        long l = noticeQueryRepository.deleteById(save.getId());

        //then
        assertEquals(1,l);
    }

}
