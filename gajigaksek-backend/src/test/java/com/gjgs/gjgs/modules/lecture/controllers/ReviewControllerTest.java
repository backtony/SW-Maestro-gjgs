package com.gjgs.gjgs.modules.lecture.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewReplyRequest;
import com.gjgs.gjgs.modules.lecture.dtos.review.CreateReviewRequest;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.utils.querydsl.RepositorySliceHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.List;

import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest extends RestDocsTestSupport {

    private final String REVIEW_URL = "/api/v1/reviews";


    @Test
    @DisplayName("리뷰 작성하기 / 클래스를 선택하지 않고 후기와 별점을 작성하지 않을 경우")
    void write_review_should_require_constraint() throws Exception {

        // given
        securityUserMockSetting();
        CreateReviewRequest request = CreateReviewRequest.builder().build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(REVIEW_URL)
                .file(getMockMultipartJson(request))
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(4)));
    }

    @Test
    @DisplayName("리뷰 작성하기")
    void write_review() throws Exception {

        // given
        securityUserMockSetting();
        CreateReviewRequest request = CreateReviewRequest.builder()
                .lectureId(1L).text("testtesttest").score(5).scheduleId(1L)
                .build();
        MockMultipartFile json = getMockMultipartJson(request);
        MockMultipartFile file = createMockFile();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(REVIEW_URL)
                .file(file)
                .file(json)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestParts(
                                partWithName("request").description("리뷰 저장 요청 JSON"),
                                partWithName("file").description("리뷰를 남길 사진").optional().attributes(field("constraints", "1장만 전송 가능"))
                        ),
                        requestPartFields("request",
                                fieldWithPath("lectureId").type(NUMBER).description("들었던 클래스 ID").attributes(field("constraints", "Not Null")),
                                fieldWithPath("scheduleId").type(NUMBER).description("들었던 스케줄 ID").attributes(field("constraints", "Not Null")),
                                fieldWithPath("score").type(NUMBER).description("후기 점수(별점)").attributes(field("constraints", "1 ~ 5 사이 값만 가능")),
                                fieldWithPath("text").type(STRING).description("후기 내용").attributes(field("constraints", "최소 10자 이상"))
                        )
                ));
    }

    @Test
    @DisplayName("리뷰 답글 작성하기 / 답글을 입력하지 않았을 경우")
    void reply_review_should_write_reply() throws Exception {

        // given
        securityDirectorMockSetting();
        String json = createJsonBody(CreateReviewReplyRequest.builder().build());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(REVIEW_URL + "/{reviewId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    @DisplayName("리뷰 답글 작성하기")
    void reply_review() throws Exception {

        // given
        securityDirectorMockSetting();
        String json = createJsonBody(CreateReviewReplyRequest.builder()
                .replyText("testtesttest")
                .build());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(REVIEW_URL + "/{reviewId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("reviewId").description("답글을 남길 리뷰 ID")
                        ),
                        requestFields(
                                fieldWithPath("replyText").description("디렉터가 리뷰에 남길 답글")
                        )
                ));
    }

    @Test
    @DisplayName("디렉터가 진행중인 클래스의 모든 리뷰 가져오기")
    void get_director_reviews() throws Exception {

        // given
        Mockito.when(reviewService.getDirectorsReviews(any(), any()))
                .thenReturn(createDirectorReviews());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(REVIEW_URL + "/directors/{directorId}", 1))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(parameterWithName("directorId").description("리뷰들을 조회할 디렉터 ID")),
                        responseFields(
                                fieldWithPath("content[].reviewId").type(NUMBER).description("리뷰 ID"),
                                fieldWithPath("content[].reviewImageFileUrl").type(STRING).description("유저가 남긴 리뷰 사진").optional(),
                                fieldWithPath("content[].text").type(STRING).description("유저의 후기"),
                                fieldWithPath("content[].replyText").type(STRING).description("디렉터의 답글"),
                                fieldWithPath("content[].score").type(NUMBER).description("유저가 남긴 점수"),
                                fieldWithPath("content[].nickname").type(STRING).description("유저 닉네임"),
                                fieldWithPath("content[].profileImageFileUrl").type(STRING).description("유저의 프로필 이미지 URL")
                        ).and(sliceDescriptor())
                ));
    }

    private Slice<ReviewResponse> createDirectorReviews() {
        List<ReviewResponse> content = List.of(
                ReviewResponse.builder()
                        .reviewId(1L)
                        .reviewImageFileUrl("test")
                        .text("test")
                        .replyText("test")
                        .score(4)
                        .nickname("test")
                        .profileImageFileUrl("test")
                        .build()
        );
        return RepositorySliceHelper.toSlice(content, PageRequest.of(0, 20));
    }

    private MockMultipartFile createMockFile() {
        return new MockMultipartFile(
                "file",
                "hello.jpeg",
                IMAGE_JPEG_VALUE,
                "<<image>>".getBytes());
    }

    private MockMultipartFile getMockMultipartJson(Object dto) throws JsonProcessingException {
        return new MockMultipartFile("request",
                "",
                APPLICATION_JSON_VALUE,
                createJsonBody(dto).getBytes());
    }

    private String createJsonBody(Object request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }
}
