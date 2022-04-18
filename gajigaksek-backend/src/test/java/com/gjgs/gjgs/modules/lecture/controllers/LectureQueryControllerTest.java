package com.gjgs.gjgs.modules.lecture.controllers;

import com.gjgs.gjgs.document.utils.DocumentLinkGenerator;
import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.dummy.LectureDtoDummy;
import com.gjgs.gjgs.modules.lecture.dtos.LectureDetailResponse;
import com.gjgs.gjgs.modules.lecture.dtos.LectureQuestionsResponse;
import com.gjgs.gjgs.modules.lecture.dtos.review.ReviewResponse;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import com.gjgs.gjgs.modules.team.enums.Age;
import com.gjgs.gjgs.modules.utils.querydsl.RepositorySliceHelper;
import org.elasticsearch.ElasticsearchStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static org.elasticsearch.rest.RestStatus.BAD_REQUEST;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LectureQueryControllerTest extends RestDocsTestSupport {

    @Test
    @DisplayName("클래스 검색")
    void get_lectures() throws Exception {

        // given
        when(lectureService.searchLectures(any(), any()))
                .thenReturn(createSearchResponse());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/lectures?categoryIdList=1,2,3&keyword=test&zoneId=1&searchPriceCondition=LOWER_EQUAL_FIVE&createdDate,desc&reviewCount,desc&score,desc&clickCount,desc")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.pageable.offset", is(0)))
                .andExpect(jsonPath("$.pageable.pageSize", is(10)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.first", is(true)))
                .andDo(restDocs.document(
                        requestParameters(
                                parameterWithName("searchPriceCondition").description(generateLinkCode(DocumentLinkGenerator.DocUrl.SEARCH_PRICE_CONDITION)),
                                parameterWithName("categoryIdList").description("검색할 카테고리 ID 복수 선택 가능"),
                                parameterWithName("keyword").description("검색어, 공백 불가능"),
                                parameterWithName("zoneId").description("지역 ID, 단일 선택"),
                                parameterWithName("createdDate,desc").description("최신순"),
                                parameterWithName("score,desc").description("리뷰 평점 순"),
                                parameterWithName("reviewCount,desc").description("리뷰 많은 순"),
                                parameterWithName("clickCount,desc").description("인기순")
                        ),
                        responseFields(
                                fieldWithPath("content[].lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("content[].onlyGjgs").type(BOOLEAN).description("온리 가지각색 여부"),
                                fieldWithPath("content[].myFavorite").type(BOOLEAN).description("내가 찜 표시한 클래스 여부"),
                                fieldWithPath("content[].imageUrl").type(STRING).description("파일 URL"),
                                fieldWithPath("content[].title").type(STRING).description("클래스 제목"),
                                fieldWithPath("content[].zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("content[].priceOne").type(NUMBER).description("클래스 1명 결제 가격"),
                                fieldWithPath("content[].priceTwo").type(NUMBER).description("클래스 2명 결제 가격"),
                                fieldWithPath("content[].priceThree").type(NUMBER).description("클래스 3명 결제 가격"),
                                fieldWithPath("content[].priceFour").type(NUMBER).description("클래스 4명 결제 가격")
                        ).and(pageDescriptor())
                ));
    }

    @Test
    @DisplayName("추천 클래스(메인 페이지, 클래스 상세 조회와 동일)")
    void get_recommend_lectures() throws Exception {

        // given
        when(lectureService.searchLectures(any(), any()))
                .thenReturn(createRecommendSearchResponse());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/lectures?categoryIdList=1,2,3&reviewCount,desc&score,desc&clickCount,desc&createdDate,desc")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.first", is(true)))
                .andDo(restDocs.document(
                        requestParameters(
                                parameterWithName("categoryIdList").description("추천받을 취미의 카테고리(복수는 메인 페이지, 단수는 클래스 상세 조회 페이지)"),
                                parameterWithName("score,desc").description("리뷰 평점 순"),
                                parameterWithName("clickCount,desc").description("인기순"),
                                parameterWithName("reviewCount,desc").description("리뷰 많은 순"),
                                parameterWithName("createdDate,desc").description("최신순")
                        ),
                        responseFields(
                                fieldWithPath("content[].lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("content[].onlyGjgs").type(BOOLEAN).description("온리 가지각색 여부"),
                                fieldWithPath("content[].myFavorite").type(BOOLEAN).description("내가 찜 표시한 클래스 여부"),
                                fieldWithPath("content[].imageUrl").type(STRING).description("파일 URL"),
                                fieldWithPath("content[].title").type(STRING).description("클래스 제목"),
                                fieldWithPath("content[].zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("content[].priceOne").type(NUMBER).description("클래스 1명 결제 가격"),
                                fieldWithPath("content[].priceTwo").type(NUMBER).description("클래스 2명 결제 가격"),
                                fieldWithPath("content[].priceThree").type(NUMBER).description("클래스 3명 결제 가격"),
                                fieldWithPath("content[].priceFour").type(NUMBER).description("클래스 4명 결제 가격")
                        ).and(pageDescriptor())
                ));
    }

    private Page<LectureSearchResponse> createRecommendSearchResponse() {
        return new PageImpl<>(createSearchResponseResult(),
                PageRequest.of(0, 4), 1);
    }


    @Test
    @DisplayName("클래스 검색 / 엘라스틱 서치와 통신이 안될 경우")
    void get_lectures_should_run_elasticsearch() throws Exception {

        // given
        stubbingElasticsearchException();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/lectures?categoryIdList=1,2,3&keyword=test&zoneId=1&searchPriceCondition=LOWER_EQUAL_FIVE")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isInternalServerError());
    }

    private void stubbingElasticsearchException() {
        when(lectureService.searchLectures(any(), any())).thenThrow(new ElasticsearchStatusException("샘플 메세지, 적절한 요청이 아닙니다.", BAD_REQUEST));
    }

    @Test
    @DisplayName("클래스 검색 / keyword가 공백일 경우")
    void get_lectures_should_not_keyword_is_blank() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/lectures?keyword=      ")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("클래스 상세 조회")
    void get_lecture() throws Exception {

        // when
        LectureDetailResponse res = LectureDtoDummy.createLectureDetailResponse();
        when(lectureService.getLecture(any()))
                .thenReturn(res);

        // then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/lectures/{lectureId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.lectureId", is(res.getLectureId().intValue())))
                .andExpect(jsonPath("$.thumbnailImageUrl", is(res.getThumbnailImageUrl())))
                .andExpect(jsonPath("$.lectureTitle", is(res.getLectureTitle())))
                .andExpect(jsonPath("$.zoneId", is(res.getZoneId().intValue())))
                .andExpect(jsonPath("$.categoryId", is(res.getCategoryId().intValue())))
                .andExpect(jsonPath("$.progressTime", is(res.getProgressTime())))
                .andExpect(jsonPath("$.priceOne", is(res.getPriceOne())))
                .andExpect(jsonPath("$.priceTwo", is(res.getPriceTwo())))
                .andExpect(jsonPath("$.priceThree", is(res.getPriceThree())))
                .andExpect(jsonPath("$.priceFour", is(res.getPriceFour())))
                .andExpect(jsonPath("$.regularPrice", is(res.getRegularPrice())))
                .andExpect(jsonPath("$.mainText", is(res.getMainText())))
                .andExpect(jsonPath("$.directorResponse.directorId", is(res.getDirectorResponse().getDirectorId().intValue())))
                .andExpect(jsonPath("$.directorResponse.directorProfileText", is(res.getDirectorResponse().getDirectorProfileText())))
                .andExpect(jsonPath("$.directorResponse.directorProfileImageUrl", is(res.getDirectorResponse().getDirectorProfileImageUrl())))
                .andExpect(jsonPath("$.curriculumResponseList", hasSize(res.getCurriculumResponseList().size())))
                .andExpect(jsonPath("$.finishedProductResponseList", hasSize(res.getFinishedProductResponseList().size())))
                .andExpect(jsonPath("$.scheduleResponseList", hasSize(res.getScheduleResponseList().size())))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("lectureId").description("조회할 클래스 ID")
                        ),
                        responseFields(
                                fieldWithPath("lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("thumbnailImageUrl").type(STRING).description("이미지 URL"),
                                fieldWithPath("lectureTitle").type(STRING).description("클래스 제목"),
                                fieldWithPath("zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("categoryId").type(NUMBER).description("취미 카테고리 ID"),
                                fieldWithPath("progressTime").type(NUMBER).description("진행 시간"),
                                fieldWithPath("priceOne").type(NUMBER).description("클래스 1명 결제 금액"),
                                fieldWithPath("priceTwo").type(NUMBER).description("클래스 2명 결제 금액"),
                                fieldWithPath("priceThree").type(NUMBER).description("클래스 3명 결제 금액"),
                                fieldWithPath("priceFour").type(NUMBER).description("클래스 4명 결제 금액"),
                                fieldWithPath("regularPrice").type(NUMBER).description("클래스 표준 결제 금액"),
                                fieldWithPath("mainText").type(STRING).description("클래스 상세 설명"),
                                fieldWithPath("myFavorite").type(BOOLEAN).description("내가 좋아요 했는지 여부"),
                                fieldWithPath("minParticipants").type(NUMBER).description("스케줄 최소 인원"),
                                fieldWithPath("maxParticipants").type(NUMBER).description("스케줄 최대 인원"),
                                fieldWithPath("directorResponse.directorId").type(NUMBER).description("디렉터의 ID"),
                                fieldWithPath("directorResponse.directorProfileText").type(STRING).description("디렉터의 프로필 설명"),
                                fieldWithPath("directorResponse.directorProfileImageUrl").type(STRING).description("디렉터의 프로필 사진 URL"),
                                fieldWithPath("curriculumResponseList[].curriculumId").type(NUMBER).description("커리큘럼 ID"),
                                fieldWithPath("curriculumResponseList[].curriculumImageUrl").type(STRING).description("커리큘럼 이미지 URL"),
                                fieldWithPath("curriculumResponseList[].order").type(NUMBER).description("커리큘럼의 순서"),
                                fieldWithPath("curriculumResponseList[].title").type(STRING).description("커리큘럼 소제목"),
                                fieldWithPath("curriculumResponseList[].detailText").type(STRING).description("커리큘럼 설명"),
                                fieldWithPath("finishedProductResponseList[].finishedProductId").type(NUMBER).description("완성작 ID"),
                                fieldWithPath("finishedProductResponseList[].order").type(NUMBER).description("완성작의 순서"),
                                fieldWithPath("finishedProductResponseList[].finishedProductImageUrl").type(STRING).description("완성작의 이미지 URL"),
                                fieldWithPath("finishedProductResponseList[].text").type(STRING).description("완성작 설명"),
                                fieldWithPath("scheduleResponseList[].scheduleId").type(NUMBER).description("스케줄 ID"),
                                fieldWithPath("scheduleResponseList[].lectureDate").type(STRING).description("스케줄 시작 날짜"),
                                fieldWithPath("scheduleResponseList[].currentParticipants").type(NUMBER).description("스케줄 현재 인원"),
                                fieldWithPath("scheduleResponseList[].startHour").type(NUMBER).description("스케줄 시작 시간"),
                                fieldWithPath("scheduleResponseList[].startMinute").type(NUMBER).description("스케줄 시작 분"),
                                fieldWithPath("scheduleResponseList[].endHour").type(NUMBER).description("스케줄 종료 시간"),
                                fieldWithPath("scheduleResponseList[].endMinute").type(NUMBER).description("스케줄 종료 분"),
                                fieldWithPath("scheduleResponseList[].progressMinutes").type(NUMBER).description("스케줄 진행 시간"),
                                fieldWithPath("scheduleResponseList[].scheduleStatus").type(STRING).description("스케줄의 상태")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("어떤 클래스를 선택한 게시글들 조회하기")
    void get_lecture_favorite_bulletins() throws Exception {

        // given
        when(lectureService.getBulletinsPickedLecture(any(), any()))
                .thenReturn(createBulletinsPickedLectureResponse());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/lectures/{lectureId}/bulletins", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.pageable.offset", is(0)))
                .andExpect(jsonPath("$.pageable.pageSize", is(10)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.first", is(true)))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("lectureId").description("클래스를 찜한 모집글들에서의 클래스 ID")
                        ),
                        responseFields(
                                fieldWithPath("content[].bulletinId").type(NUMBER).description("모집글 ID"),
                                fieldWithPath("content[].lectureImageUrl").type(STRING).description("클래스 썸네일"),
                                fieldWithPath("content[].zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("content[].categoryId").type(NUMBER).description("취미 카테고리 ID"),
                                fieldWithPath("content[].bulletinTitle").type(STRING).description("모집글 제목"),
                                fieldWithPath("content[].age").type(STRING).description("모집글 나이대"),
                                fieldWithPath("content[].time").type(STRING).description("모집글 모집 시간"),
                                fieldWithPath("content[].myFavorite").type(BOOLEAN).description("나의 모집글 좋아요 여부"),
                                fieldWithPath("content[].nowMembers").type(NUMBER).description("현재 인원"),
                                fieldWithPath("content[].maxMembers").type(NUMBER).description("최대 모집 인원")
                        ).and(pageDescriptor())
                ));
    }

    @Test
    @DisplayName("클래스의 문의글 조회")
    void get_lecture_questions() throws Exception {

        // given
        when(lectureService.getLectureQuestions(any(), any()))
                .thenReturn(createLectureQuestions());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/lectures/{lectureId}/questions", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("lectureId").description("질문글을 조회할 클래스 ID")
                        ),
                        responseFields(
                                fieldWithPath("content[].questionId").type(NUMBER).description("질문글 ID"),
                                fieldWithPath("content[].questionerId").type(NUMBER).description("질문글 작성자 ID"),
                                fieldWithPath("content[].questionerNickname").type(STRING).description("질문글 작성자 닉네임"),
                                fieldWithPath("content[].questionerProfileImageUrl").type(STRING).description("질문글 작성자 프로필 사진 URL"),
                                fieldWithPath("content[].questionDate").type(STRING).description("질문 날짜"),
                                fieldWithPath("content[].questionText").type(STRING).description("질문 내용"),
                                fieldWithPath("content[].answerComplete").type(BOOLEAN).description("질문글의 답변 여부")
                        ).and(pageDescriptor())
                ));
    }

    @Test
    @DisplayName("클래스 리뷰 전체 조회(페이징)")
    void get_lecture_reviews() throws Exception {

        // given
        when(lectureService.getLectureReviews(any(), any()))
                .thenReturn(createLectureReviews());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/lectures/{lectureId}/reviews", 1))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(parameterWithName("lectureId").description("리뷰를 조회할 클래스 ID")),
                        responseFields(
                                fieldWithPath("content[].reviewId").type(NUMBER).description("리뷰 ID"),
                                fieldWithPath("content[].reviewImageFileUrl").type(STRING).description("유저가 남긴 리뷰 사진").optional(),
                                fieldWithPath("content[].text").type(STRING).description("유저의 후기"),
                                fieldWithPath("content[].replyText").type(STRING).description("디렉터의 답글"),
                                fieldWithPath("content[].score").type(NUMBER).description("유저가 남긴 점수"),
                                fieldWithPath("content[].nickname").type(STRING).description("유저 닉네임"),
                                fieldWithPath("content[].profileImageFileUrl").type(STRING).description("유저의 프로필 이미지 URL")
                        ).and(pageDescriptor())
                ));
    }

    @Test
    @DisplayName("디렉터가 운영하는 클래스 조회")
    void get_director_lectures() throws Exception {

        // given
        when(lectureService.getDirectorLectures(any(), any()))
                .thenReturn(createDirectorLectures());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/lectures/directors/{directorId}", 1))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("directorId").description("조회할 디렉터의 ID")
                        ),
                        responseFields(
                                fieldWithPath("content[].lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("content[].onlyGjgs").type(BOOLEAN).description("온리 가지각색 여부"),
                                fieldWithPath("content[].myFavorite").type(BOOLEAN).description("내가 찜 표시한 클래스 여부"),
                                fieldWithPath("content[].imageUrl").type(STRING).description("파일 URL"),
                                fieldWithPath("content[].title").type(STRING).description("클래스 제목"),
                                fieldWithPath("content[].zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("content[].priceOne").type(NUMBER).description("클래스 1명 결제 가격"),
                                fieldWithPath("content[].priceTwo").type(NUMBER).description("클래스 2명 결제 가격"),
                                fieldWithPath("content[].priceThree").type(NUMBER).description("클래스 3명 결제 가격"),
                                fieldWithPath("content[].priceFour").type(NUMBER).description("클래스 4명 결제 가격")
                        ).and(sliceDescriptor())
                ));
    }

    private Slice<LectureSearchResponse> createDirectorLectures() {
        return RepositorySliceHelper.toSlice(createSearchResponseResult(), PageRequest.of(0, 20));
    }

    private Page<ReviewResponse> createLectureReviews() {
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
        return new PageImpl<>(content, PageRequest.of(0, 10), 1);
    }

    private Page<LectureQuestionsResponse> createLectureQuestions() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<LectureQuestionsResponse> content = List.of(
                createLectureQuestionResponse()
        );
        return new PageImpl<>(content, pageRequest, 1);
    }

    private LectureQuestionsResponse createLectureQuestionResponse() {
        return LectureQuestionsResponse.builder()
                .questionId(1L)
                .questionerId(1L)
                .questionerNickname("닉네임1")
                .questionerProfileImageUrl("url")
                .questionDate(LocalDateTime.now())
                .questionText("text")
                .answerComplete(false)
                .build();
    }

    private Page<BulletinSearchResponse> createBulletinsPickedLectureResponse() {
        return new PageImpl<>(createBulletinsPickedLectureResponseResult(),
                PageRequest.of(0, 10), 1);
    }

    private List<BulletinSearchResponse> createBulletinsPickedLectureResponseResult() {
        List<BulletinSearchResponse> list = new ArrayList<>();
        list.add(
                BulletinSearchResponse.builder()
                        .bulletinId((long) 1)
                        .lectureImageUrl("test")
                        .zoneId((long) 1)
                        .categoryId((long) 1)
                        .bulletinTitle("test")
                        .age(Age.TWENTYFIVE_TO_THIRTY.name())
                        .time("MORNING")
                        .myFavorite(false)
                        .nowMembers(2)
                        .maxMembers(4)
                        .build());
        return list;
    }

    private Page<LectureSearchResponse> createSearchResponse() {
        return new PageImpl<>(createSearchResponseResult(),
                PageRequest.of(0, 10), 1);
    }

    private List<LectureSearchResponse> createSearchResponseResult() {
        List<LectureSearchResponse> list = new ArrayList<>();
        list.add(
            LectureSearchResponse.builder()
                    .lectureId((long) 1)
                    .isOnlyGjgs(false)
                    .imageUrl("test")
                    .title("test")
                    .zoneId(1L)
                    .priceOne(100)
                    .priceTwo(100)
                    .priceThree(100)
                    .priceFour(100)
                    .build());
        return list;
    }
}