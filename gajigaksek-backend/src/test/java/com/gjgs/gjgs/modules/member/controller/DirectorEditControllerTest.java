package com.gjgs.gjgs.modules.member.controller;


import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.dto.myinfo.DirectorTextModifyRequest;
import com.gjgs.gjgs.modules.member.dto.mypage.DirectorMyPageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DirectorEditControllerTest extends RestDocsTestSupport {

    final String URL = "/api/v1/mypage";
    final String BEARER_ACCESS_TOKEN = "Bearer access_token";

    @DisplayName("디렉터 편집 페이지")
    @Test
    void get_director_edit() throws Exception {
        // given
        securityDirectorMockSetting();

        DirectorMyPageResponse directorMyPageResponse
                = DirectorMyPageResponse.from(MemberDummy.createTestMember());
        when(directorEditService.getDirectorEditPage()).thenReturn(directorMyPageResponse);

        // when then
        mockMvc.perform(get(URL+"/directors/edit")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("imageFileUrl").value(directorMyPageResponse.getImageFileUrl()))
                .andExpect(jsonPath("nickname").value(directorMyPageResponse.getNickname()))
                .andExpect(jsonPath("name").value(directorMyPageResponse.getName()))
                .andExpect(jsonPath("phone").value(directorMyPageResponse.getPhone()))
                .andExpect(jsonPath("directorText").value(directorMyPageResponse.getDirectorText()))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        responseFields(
                                fieldWithPath("imageFileUrl").type(STRING).description("프로필 URL"),
                                fieldWithPath("nickname").type(STRING).description("닉네임"),
                                fieldWithPath("name").type(STRING).description("성함"),
                                fieldWithPath("phone").type(STRING).description("휴대폰 번호"),
                                fieldWithPath("directorText").type(STRING).description("디렉터 소개글")
                        )
                ));
    }

    @DisplayName("디렉터 소개 수정")
    @Test
    void update_director_text() throws Exception  {
        // given
        securityDirectorMockSetting();

        DirectorTextModifyRequest directorTextModifyRequest = createDirectorTextModifyRequest("change directorText");

        // when then
        mockMvc.perform(patch(URL+"/directors/director-text")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .content(createJson(directorTextModifyRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("directorText").type(STRING).description("디렉터 자기소개").attributes(field("constraints", "1000자 이하로 작성"))
                        )
                ));
    }


    @DisplayName("디렉터소개 수정 1000자 이상 valid")
    @Test
    void update_director_text_should_not_over_1000_length() throws Exception {
        // given
        securityDirectorMockSetting();
        DirectorTextModifyRequest directorTextModifyRequest = createDirectorTextModifyRequest("안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요아");

        // when then
        mockMvc.perform(patch(URL+"/directors/director-text")
                .header(HttpHeaders.AUTHORIZATION, BEARER_ACCESS_TOKEN)
                .content(objectMapper.writeValueAsString(directorTextModifyRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));

    }

    @DisplayName("Authorization 헤더 없이 접근")
    @Test
    void common_errors_should_require_header() throws Exception {
        // given
        DirectorTextModifyRequest directorTextModifyRequest = createDirectorTextModifyRequest("change directorText");

        // when then
        mockMvc.perform(patch(URL+"/directors/director-text")
                .content(createJson(directorTextModifyRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    private DirectorTextModifyRequest createDirectorTextModifyRequest(String s) {
        String directorText = s;
        return DirectorTextModifyRequest.from(directorText);
    }
}
