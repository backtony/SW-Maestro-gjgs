package com.gjgs.gjgs.modules.notification.controller;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.dummy.NotificationDummy;
import com.gjgs.gjgs.modules.notification.dto.NotificationCreateRequest;
import com.gjgs.gjgs.modules.notification.enums.TargetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.List;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.DocUrl.TARGET_TYPE;
import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerTest extends RestDocsTestSupport {


    final String TOKEN = "Bearer AccessToken";
    final String URL = "/api/v1/notification";

    @DisplayName("admin 으로 알림 보내기")
    @Test
    void send_notification() throws Exception{
        // given
        securityAdminMockSetting();
        NotificationCreateRequest notificationCreateRequest = createNotificationCreateRequest();

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .content(objectMapper.writeValueAsString(notificationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("title").type(STRING).description("알림 제목").attributes(field("constraints", "Not Blank")),
                                fieldWithPath("message").type(STRING).description("알림 메세지").attributes(field("constraints", "Not Blank")),
                                fieldWithPath("targetType").type(STRING).description(generateLinkCode(TARGET_TYPE)).attributes(field("constraints", "ALL or SELECT")),
                                fieldWithPath("memberIdList[]").type(ARRAY).description("보낼 멤버들의 ID").optional(),
                                fieldWithPath("teamId").type(NUMBER).description("보낼 팀 ID").optional()
                        )
                ))
        ;
    }



    @DisplayName("admin 으로 알림 보내기 / Request 제약 조건을 지키지 못할 경우")
    @Test
    void send_notification_should_follow_constraints() throws Exception{
        // given
        securityAdminMockSetting();
        NotificationCreateRequest request = NotificationCreateRequest.builder().build();

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("권한 없이 알림 보내기")
    @Test
    void send_notification_forbidden_exception() throws Exception{
        // given
        NotificationCreateRequest notificationCreateRequest = createNotificationCreateRequest();

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .content(objectMapper.writeValueAsString(notificationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
        ;
    }

    @DisplayName("알림 읽기")
    @Test
    void read_notification_read() throws Exception{
        // given
        securityUserMockSetting();

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL+"/{uuid}", "test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("uuid").description("알림에 해당하는 UUID")
                        )
                ))
        ;
    }

    @DisplayName("비로그인 알림 읽기")
    @Test
    void read_notification_unauthorized_exception() throws Exception{

        // when then
        mockMvc.perform(post(URL+"/{uuid}", "test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private NotificationCreateRequest createNotificationCreateRequest() {
        String title = "title";
        String message = "message";
        TargetType targetType = TargetType.ALL;
        List<Long> memberIdList = List.of(1L, 2L);
        return NotificationDummy.createNotificationForm(title, message, targetType, memberIdList);
    }
}
