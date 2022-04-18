package com.gjgs.gjgs.modules.utils;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SecurityUtilTest {

    @InjectMocks
    SecurityUtil securityUtil;

    @Mock
    MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        SecurityContextHolder.clearContext();
    }


    @DisplayName("사용자 존재")
    @Test
    void exist_user() throws Exception {
        // given
        Authentication authentication = MemberDummy.createAuthentication();

        // when
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // then
        assertEquals(authentication.getName(), securityUtil.getCurrentUsername().get());


    }

    @DisplayName("익명 사용자")
    @Test
    void anonymous_user() throws Exception {
        assertEquals(securityUtil.getCurrentUsername(), Optional.empty());
    }

    @DisplayName("권한 추출")
    @Test
    void get_authority() throws Exception{
        // given
        Authentication authentication = MemberDummy.createAuthentication();

        // when
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // then
        assertEquals(Authority.ROLE_USER, securityUtil.getAuthority().get());
    }

    @DisplayName("익명 권한 추출")
    @Test
    void get_none_authority() throws Exception{

        // then
        assertEquals(Optional.empty(), securityUtil.getAuthority());
    }

    @DisplayName("현재 사용자 가져오기")
    @Test
    void get_currentUser() throws Exception {
        // given
        Authentication authentication = MemberDummy.createAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Member member = MemberDummy.createTestMember();
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));

        // when
        Member currentUser = securityUtil.getCurrentUserOrThrow();

        // then
        assertEquals(member, currentUser);
    }



    @DisplayName("현재 사용자 정보가 db에 없는 경우")
    @Test
    void cannot_find_currentUser_in_db() throws Exception {
        // given
        Authentication authentication = MemberDummy.createAuthentication();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(memberRepository.findByUsername(any())).thenThrow(MemberNotFoundException.class);

        // when then
        assertThrows(MemberNotFoundException.class,() -> securityUtil.getCurrentUserOrThrow());
    }

    @DisplayName("로그인 하지 않은 사용자")
    @Test
    void get_currentUser_throw_memberNotFoundException() throws Exception {

        // when then
        assertThrows(MemberNotFoundException.class,() -> securityUtil.getCurrentUserOrThrow());
    }



}

