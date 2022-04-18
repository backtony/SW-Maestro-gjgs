package com.gjgs.gjgs.document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gjgs.gjgs.document.utils.CustomResponseFieldsSnippet;
import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        value = {CommonDocController.class}
)
class CommonDocControllerTest extends RestDocsTestSupport {



    @WithMockUser
    @DisplayName("overview 에러 샘플")
    @Test
    public void error_sample() throws Exception {
        CommonDocController.SampleRequest sampleRequest = new CommonDocController.SampleRequest("name","hhh.naver");
        mockMvc.perform(
                post("/test/error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest))
        )
                .andExpect(status().isBadRequest())
                .andDo(restDocs.document(
                            responseFields(
                                    errorDescriptor()
                            )
                        )
                )
        ;
    }

    @WithMockUser
    @DisplayName("문서화에 사용할 enum 값들")
    @Test
    public void enums() throws Exception {
        ResultActions result = this.mockMvc.perform(
                get("/test/enums")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        MvcResult mvcResult = result.andReturn();
        EnumDocs enumDocs = getData(mvcResult);

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        customResponseFields("custom-response", beneathPath("data.bulletinAge").withSubsectionId("bulletinAge"),
                                attributes(key("title").value("bulletinAge")),
                                enumConvertFieldDescriptor((enumDocs.getBulletinAge()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.dayType").withSubsectionId("dayType"),
                                attributes(key("title").value("dayType")),
                                enumConvertFieldDescriptor((enumDocs.getDayType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.timeType").withSubsectionId("timeType"),
                                attributes(key("title").value("timeType")),
                                enumConvertFieldDescriptor((enumDocs.getTimeType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.lectureStatus").withSubsectionId("lectureStatus"),
                                attributes(key("title").value("lectureStatus")),
                                enumConvertFieldDescriptor((enumDocs.getLectureStatus()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.scheduleStatus").withSubsectionId("scheduleStatus"),
                                attributes(key("title").value("scheduleStatus")),
                                enumConvertFieldDescriptor((enumDocs.getScheduleStatus()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.authority").withSubsectionId("authority"),
                                attributes(key("title").value("authority")),
                                enumConvertFieldDescriptor((enumDocs.getAuthority()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.sex").withSubsectionId("sex"),
                                attributes(key("title").value("sex")),
                                enumConvertFieldDescriptor((enumDocs.getSex()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.noticeType").withSubsectionId("noticeType"),
                                attributes(key("title").value("noticeType")),
                                enumConvertFieldDescriptor((enumDocs.getNoticeType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.notificationType").withSubsectionId("notificationType"),
                                attributes(key("title").value("notificationType")),
                                enumConvertFieldDescriptor((enumDocs.getNotificationType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.pushMessage").withSubsectionId("pushMessage"),
                                attributes(key("title").value("pushMessage")),
                                enumConvertFieldDescriptor((enumDocs.getPushMessage()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.targetType").withSubsectionId("targetType"),
                                attributes(key("title").value("targetType")),
                                enumConvertFieldDescriptor((enumDocs.getTargetType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.questionStatus").withSubsectionId("questionStatus"),
                                attributes(key("title").value("questionStatus")),
                                enumConvertFieldDescriptor((enumDocs.getQuestionStatus()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.rewardType").withSubsectionId("rewardType"),
                                attributes(key("title").value("rewardType")),
                                enumConvertFieldDescriptor((enumDocs.getRewardType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.teamAge").withSubsectionId("teamAge"),
                                attributes(key("title").value("teamAge")),
                                enumConvertFieldDescriptor((enumDocs.getTeamAge()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.teamDayType").withSubsectionId("teamDayType"),
                                attributes(key("title").value("teamDayType")),
                                enumConvertFieldDescriptor((enumDocs.getTeamDayType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.teamTimeType").withSubsectionId("teamTimeType"),
                                attributes(key("title").value("teamTimeType")),
                                enumConvertFieldDescriptor((enumDocs.getTeamTimeType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.decideLectureType").withSubsectionId("decideLectureType"),
                                attributes(key("title").value("decideLectureType")),
                                enumConvertFieldDescriptor((enumDocs.getDecideLectureType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.searchPriceCondition").withSubsectionId("searchPriceCondition"),
                                attributes(key("title").value("searchPriceCondition")),
                                enumConvertFieldDescriptor((enumDocs.getSearchPriceCondition()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.orderStatus").withSubsectionId("orderStatus"),
                                attributes(key("title").value("orderStatus")),
                                enumConvertFieldDescriptor((enumDocs.getOrderStatus()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.getLectureType").withSubsectionId("getLectureType"),
                                attributes(key("title").value("getLectureType")),
                                enumConvertFieldDescriptor((enumDocs.getGetLectureType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.getScheduleType").withSubsectionId("getScheduleType"),
                                attributes(key("title").value("getScheduleType")),
                                enumConvertFieldDescriptor((enumDocs.getGetScheduleType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.scheduleResult").withSubsectionId("scheduleResult"),
                                attributes(key("title").value("scheduleResult")),
                                enumConvertFieldDescriptor((enumDocs.getScheduleResult()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.pathAuthority").withSubsectionId("pathAuthority"),
                                attributes(key("title").value("Authority")),
                                enumConvertFieldDescriptor((enumDocs.getPathAuthority()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.createLectureStep").withSubsectionId("createLectureStep"),
                                attributes(key("title").value("createLectureStep")),
                                enumConvertFieldDescriptor((enumDocs.getCreateLectureStep()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.lectureSaveDeleteResponse").withSubsectionId("lectureSaveDeleteResponse"),
                                attributes(key("title").value("lectureSaveDeleteResponse")),
                                enumConvertFieldDescriptor((enumDocs.getLectureSaveDeleteResponse()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.payType").withSubsectionId("payType"),
                                attributes(key("title").value("payType")),
                                enumConvertFieldDescriptor((enumDocs.getPayType()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.questionResponse").withSubsectionId("questionResponse"),
                                attributes(key("title").value("questionResponse")),
                                enumConvertFieldDescriptor((enumDocs.getQuestionResponse()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.currentLectureStatus").withSubsectionId("currentLectureStatus"),
                                attributes(key("title").value("currentLectureStatus")),
                                enumConvertFieldDescriptor((enumDocs.getCurrentLectureStatus()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.matchingStatus").withSubsectionId("matchingStatus"),
                                attributes(key("title").value("matchingStatus")),
                                enumConvertFieldDescriptor((enumDocs.getMatchingStatus()))
                        ),
                        customResponseFields("custom-response", beneathPath("data.alarmType").withSubsectionId("alarmType"),
                                attributes(key("title").value("alarmType")),
                                enumConvertFieldDescriptor((enumDocs.getAlarmType()))
                        )
                ));
    }

    // 커스텀 템플릿 사용을 위한 함수
    public static CustomResponseFieldsSnippet customResponseFields
                                (String type,
                                 PayloadSubsectionExtractor<?> subsectionExtractor,
                                 Map<String, Object> attributes, FieldDescriptor... descriptors) {
        return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes
                , true);
    }

    private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
        return enumValues.entrySet().stream()
                .map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
                .toArray(FieldDescriptor[]::new);
    }

    // mvc result 데이터 파싱
    private EnumDocs getData(MvcResult result) throws IOException {
        ApiResponseDto<EnumDocs> apiResponseDto = objectMapper
                                                .readValue(result.getResponse().getContentAsByteArray(),
                                                new TypeReference<ApiResponseDto<EnumDocs>>() {}
                                                );
        return apiResponseDto.getData();
    }


    @DisplayName("Header 테스트")
    @Test
    void header_sample() throws Exception {

        mockMvc.perform(RestDocumentationRequestBuilders.get("/health")
                .header(HttpHeaders.AUTHORIZATION, "Bearer example"))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT 토큰")
                        )
                ));
    }

    @DisplayName("USER 권한이 없을 경우")
    @Test
    void have_not_user_authorization_sample() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/user"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("DIRECTOR 권한이 없을 경우")
    @Test
    void have_not_director_authorization_sample() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/director"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("ADMIN 권한이 없을 경우")
    @Test
    void have_not_admin_authorization_sample() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/test/admin"))
                .andExpect(status().isForbidden());
    }
}
