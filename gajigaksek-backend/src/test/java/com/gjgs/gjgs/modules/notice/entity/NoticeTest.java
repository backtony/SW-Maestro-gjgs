package com.gjgs.gjgs.modules.notice.entity;

import com.gjgs.gjgs.modules.dummy.NoticeDummy;
import com.gjgs.gjgs.modules.notice.dto.NoticeForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NoticeTest {

    @DisplayName("공지사항 수정")
    @Test
    void changeNotice() throws Exception{
        //given
        Notice notice = NoticeDummy.createAllNotice();
        NoticeForm noticeForm = NoticeDummy.createDirectorNoticeForm();

        //when
        notice.changeNotice(noticeForm);

        //then
        assertAll(
                () -> assertEquals(noticeForm.getNoticeType(),notice.getNoticeType()),
                () -> assertEquals(noticeForm.getText(),notice.getText()),
                () -> assertEquals(noticeForm.getTitle(),notice.getTitle())
        );
    }
}
