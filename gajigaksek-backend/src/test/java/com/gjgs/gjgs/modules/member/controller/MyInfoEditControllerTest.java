package com.gjgs.gjgs.modules.member.controller;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.dummy.AlarmDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.myinfo.*;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Alarm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.DocUrl.ALARM_TYPE;
import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.DocUrl.MEMBER_AUTHORITY;
import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MyInfoEditControllerTest extends RestDocsTestSupport {

    final String URL = "/api/v1/mypage";
    final String BEARER_ACCESS_TOKEN = "Bearer access_token";


    @BeforeEach
    void securitySetup() {
        securityUserMockSetting();
    }

    @DisplayName("인증은 되었으나 접근 권한이 없을 때")
    @Test
    void common_errors_should_require_authorization() throws Exception {

        // given
        String directorText = "change directorText";

        // when then
        mockMvc.perform(patch(URL + "/directors/director-text")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .content(createJson(directorText))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("마이페이지 편집창 조회")
    @Test
    void get_member_edit_page() throws Exception {
        // given
        MyPageEditResponse myPageEditResponse = createMyPageEditResponse();

        when(myInfoEditService.editMyPage()).thenReturn(myPageEditResponse);

        // when then
        mockMvc.perform(get(URL + "/edit")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        responseFields(
                                fieldWithPath("imageFileUrl").type(STRING).description("프로필 이미지 URL (S3)"),
                                fieldWithPath("nickname").type(STRING).description("닉네임"),
                                fieldWithPath("name").type(STRING).description("실명"),
                                fieldWithPath("phone").type(STRING).description("휴대폰 번호"),
                                fieldWithPath("memberCategoryId").type(ARRAY).description("취미 카테고리 ID 리스트"),
                                fieldWithPath("authority").type(STRING).description(generateLinkCode(MEMBER_AUTHORITY)),
                                fieldWithPath("profileText").type(STRING).description("자기소개"),
                                fieldWithPath("directorText").type(STRING).description("디렉터 자기소개"),
                                fieldWithPath("sex").type(STRING).description("성별"),
                                fieldWithPath("age").type(NUMBER).description("나이"),
                                fieldWithPath("zoneId").type(NUMBER).description("위치 ID")
                        )

                ));
    }


    @DisplayName("닉네임 수정")
    @Test
    void update_nickname() throws Exception {
        // given
        NicknameModifyRequest nicknameModifyRequest = createNicknameModifyRequest("nickname");

        // when then
        mockMvc.perform(patch(URL + "/nickname")
                .content(createJson(nicknameModifyRequest))
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("nickname").type(STRING).description("닉네임").attributes(field("constraints", "한글/영어(대,소문자)/숫자 조합으로 2 ~ 20자"))
                        )
                ));
    }

    @DisplayName("@valid 닉네임 수정 특수문자 실패 ")
    @Test
    void update_nickname_should_not_special_words() throws Exception {
        // given
        NicknameModifyRequest nicknameModifyRequest = createNicknameModifyRequest("hello!!");

        when(memberRepository.existsByUsername(any())).thenReturn(false);

        // when then
        mockMvc.perform(patch(URL + "/nickname")
                .content(objectMapper.writeValueAsString(nicknameModifyRequest))
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));
    }


    @DisplayName("닉네임 수정 1글자 실패")
    @Test
    void update_nickname_should_over_2_length() throws Exception {
        // given
        NicknameModifyRequest nicknameModifyRequest = createNicknameModifyRequest("h");

        when(memberRepository.existsByUsername(any())).thenReturn(false);

        // when then
        mockMvc.perform(patch(URL + "/nickname")
                .content(createJson(nicknameModifyRequest))
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));

    }

    @DisplayName("validator 닉네임 수정 - 중복")
    @Test
    void update_nickname_should_not_duplicate() throws Exception {
        // given
        NicknameModifyRequest nicknameModifyRequest = createNicknameModifyRequest("hello");
        when(memberRepository.existsByNickname(any())).thenReturn(true);

        // when then
        mockMvc.perform(patch(URL + "/nickname")
                .content(createJson(nicknameModifyRequest))
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].field").value("nickname"));
    }


    @DisplayName("전화번호 수정")
    @Test
    void update_phone() throws Exception {
        // given
        PhoneModifyRequest phoneModifyRequest = createPhoneModifyRequest("01012341234");

        // when then
        mockMvc.perform(patch(URL + "/phone")
                .content(createJson(phoneModifyRequest))
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("phone").type(STRING).description("휴대폰 번호").attributes(field("constraints", "숫자로 10 ~ 11자 가능"))
                        )
                ));
    }


    @DisplayName("@valid 전화번호 수정 / 10자 미만인 경우")
    @Test
    void update_phone_should_over_10_length() throws Exception {
        // given
        PhoneModifyRequest phoneModifyRequest = createPhoneModifyRequest("010111");

        // when then
        mockMvc.perform(patch(URL + "/phone")
                .content(createJson(phoneModifyRequest))
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));

    }

    @DisplayName("전화번호 수정 / 11자 초과인 경우")
    @Test
    void update_phone_should_not_over_11_length() throws Exception {
        // given
        PhoneModifyRequest phoneModifyRequest = createPhoneModifyRequest("010111123123123123123");

        // when then
        mockMvc.perform(patch(URL + "/phone")
                .content(createJson(phoneModifyRequest))
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));
    }

    @DisplayName("validator 전화번호 중복 에러")
    @Test
    void update_phone_should_not_duplicate() throws Exception {
        // given
        PhoneModifyRequest phoneModifyRequest = createPhoneModifyRequest("01011111111");

        when(memberRepository.existsByPhone(any())).thenReturn(true);

        // when then
        mockMvc.perform(patch(URL + "/phone")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .content(createJson(phoneModifyRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));

    }

    @DisplayName("카테고리 수정")
    @Test
    void update_category() throws Exception {
        // given
        CategoryModifyRequest categoryModifyRequest = createCategoryModifyRequest();
        when(categoryRepository.countCategoryByIdList(any())).thenReturn(3L);

        // when then
        mockMvc.perform(patch(URL + "/category")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .content(createJson(categoryModifyRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("categoryIdList").type(ARRAY).description("카테고리 Id 리스트").attributes(field("constraints", "변경할 카테고리 ID, 1 ~ 3개 선택 가능"))
                        )
                ));
    }


    @DisplayName("카테고리 수정 validator 에러")
    @Test
    void update_category_not_found() throws Exception {
        // given
        CategoryModifyRequest categoryModifyRequest = createCategoryModifyRequest();
        when(categoryRepository.countCategoryByIdList(any())).thenReturn(2L);

        // when then
        mockMvc.perform(patch(URL + "/category")
                .content(createJson(categoryModifyRequest))
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("categoryIdList"));

    }

    @DisplayName("자기소개 수정")
    @Test
    void update_profile() throws Exception {
        // given
        ProfileTextModifyRequest profileTextModifyRequest = createProfileTextModifyRequest("change profileText");


        // when then
        mockMvc.perform(patch(URL + "/profile-text")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .content(createJson(profileTextModifyRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("profileText").type(STRING).description("자기소개").attributes(field("constraints", "1000자 이하로 작성"))
                        )
                ));
    }


    @DisplayName("자기소개 수정 1000자 이상 valid")
    @Test
    void update_profile_should_not_over_1000_length() throws Exception {
        // given
        ProfileTextModifyRequest profileTextModifyRequest = createProfileTextModifyRequest("안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요아");

        // when then
        mockMvc.perform(patch(URL + "/profile-text")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .content(objectMapper.writeValueAsString(profileTextModifyRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));
    }

    @DisplayName("위치 수정")
    @Test
    void update_zone() throws Exception {
        // given
        ZoneModifyRequest zoneModifyRequest = createZonemodifyRequest();
        when(zoneRepository.existsById(any())).thenReturn(true);

        // when then
        mockMvc.perform(patch(URL + "/zone")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .content(createJson(zoneModifyRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("zoneId").type(NUMBER).description("지역 id")
                        )
                ));
    }


    @DisplayName("위치 수정 validator 에러")
    @Test
    void update_zone_should_exist_zone() throws Exception {
        // given
        ZoneModifyRequest zoneModifyRequest = createZonemodifyRequest();
        when(zoneRepository.existsById(any())).thenReturn(false);

        // when then
        mockMvc.perform(patch(URL + "/zone")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .content(createJson(zoneModifyRequest))
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("zoneId"));

    }

    @DisplayName("이미지 수정")
    @Test
    void update_image() throws Exception {

        // given
        MockMultipartFile file = createMultipartFile();


        // when then
        mockMvc.perform(fileUpload(URL + "/image")
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestPartBody("file")
                ));
    }


    @DisplayName("이벤트 알림 수신 여부")
    @Test
    void edit_my_event_alarm() throws Exception {
        // given
        AlarmEditRequest alarmEditRequest = AlarmDummy.createAlarmEditRequest(false, Alarm.EVENT);

        // when then
        mockMvc.perform(post(URL + "/alarm")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .content(createJson(alarmEditRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("active").description("알림 수신 동의 여부"),
                                fieldWithPath("type").description(generateLinkCode(ALARM_TYPE))
                        )
                ));
    }


    private NicknameModifyRequest createNicknameModifyRequest(String s) {
        return NicknameModifyRequest.builder()
                .nickname(s)
                .build();
    }

    private PhoneModifyRequest createPhoneModifyRequest(String s) {
        return PhoneModifyRequest.builder()
                .phone(s)
                .build();
    }

    private CategoryModifyRequest createCategoryModifyRequest() {
        List<Long> categoryId = Arrays.asList(1L, 2L, 3L);
        return CategoryModifyRequest.builder().categoryIdList(categoryId).build();
    }

    private ZoneModifyRequest createZonemodifyRequest() {
        return ZoneModifyRequest.builder()
                .zoneId(1L)
                .build();
    }

    private ProfileTextModifyRequest createProfileTextModifyRequest(String s) {
        String profileText = s;
        return ProfileTextModifyRequest.from(profileText);
    }

    private MockMultipartFile createMultipartFile() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        return file;
    }

    private MyPageEditResponse createMyPageEditResponse() {
        Member member = MemberDummy.createTestMember();
        MyPageEditResponse myPageEditResponse = MyPageEditResponse.from(member);
        myPageEditResponse.setZoneId(1L);
        return myPageEditResponse;
    }


}
