package com.gjgs.gjgs.modules.member.controller;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.login.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.DocUrl;
import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerTest extends RestDocsTestSupport {

    final String LOGIN = "/api/v1/login";
    final String LOGOUT = "/api/v1/logout";
    final String SIGNUP = "/api/v1/sign-up";
    final String KAKAO_TOKEN_HEADER = "KakaoAccessToken";
    final String BEARER_ACCESS_TOKEN = "Bearer access_token";
    final String BEARER_KAKAO_TOKEN = "Bearer kakao_token";
    final String BEARER_REFRESH_TOKEN = "Bearer refresh_token";
    final String REFRESH_TOKEN_HEADER = "RefreshToken";


    @DisplayName("토큰 타입이 Bearer이 아닐 때")
    @Test
    void common_errors_should_token_grant_type_is_bearer() throws Exception {

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(LOGOUT)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "test "))
                .andExpect(status().isUnauthorized())
                ;

    }

    @DisplayName("정상적이지 않은 토큰")
    @Test
    void common_errors_should_not_request_not_normal_token() throws Exception {
        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(LOGOUT)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "invalid"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value("KAKAO-401"));
    }

    @DisplayName("로그인 성공")
    @Test
    void login() throws Exception {
        //given
        String fcmToken = "testFcmToken";
        FcmTokenRequest fcmTokenRequest = FcmTokenRequest.builder()
                .fcmToken(fcmToken)
                .build();
        LoginResponse loginResponse = createLoginResponse();
        when(loginService.login(any(),any())).thenReturn(loginResponse);

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(LOGIN + "/{provider}", "kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fcmTokenRequest))
                .header(KAKAO_TOKEN_HEADER, BEARER_KAKAO_TOKEN))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("provider").description("소셜 로그인 제공자")
                        ),
                        requestHeaders(
                                headerWithName("kakaoAccessToken").description("카카오 access token")
                        ),
                        requestFields(
                                fieldWithPath("fcmToken").type(STRING).description("fcm token")
                        ),
                        responseFields(
                                fieldWithPath("tokenDto.grantType").type(STRING).description("토큰 타입"),
                                fieldWithPath("tokenDto.accessToken").type(STRING).description("JWT Access Token"),
                                fieldWithPath("tokenDto.refreshToken").type(STRING).description("JWT Refresh Token"),
                                fieldWithPath("tokenDto.accessTokenExpiresIn").type(NUMBER).description("JWT Access Token 만료 시간"),
                                fieldWithPath("tokenDto.refreshTokenExpiresIn").type(NUMBER).description("JWT Refresh Token 만료 시간"),
                                fieldWithPath("memberId").type(NUMBER).description("멤버 ID"),
                                fieldWithPath("id").type(NUMBER).description("카카오 회원 고유 ID"),
                                fieldWithPath("imageFileUrl").type(STRING).description("카카오 프로필 URL")
                        )
                ))
        ;
    }

    @DisplayName("FcmTokenForm @valid 동작하는지 확인")
    @Test
    void login_should_valid_fcm_token_form() throws Exception {
        // given
        SignUpRequest signUpRequest = MemberDummy.createInvalidSignupForm();
        String fcmToken = "";

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(LOGIN + "/{provider}", "kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fcmToken))
                .header(KAKAO_TOKEN_HEADER, BEARER_KAKAO_TOKEN))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @DisplayName("로그인 시도 시, 기존 회원이 아닌 경우")
    @Test
    void login_fail_redirect_sign_up() throws Exception {
        //given
        FcmTokenRequest fcmTokenRequest = FcmTokenRequest.builder()
                .fcmToken("FcmToken")
                .build();

        LoginResponse loginResponse = LoginResponse.from(
                createKakaoProfile("profile image url"));
        when(loginService.login(any(),any())).thenReturn(loginResponse);

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(LOGIN + "/{provider}", "kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson(fcmTokenRequest))
                .header(KAKAO_TOKEN_HEADER, BEARER_KAKAO_TOKEN))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("fcmToken").description("FCM 토큰")
                        ),
                        responseFields(
                                fieldWithPath("tokenDto").description("생성된 토큰 값"),
                                fieldWithPath("memberId").description("회원 Id 값"),
                                fieldWithPath("id").description("카카오에서 제공하는 id값"),
                                fieldWithPath("imageFileUrl").description("카카오에서 제공하는 프로필 이미지 url")
                        )
                ))

        ;
    }

    @DisplayName("로그인 시도시 fcmTokenRequest 가 비어있는 경우")
    @Test
    void login_fcmTokenRequest_valid_check() throws Exception {
        //given
        FcmTokenRequest fcmTokenRequest = FcmTokenRequest.builder().fcmToken("").build();

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(LOGIN + "/{provider}", "kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson(fcmTokenRequest))
                .header(KAKAO_TOKEN_HEADER, BEARER_KAKAO_TOKEN))
                .andExpect(status().isBadRequest())
                .andDo(restDocs.document(
                        responseFields(errorDescriptor())
                ))
        ;
    }


    @DisplayName("로그인 시도 후, 기존 회원이 아니어서 리다이렉트로 sign up 하는 경우 - 회원가입 + 로그인")
    @Test
    void sign_up() throws Exception {
        // given
        SignUpRequest signUpRequest = MemberDummy.createSignupForm();

        TokenDto tokenDto = createTestTokenDto();
        LoginResponse loginResponse = LoginResponse.of(
                1L,
                tokenDto,
                signUpRequest.getId(),
                signUpRequest.getImageFileUrl()
                );

        when(memberRepository.existsByUsername(any())).thenReturn(false);
        when(zoneRepository.existsById(any())).thenReturn(true);
        when(categoryRepository.countCategoryByIdList(any())).thenReturn(3L);
        when(memberRepository.existsByNickname(any()))
                .thenReturn(false)
                .thenReturn(true);
        when(loginService.saveAndLogin(any())).thenReturn(loginResponse);

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("tokenDto.grantType").value(tokenDto.getGrantType()))
                .andExpect(jsonPath("tokenDto.accessToken").value(tokenDto.getAccessToken()))
                .andExpect(jsonPath("tokenDto.refreshToken").value(tokenDto.getRefreshToken()))
                .andExpect(jsonPath("tokenDto.accessTokenExpiresIn").value(tokenDto.getAccessTokenExpiresIn()))
                .andExpect(jsonPath("tokenDto.refreshTokenExpiresIn").value(tokenDto.getRefreshTokenExpiresIn()))
                .andExpect(jsonPath("memberId").value(1))
                .andExpect(jsonPath("imageFileUrl").value(loginResponse.getImageFileUrl()))
                .andExpect(jsonPath("id").value(loginResponse.getId()))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("id").description("카카오에서 제공하는 id"),
                                fieldWithPath("imageFileUrl").description("카카오에서 제공하는 프로필 이미지 url"),
                                fieldWithPath("name").description("실명").attributes(field("constraints", "[1,10] 자리 한글")),
                                fieldWithPath("phone").description("휴대폰 번호").attributes(field("constraints", "[10,11] 자리 숫자")),
                                fieldWithPath("nickname").description("닉네임").attributes(field("constraints", "한글,영문,숫자 혼합 [2,20] 글자")),
                                fieldWithPath("age").description("나이").attributes(field("constraints", "[10,100] 숫자")),
                                fieldWithPath("sex").description(generateLinkCode(DocUrl.SEX)),
                                fieldWithPath("zoneId").description("지역 id"),
                                fieldWithPath("categoryIdList").description("선호하는 카테고리 id 리스트").attributes(field("constraints", "최소 1개, 최대 3개")),
                                fieldWithPath("fcmToken").description("FCM 토큰"),
                                fieldWithPath("recommendNickname").description("추천인 닉네임").optional()
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("회원 Id 값"),
                                fieldWithPath("id").description("카카오에서 제공하는 id값"),
                                fieldWithPath("imageFileUrl").description("프로필 파일 url")
                        ).and(tokenDtoDescriptor())
                ))
        ;
    }

    @DisplayName("signupForm @validated")
    @Test
    void sign_up_bean_validation() throws Exception {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .name("가나다라마바사아자차카타파하")
                .phone("01022")
                .nickname("@@@")
                .age(101)
                .sex("E")
                .categoryIdList(List.of(1L,2L,3L))
                .build();

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(10)));
    }

    @DisplayName("signupForm validator 동작하는지 확인 - 논리적 오류")
    @Test
    void sign_up_validator() throws Exception {

        // given
        SignUpRequest signUpRequest = MemberDummy.createWrongInitBinderSignupForm();
        when(memberRepository.existsByNickname(any())).thenReturn(true).thenReturn(false);
        when(memberRepository.existsByPhone(any())).thenReturn(true);
        when(memberRepository.existsByUsername(any())).thenReturn(true);
        when(zoneRepository.existsById(any())).thenReturn(false);
        when(categoryRepository.countCategoryByIdList(any())).thenReturn(4L);

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(6)));
    }

    @DisplayName("토큰 재발급")
    @Test
    void reissue_token() throws Exception {

        // Given
        securityUserMockSetting();
        TokenDto tokenDto = createTestTokenDto();
        when(loginService.reissue(any())).thenReturn(tokenDto);

        // Given
        when(loginService.reissue(any())).thenReturn(createTestTokenDto());

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER_REFRESH_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("grantType").value("Bearer"))
                .andExpect(jsonPath("accessToken").value(tokenDto.getAccessToken()))
                .andExpect(jsonPath("refreshToken").value(tokenDto.getRefreshToken()))
                .andExpect(jsonPath("accessTokenExpiresIn").value(tokenDto.getAccessTokenExpiresIn()))
                .andExpect(jsonPath("refreshTokenExpiresIn").value(tokenDto.getRefreshTokenExpiresIn()))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT refresh token")
                        ),
                        responseFields(
                                fieldWithPath("grantType").type(STRING).description("토큰 타입"),
                                fieldWithPath("accessToken").type(STRING).description("JWT Access Token"),
                                fieldWithPath("refreshToken").type(STRING).description("JWT Refresh Token"),
                                fieldWithPath("accessTokenExpiresIn").type(NUMBER).description("JWT Access Token 만료 시간"),
                                fieldWithPath("refreshTokenExpiresIn").type(NUMBER).description("JWT Refresh Token 만료 시간")
                        )
                ));
    }

    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {

        // given
        securityUserMockSetting();

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.post(LOGOUT)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .header(REFRESH_TOKEN_HEADER, BEARER_REFRESH_TOKEN))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("RefreshToken").description("JWT refresh token"),
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        )
                ));
    }

    @DisplayName("로그아웃한 토큰으로 접근")
    @Test
    void logout_should_not_access_with_logout_token() throws Exception {
        // given
        when(logoutAccessTokenRedisRepository.existsById(any())).thenReturn(true);

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(LOGOUT)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .header(REFRESH_TOKEN_HEADER, BEARER_REFRESH_TOKEN))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value("KAKAO-401"));

    }

    @DisplayName("탈퇴한 회원의 토큰으로 접근")
    @Test
    void logout_should_not_access_with_withdraw_member() throws Exception {
        // given
        when(jwtTokenUtil.validateToken(any())).thenReturn(true);
        when(logoutAccessTokenRedisRepository.existsById(any())).thenReturn(false);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(LOGOUT)
                .contentType(MediaType.APPLICATION_JSON)
                .header("RefreshToken", "Bearer test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value("MEMBER-401"));

    }

    @DisplayName("request body 자체가 안들어올 경우")
    @Test
    void missing_request_body() throws Exception {

        mockMvc.perform(RestDocumentationRequestBuilders.post(LOGOUT)
                .contentType(MediaType.APPLICATION_JSON)
                .header(REFRESH_TOKEN_HEADER,BEARER_REFRESH_TOKEN)
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN))
                .andExpect(status().isUnauthorized())
        ;
    }

    @DisplayName("웹 디렉터 로그인")
    @Test
    void web_login_director() throws Exception{
        //given
        LoginResponse loginResponse = createLoginResponse();
        when(loginService.webLogin(any(),any())).thenReturn(loginResponse);

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/web/{authority}/login","director")
                .contentType(MediaType.APPLICATION_JSON)
                .header(KAKAO_TOKEN_HEADER, BEARER_KAKAO_TOKEN))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("KakaoAccessToken").description("카카오의 Access token")
                        ),
                        pathParameters(
                                parameterWithName("authority").description(generateLinkCode(DocUrl.PATH_AUTHORITY))
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("회원 Id 값"),
                                fieldWithPath("id").description("카카오에서 제공하는 id값"),
                                fieldWithPath("imageFileUrl").description("카카오에서 제공하는 프로필 이미지 url")
                        ).and(tokenDtoDescriptor())
                ));
    }



    @DisplayName("웹 어드민 로그인")
    @Test
    void web_login_admin() throws Exception{
        //given
        LoginResponse loginResponse = createLoginResponse();
        when(loginService.webLogin(any(),any())).thenReturn(loginResponse);

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/web/{authority}/login","admin")
                .contentType(MediaType.APPLICATION_JSON)
                .header(KAKAO_TOKEN_HEADER, BEARER_KAKAO_TOKEN))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName("KakaoAccessToken").description("카카오의 Access token")
                        ),
                        pathParameters(
                                parameterWithName("authority").description(generateLinkCode(DocUrl.PATH_AUTHORITY))
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("회원 Id 값"),
                                fieldWithPath("id").description("카카오에서 제공하는 id값"),
                                fieldWithPath("imageFileUrl").description("카카오에서 제공하는 프로필 이미지 url")
                        ).and(tokenDtoDescriptor())
                ));
    }

    private LoginResponse createLoginResponse() {
        return LoginResponse.of(
                1L,
                createTestTokenDto(),
                createKakaoProfile("profile_image_url")
        );
    }

    private TokenDto createTestTokenDto() {
        return TokenDto.of("Bearer", "newAccessToken",
                "newRefreshToken", 1000L, 10000L);
    }

    private KakaoProfile createKakaoProfile(String profile_image_url) {
        return KakaoProfile.builder()
                .id(1L)
                .properties(KakaoProfile.Properties.builder()
                        .profile_image(profile_image_url)
                        .build())
                .build();
    }


    private List<FieldDescriptor> tokenDtoDescriptor() {
        return new ArrayList<>(List.of(
                fieldWithPath("tokenDto.grantType").description("토큰 타입"),
                fieldWithPath("tokenDto.accessToken").description("Access 토큰"),
                fieldWithPath("tokenDto.refreshToken").description("Refresh 토큰"),
                fieldWithPath("tokenDto.accessTokenExpiresIn").description("Access 토큰 만료 기한"),
                fieldWithPath("tokenDto.refreshTokenExpiresIn").description("Refresh 토큰 만료 기한")
        ));
    }
}
