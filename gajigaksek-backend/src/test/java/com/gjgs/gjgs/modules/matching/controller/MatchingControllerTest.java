package com.gjgs.gjgs.modules.matching.controller;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.dummy.MatchingDummy;
import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.matching.dto.MatchingStatusResponse;
import com.gjgs.gjgs.modules.matching.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.DocUrl.*;
import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MatchingControllerTest extends RestDocsTestSupport {


    final String TOKEN = "Bearer access-token";
    final String URL = "/api/v1/matching";
    MatchingRequest matchingRequest;

    @BeforeEach
    void setUserMockSetting(){
        securityUserMockSetting();
        matchingRequest = MatchingDummy.createMatchingRequest();
        when(zoneRepository.existsById(any())).thenReturn(true);
        when(categoryRepository.existsById(any())).thenReturn(true);
    }

    @DisplayName("매칭 성공")
    @Test
    void success_matching() throws Exception {

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .content(createJson(matchingRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("categoryId").type(NUMBER).description("취미 카테고리 ID"),
                                fieldWithPath("dayType").type(STRING).description(generateLinkCode(DAY_TYPE)),
                                fieldWithPath("timeType").type(STRING).description(generateLinkCode(TIME_TYPE)),
                                fieldWithPath("preferMemberCount").type(NUMBER).description("선호하는 팀 구성 인원")
                        )
                ))
        ;
    }

    @DisplayName("존재하지 않는 zoneId로 매칭 시도")
    @Test
    void matching_should_exist_zone_id() throws Exception {
        //given
        when(zoneRepository.existsById(any())).thenReturn(false);
        when(categoryRepository.existsById(any())).thenReturn(true);


        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .content(objectMapper.writeValueAsString(matchingRequest))
                .content(createJson(matchingRequest))
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].field").value("zoneId"))
                .andExpect(jsonPath("errors[0].reason").value("존재하지 않는 zoneId 입니다."));
    }

    @DisplayName("존재하지 않는 categoryId로 매칭 시도")
    @Test
    void matching_should_exist_category_id() throws Exception {
        //given
        when(categoryRepository.existsById(any())).thenReturn(false);
        when(zoneRepository.existsById(any())).thenReturn(true);

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .content(createJson(matchingRequest))
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].field").value("categoryId"))
                .andExpect(jsonPath("errors[0].reason").value("존재하지 않는 categoryId 입니다."));
    }

    @DisplayName("존재하지 않는 timeType으로 매칭 시도")
    @Test
    void matchingForm_validation_timeType_fail() throws Exception {
        //given
        MatchingRequest matchingRequest = MatchingRequest.builder()
                .zoneId(1L)
                .categoryId(1L)
                .dayType("MON|TUE")
                .timeType("Hello")
                .preferMemberCount(4)
                .build();

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .content(createJson(matchingRequest))
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].field").value("timeType"))
                .andExpect(jsonPath("errors[0].reason").value("Hello" + " 은 올바른 시간이 아닙니다."));
    }

    @DisplayName("존재하지 않는 요일로 매칭 시도")
    @Test
    void matchingForm_validation_dayType_fail() throws Exception {
        //given
        MatchingRequest matchingRequest = MatchingRequest.builder()
                .zoneId(1L)
                .categoryId(1L)
                .dayType("Hello")
                .timeType("AFTERNOON")
                .preferMemberCount(4)
                .build();


        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .content(createJson(matchingRequest))
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].field").value("dayType"))
                .andExpect(jsonPath("errors[0].reason").value("Hello" + " 은 올바른 요일이 아닙니다."));
    }

    @DisplayName("범위가 맞지 않는 매칭 인원으로 매칭 시도")
    @Test
    void matchingForm_validation_preferMemberCount_fail() throws Exception {
        //given
        MatchingRequest matchingRequest = MatchingRequest.builder()
                .zoneId(1L)
                .categoryId(1L)
                .dayType("Hello")
                .timeType("AFTERNOON")
                .preferMemberCount(5)
                .build();

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .content(createJson(matchingRequest))
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].field").value("preferMemberCount"))
                .andExpect(jsonPath("errors[0].reason").value("매칭 인원의 최대값은 4명입니다."));
    }

    @DisplayName("모든 valid fail")
    @Test
    void matchingForm_valid_all_fail() throws Exception {
        //given
        MatchingRequest matchingRequest = MatchingRequest.builder()
                .zoneId(1L)
                .categoryId(1L)
                .dayType("Hello")
                .timeType("Hello")
                .preferMemberCount(5)
                .build();

        when(categoryRepository.existsById(any())).thenReturn(false);
        when(zoneRepository.existsById(any())).thenReturn(false);

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .content(createJson(matchingRequest))
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(5)));
    }

    @DisplayName("매칭 취소")
    @Test
    void cancel_matching() throws Exception{

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        )
                ))
        ;

    }

    @DisplayName("매칭 상태 확인")
    @Test
    void get_matching_status() throws Exception{

        when(matchingService.status()).thenReturn(MatchingStatusResponse.from(Status.MATCHING));

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        responseFields(
                                fieldWithPath("status").description(generateLinkCode(MATCHING_STATUS))
                        )
                ))
        ;

    }


}
