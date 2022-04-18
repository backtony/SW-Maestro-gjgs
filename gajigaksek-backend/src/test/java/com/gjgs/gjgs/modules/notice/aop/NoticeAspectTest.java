package com.gjgs.gjgs.modules.notice.aop;

import com.gjgs.gjgs.modules.exception.member.InvalidAuthorityException;
import com.gjgs.gjgs.modules.exception.notice.NoticeTypeException;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.notice.dto.NoticeResponse;
import com.gjgs.gjgs.modules.notice.repository.interfaces.NoticeQueryRepository;
import com.gjgs.gjgs.modules.notice.repository.interfaces.NoticeRepository;
import com.gjgs.gjgs.modules.notice.service.impl.NoticeServiceImpl;
import com.gjgs.gjgs.modules.notice.service.interfaces.NoticeService;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeAspectTest {

    @InjectMocks NoticeAspect noticeAspect;
    @InjectMocks NoticeServiceImpl noticeService;

    @Mock SecurityUtil securityUtil;
    @Mock NoticeQueryRepository noticeQueryRepository;
    @Mock MemberRepository memberRepository;
    @Mock NoticeRepository noticeRepository;

    @DisplayName("all 타입 공지사항 가져오기 - 권한일반유저")
    @Test
    void get_notice_type_all() throws Exception{
        //given
        NoticeService proxy = getAopProxy();
        PageRequest pageRequest = PageRequest.of(1, 4, Sort.by(Sort.Direction.DESC,"createdDate"));

        //when
        Page<NoticeResponse> dtos = proxy.getNotice("ALL",pageRequest);

        //then
        verify(noticeQueryRepository,times(1)).findPagingNotice(any(),any());
    }

    @DisplayName("director 타입 공지사항 가져오기 - 권한디렉터")
    @Test
    void get_notice_type_director() throws Exception{
        //given
        NoticeService proxy = getAopProxy();
        PageRequest pageRequest = PageRequest.of(1, 4, Sort.by(Sort.Direction.DESC,"createdDate"));
        when(securityUtil.getAuthority()).thenReturn(Optional.of(Authority.ROLE_DIRECTOR));

        //when
        Page<NoticeResponse> dtos = proxy.getNotice("DIRECTOR",pageRequest);

        //then
        verify(noticeQueryRepository,times(1)).findPagingNotice(any(),any());
        verify(securityUtil,times(1)).getAuthority();
    }

    @DisplayName("director 타입 공지사항 가져오기 - 일반 유저 권한 - 권한 에러")
    @Test
    void get_notice_type_director_by_normal_user() throws Exception{
        //given
        NoticeService proxy = getAopProxy();
        PageRequest pageRequest = PageRequest.of(1, 4, Sort.by(Sort.Direction.DESC,"createdDate"));
        when(securityUtil.getAuthority()).thenReturn(Optional.of(Authority.ROLE_USER));

        //when then
        assertThrows(InvalidAuthorityException.class,
                () -> proxy.getNotice("DIRECTOR",pageRequest));

    }

    @DisplayName("존재하지 않는 공지 타입")
    @Test
    void not_exist_noticeType() throws Exception{
        //given
        NoticeService proxy = getAopProxy();
        PageRequest pageRequest = PageRequest.of(1, 4, Sort.by(Sort.Direction.DESC,"createdDate"));

        //when then
        assertThrows(NoticeTypeException.class,
                () -> proxy.getNotice("hello",pageRequest));

    }




    private NoticeService getAopProxy() {
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(noticeService);
        aspectJProxyFactory.addAspect(noticeAspect);
        NoticeService proxy = aspectJProxyFactory.getProxy();
        return proxy;
    }


}
