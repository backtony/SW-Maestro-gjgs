package com.gjgs.gjgs.modules.question.controller;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.exception.member.InvalidAuthorityException;
import com.gjgs.gjgs.modules.member.dto.mypage.QuestionMainTextModifyRequest;
import com.gjgs.gjgs.modules.question.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.DocUrl.QUESTION_RESPONSE;
import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class QuestionControllerTest extends RestDocsTestSupport {

    private final String URL = "/api/v1/questions";

    @WithMockUser
    @Test
    @DisplayName("문의글 생성")
    void create_question() throws Exception {

        // given
        securityUserMockSetting();
        String body = createJson(createQuestionRequest());
        when(questionService.createQuestion(any()))
                .thenReturn(QuestionResponse.builder()
                        .lectureId(1L)
                        .questionId(1L)
                        .result(QuestionResult.QUESTION.getDescription())
                        .build());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.lectureId", is(1)))
                .andExpect(jsonPath("$.questionId", is(1)))
                .andExpect(jsonPath("$.result", is(QuestionResult.QUESTION.getDescription())))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("lectureId").type(NUMBER).description("질문글 작성할 클래스 ID").attributes(field("constraints", "Not Null")),
                                fieldWithPath("questionForm.mainText").type(STRING).description("문의할 내용").attributes(field("constraints", "Not Null"))
                        ),
                        responseFields(
                                fieldWithPath("lectureId").type(NUMBER).description("질문한 클래스 ID"),
                                fieldWithPath("questionId").type(NUMBER).description("질문글 ID"),
                                fieldWithPath("result").description(generateLinkCode(QUESTION_RESPONSE))
                        )
                ))
        ;
    }

    @Test
    @DisplayName("문의글 생성 / 질문할 클래스 ID, 질문글을 작성하지 않을 경우")
    void create_question_should_require_lecture_id_and_main_text() throws Exception {

        // given
        securityUserMockSetting();
        String body = createJson(QuestionRequest.builder().build());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문의글 상세 조회")
    void get_question() throws Exception {

        // given
        when(questionService.getQuestion(any()))
                .thenReturn(QuestionDetailResponse.builder()
                        .lectureId(1L)
                        .questionId(1L)
                        .questionStatus("COMPLETE")
                        .questionDetail(QuestionDetailResponse.QuestionDetail.builder()
                                .questionMainText("test")
                                .questionerNickname("test")
                                .questionerProfileImageUrl("test")
                                .questionDate(LocalDateTime.now())
                                .build())
                        .answerDetail(QuestionDetailResponse.AnswerDetail.builder()
                                .directorNickname("test")
                                .replyText("test")
                                .directorProfileImageUrl("test")
                                .build())
                        .build());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL + "/{questionId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.lectureId", is(1)))
                .andExpect(jsonPath("$.questionId", is(1)))
                .andExpect(jsonPath("$.questionStatus", is("COMPLETE")))
                .andExpect(jsonPath("$.questionDetail.questionerNickname", is("test")))
                .andExpect(jsonPath("$.questionDetail.questionMainText", is("test")))
                .andExpect(jsonPath("$.questionDetail.questionerProfileImageUrl", is("test")))
                .andExpect(jsonPath("$.answerDetail.directorProfileImageUrl", is("test")))
                .andExpect(jsonPath("$.answerDetail.directorNickname", is("test")))
                .andExpect(jsonPath("$.answerDetail.replyText", is("test")))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("questionId").description("조회할 질문글 ID")
                        ),
                        responseFields(
                                fieldWithPath("lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("questionId").type(NUMBER).description("질문자 ID"),
                                fieldWithPath("questionStatus").type(STRING).description("질문글에 대한 답변 상태"),
                                fieldWithPath("questionDetail.questionerNickname").type(STRING).description("질문자 닉네임"),
                                fieldWithPath("questionDetail.questionMainText").type(STRING).description("질문글"),
                                fieldWithPath("questionDetail.questionerProfileImageUrl").type(STRING).description("질문자 프로필 URL"),
                                fieldWithPath("questionDetail.questionDate").type(STRING).description("질문한 날짜"),
                                fieldWithPath("answerDetail.directorProfileImageUrl").type(STRING).description("디렉터 프로필 URL"),
                                fieldWithPath("answerDetail.directorNickname").type(STRING).description("디렉터 닉네임"),
                                fieldWithPath("answerDetail.replyText").type(STRING).description("답변글")
                        )
                ));
    }

    @Test
    @DisplayName("문의글 답글 및 수정")
    void reply_question() throws Exception {

        // given
        securityDirectorMockSetting();
        String body = createJson(createAnswerRequest());
        when(questionService.putAnswer(any(), any()))
                .thenReturn(QuestionResponse
                        .createAnswer(1L, 1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.put(URL + "/{questionId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.lectureId", is(1)))
                .andExpect(jsonPath("$.questionId", is(1)))
                .andExpect(jsonPath("$.result", is(QuestionResult.REPLY.getDescription())))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("questionId").description("답글을 남길 질문글 ID")
                        ),
                        requestFields(
                                fieldWithPath("replyText").type(STRING).description("답글").attributes(field("constraints", "10 ~ 1000자로 작성"))
                        ),
                        responseFields(
                                fieldWithPath("lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("questionId").type(NUMBER).description("질문글 ID"),
                                fieldWithPath("result").type(STRING).description("API 수행 결과")
                        )
                ));
    }

    @Test
    @DisplayName("문의글 답글 및 수정, 디렉터 권한이 없을 경우 예외 발생")
    void reply_answer_should_need_director_authorization() throws Exception {

        // given
        String body = createJson(createAnswerRequest());
        when(questionService.putAnswer(any(), any()))
                .thenThrow(new InvalidAuthorityException());

        // when, then
        mockMvc.perform(put(URL + "/{questionId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding("utf-8"))
                .andExpect(status().isForbidden());
    }

    private AnswerRequest createAnswerRequest() {
        return AnswerRequest.builder()
                .replyText("테스트답장테스트답장!!")
                .build();
    }

    private QuestionRequest createQuestionRequest() {
        return QuestionRequest.builder()
                .lectureId(1L)
                .questionForm(QuestionMainTextModifyRequest.builder()
                        .mainText("테스트")
                        .build())
                .build();
    }
}