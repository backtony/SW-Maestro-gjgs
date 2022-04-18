package com.gjgs.gjgs.modules.notice.service.impl;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.NoticeDummy;
import com.gjgs.gjgs.modules.exception.member.InvalidAuthorityException;
import com.gjgs.gjgs.modules.exception.notice.NoticeNotFoundException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.notice.dto.NoticeForm;
import com.gjgs.gjgs.modules.notice.dto.NoticeResponse;
import com.gjgs.gjgs.modules.notice.entity.Notice;
import com.gjgs.gjgs.modules.notice.enums.NoticeType;
import com.gjgs.gjgs.modules.notice.repository.interfaces.NoticeQueryRepository;
import com.gjgs.gjgs.modules.notice.repository.interfaces.NoticeRepository;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceImplTest {

    @InjectMocks NoticeServiceImpl noticeService;
    @Mock NoticeQueryRepository noticeQueryRepository;
    @Mock NoticeRepository noticeRepository;
    @Mock SecurityUtil securityUtil;
    @Mock MemberRepository memberRepository;

    @DisplayName("all 타입 공지사항 가져오기")
    @Test
    void get_notice() throws Exception{
        //given
        Notice allNotice = NoticeDummy.createAllNotice();
        PageRequest pageRequest = PageRequest.of(1, 4, Sort.by(Sort.Direction.DESC,"createdDate"));
        when(noticeQueryRepository.findPagingNotice(any(),any())).thenReturn(createPageNoticeDto(pageRequest));

        //when
        Page<NoticeResponse> dtos = noticeService.getNotice("ALL",pageRequest);

        //then
        assertAll(
                () -> assertEquals(9,dtos.getTotalPages()),
                () -> assertEquals(35,dtos.getTotalElements()),
                () -> assertEquals(4,dtos.getSize()),
                () -> assertEquals(1,dtos.getPageable().getPageNumber()),
                () -> assertEquals(allNotice.getText(),dtos.getContent().get(0).getText()),
                () -> assertEquals(allNotice.getTitle(),dtos.getContent().get(0).getTitle())
        );

    }

    private Page<NoticeResponse> createPageNoticeDto(Pageable pageable) {
        return new PageImpl<>(NoticeDummy.createNoticeDtoList(),
                pageable,
                35);
    }

    @DisplayName("공지사항 저장")
    @Test
    void create_notice() throws Exception{
        //given
        NoticeForm noticeForm = NoticeDummy.createNoticeForm();

        //when
        noticeService.createNotice(noticeForm);

        //then
        verify(noticeRepository).save(any());
    }

    @DisplayName("공지사항 수정")
    @Test
    void update_notice() throws Exception{
        //given
        Notice allNotice = NoticeDummy.createAllNotice();
        when(noticeRepository.findById(any())).thenReturn(Optional.of(allNotice));

        NoticeForm noticeForm = NoticeDummy.createDirectorNoticeForm();

        //when
        noticeService.updateNotice(noticeForm,1L);

        //then
        assertAll(
                () -> assertEquals(noticeForm.getTitle(),allNotice.getTitle()),
                () -> assertEquals(noticeForm.getText(),allNotice.getText()),
                () -> assertEquals(NoticeType.DIRECTOR,allNotice.getNoticeType())
        );
    }

    @DisplayName("공지사항 수정에서 공지사항을 찾지 못했을때")
    @Test
    void update_notice_fail() throws Exception{
        //given
        NoticeForm noticeForm = NoticeDummy.createDirectorNoticeForm();


        // when then
        assertThrows(NoticeNotFoundException.class,
                () -> noticeService.updateNotice(noticeForm,1L));
    }

    @DisplayName("공지사항 삭제")
    @Test
    void delete_notice() throws Exception{
        //given
        when(noticeQueryRepository.deleteById(any())).thenReturn(1L);

        //when
        noticeService.deleteNotice(1L);

        //then
        verify(noticeQueryRepository).deleteById(any());
    }

    @DisplayName("공지사항 삭제 실패")
    @Test
    void delete_notice_fail() throws Exception{
        assertThrows(NoticeNotFoundException.class,
                () -> noticeService.deleteNotice(1L));
    }



    @DisplayName("공지사항 하나 가져오기")
    @Test
    void get_one_notice() throws Exception{
        //given
        Notice notice = NoticeDummy.createDirectorNotice();
        Member member = MemberDummy.createTestDirectorMember();

        stubGetCurrentUser(member);
        when(noticeRepository.findById(any())).thenReturn(Optional.of(notice));

        //when
        NoticeResponse oneNotice = noticeService.getOneNotice(1L);

        //then
        assertAll(
                () -> assertEquals(notice.getText(),oneNotice.getText()),
                () -> assertEquals(notice.getTitle(),oneNotice.getTitle())
        );
    }

    @DisplayName("기본 유저로 디렉터용 공지사항 한개에 접근")
    @Test
    void get_one_notice_fail() throws Exception{
        //given
        Notice notice = NoticeDummy.createDirectorNotice();
        Member member = MemberDummy.createTestMember();

        when(noticeRepository.findById(any())).thenReturn(Optional.of(notice));
        stubGetCurrentUser(member);

        //when then
        assertThrows(InvalidAuthorityException.class
                ,() -> noticeService.getOneNotice(1L));

    }


    private void stubGetCurrentUser(Member member) {
        when(securityUtil.getCurrentUserOrThrow()).thenReturn(member);
    }



}
