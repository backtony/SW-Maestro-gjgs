package com.gjgs.gjgs.modules.member.dto;

import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.login.KakaoProfile;
import com.gjgs.gjgs.modules.member.dto.login.LoginResponse;
import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.member.dto.login.TokenDto;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginResponseTest {

    @DisplayName("kakaoProfile로 LoginResponseDto 생성")
    @Test
    void create_loginResponseDto_by_kakaoProfile() throws Exception {
        //given
        KakaoProfile kakaoProfile = createKakaoProfile();

        //when
        LoginResponse loginResponse = LoginResponse.from(kakaoProfile);

        //then
        assertAll(
                () -> assertEquals(kakaoProfile.getProperties().getProfile_image(), loginResponse.getImageFileUrl()),
                () -> assertEquals(kakaoProfile.getId(), loginResponse.getId())
        );
    }

    private KakaoProfile createKakaoProfile() {
        return KakaoProfile.builder()
                .id(1L)
                .properties(
                        KakaoProfile.Properties.builder()
                                .thumbnail_image("test")
                                .profile_image("test")
                                .nickname("test")
                                .build())
                .build();
    }


    @DisplayName("Member, Token, KakaoProfile로 LoginResponseDto 생성")
    @Test
    void create_loginResponseDto_by_member_and_token_and_kakaoProfile() throws Exception {
        //given
        Member member = MemberDummy.createMemberForTokenGenerate();
        TokenDto tokenDto = MemberDummy.generateToken();
        KakaoProfile kakaoProfile = createKakaoProfile();


        //when
        LoginResponse loginResponse = LoginResponse.of(member.getId(), tokenDto, kakaoProfile);

        //then
        assertAll(
                () -> assertEquals(member.getId(), loginResponse.getMemberId()),
                () -> assertEquals(tokenDto, loginResponse.getTokenDto()),
                () -> assertEquals(kakaoProfile.getId(), loginResponse.getId()),
                () -> assertEquals(kakaoProfile.getProperties().getProfile_image(), loginResponse.getImageFileUrl())
        );
    }

    @DisplayName("Member,Token, SignupForm 으로 LoginResponseDto 생성")
    @Test
    void create_loginResponseDto_by_member_and_token_and_signupForm() throws Exception {
        //given
        Member member = MemberDummy.createMemberForTokenGenerate();
        TokenDto tokenDto = MemberDummy.generateToken();
        SignUpRequest signUpRequest = MemberDummy.createSignupForm();


        //when
        LoginResponse loginResponse = LoginResponse.of(member.getId(), tokenDto, signUpRequest.getId(),signUpRequest.getImageFileUrl());

        //then
        assertAll(
                () -> assertEquals(member.getId(), loginResponse.getMemberId()),
                () -> assertEquals(tokenDto, loginResponse.getTokenDto()),
                () -> assertEquals(signUpRequest.getId(), loginResponse.getId()),
                () -> assertEquals(signUpRequest.getImageFileUrl(), loginResponse.getImageFileUrl())
        );
    }

}
