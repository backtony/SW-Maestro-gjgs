package com.gjgs.gjgs.modules.lecture.controllers;

import com.gjgs.gjgs.document.utils.DocumentLinkGenerator;
import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.lecture.dtos.admin.ConfirmLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.admin.RejectReason;
import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static com.gjgs.gjgs.modules.lecture.dtos.admin.DecideLectureType.ACCEPT;
import static com.gjgs.gjgs.modules.lecture.dtos.admin.DecideLectureType.REJECT;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminLectureControllerTest extends RestDocsTestSupport {

    private final String ADMIN_URL = "/api/v1/admin/lectures";


    @BeforeEach
    void setUpMockUser() {
        securityAdminMockSetting();
    }

    @Test
    @DisplayName("검수할 클래스들 가져오기")
    void get_confirm_lectures() throws Exception {

        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(adminLectureService.getConfirmLectures(any()))
                .thenReturn(new PageImpl<>(createConfirmLectureResponse(), pageRequest, 1));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(ADMIN_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        responseFields(
                                fieldWithPath("content[].lectureId").type(NUMBER).description("검수받아야 할 클래스 ID"),
                                fieldWithPath("content[].lectureTitle").type(STRING).description("클래스 명"),
                                fieldWithPath("content[].confirmDateTime").type(STRING).description("클래스 최종 등록일"),
                                fieldWithPath("content[].directorNickname").type(STRING).description("디렉터 닉네임"),
                                fieldWithPath("content[].categoryId").type(NUMBER).description("취미 카테고리 ID"),
                                fieldWithPath("content[].categoryName").type(STRING).description("취미 카테고리 명"),
                                fieldWithPath("content[].zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("content[].zoneName").type(STRING).description("지역 명")
                        ).and(pageDescriptor())
                ));
    }

    private List<ConfirmLectureResponse> createConfirmLectureResponse() {
        return List.of(
                ConfirmLectureResponse.builder()
                        .lectureId(1L)
                        .lectureTitle("test")
                        .confirmDateTime(now())
                        .directorNickname("test")
                        .categoryId(1L)
                        .categoryName("test")
                        .zoneId(1L)
                        .zoneName("test")
                        .build()
        );
    }

    @Test
    @DisplayName("검수할 클래스 상세 정보 가져오기")
    void get_confirm_lecture() throws Exception {

        // given
        when(adminLectureService.getConfirmLecture(any()))
                .thenReturn(getPutLectureResponseDto("CONFIRM"));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(ADMIN_URL + "/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(parameterWithName("lectureId").description("상세 정보를 볼 클래스 ID")),
                        responseFields(
                                fieldWithPath("lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("categoryId").type(NUMBER).description("취미 카테고리 ID"),
                                fieldWithPath("zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("title").type(STRING).description("클래스 제목"),
                                fieldWithPath("address").type(STRING).description("클래스 주소"),
                                fieldWithPath("thumbnailImageFileName").type(STRING).description("사진 파일명"),
                                fieldWithPath("thumbnailImageFileUrl").type(STRING).description("사진 파일 URL"),
                                fieldWithPath("minParticipants").type(NUMBER).description("클래스의 최소 참가 인원"),
                                fieldWithPath("maxParticipants").type(NUMBER).description("클래스의 최대 참가 인원"),
                                fieldWithPath("mainText").type(STRING).description("소개글"),
                                fieldWithPath("lectureStatus").type(STRING).description("클래스 검수 상태"),
                                fieldWithPath("finishedProductList[].finishedProductId").type(NUMBER).description("완성작 ID"),
                                fieldWithPath("finishedProductList[].orders").type(NUMBER).description("완성작의 순서"),
                                fieldWithPath("finishedProductList[].text").type(STRING).description("완성작 설명"),
                                fieldWithPath("finishedProductList[].finishedProductImageName").type(STRING).description("완성작 파일명"),
                                fieldWithPath("finishedProductList[].finishedProductImageUrl").type(STRING).description("완성작 파일 URL"),
                                fieldWithPath("curriculumList[].curriculumId").type(NUMBER).description("커리큘럼 ID"),
                                fieldWithPath("curriculumList[].orders").type(NUMBER).description("커리큘럼의 순서"),
                                fieldWithPath("curriculumList[].title").type(STRING).description("커리큘럼 명"),
                                fieldWithPath("curriculumList[].detailText").type(STRING).description("커리큘럼 상세 설명"),
                                fieldWithPath("curriculumList[].curriculumImageName").type(STRING).description("커리큘럼 파일명"),
                                fieldWithPath("curriculumList[].curriculumImageUrl").type(STRING).description("커리큘럼 파일 URL"),
                                fieldWithPath("scheduleList[].scheduleId").type(NUMBER).description("스케줄 ID"),
                                fieldWithPath("scheduleList[].lectureDate").type(STRING).description("스케줄 진행 날짜"),
                                fieldWithPath("scheduleList[].startHour").type(NUMBER).description("스케줄 진행 시작 시간"),
                                fieldWithPath("scheduleList[].startMinute").type(NUMBER).description("스케줄 진행 시작 분"),
                                fieldWithPath("scheduleList[].endHour").type(NUMBER).description("스케줄 끝 시간"),
                                fieldWithPath("scheduleList[].endMinute").type(NUMBER).description("스케줄 끝 분"),
                                fieldWithPath("scheduleList[].progressMinute").type(NUMBER).description("스케줄 진행 시간"),
                                fieldWithPath("price.regularPrice").type(NUMBER).description("클래스 표준 가격"),
                                fieldWithPath("price.priceOne").type(NUMBER).description("클래스 1인 결제 가격"),
                                fieldWithPath("price.priceTwo").type(NUMBER).description("클래스 2인 결제 가격"),
                                fieldWithPath("price.priceThree").type(NUMBER).description("클래스 3인 결제 가격"),
                                fieldWithPath("price.priceFour").type(NUMBER).description("클래스 4인 결제 가격"),
                                fieldWithPath("coupon.couponPrice").type(NUMBER).description("쿠폰 할인 가격"),
                                fieldWithPath("coupon.couponCount").type(NUMBER).description("쿠폰이 남은 갯수")
                        )
                ));
    }

    private PutLectureResponse getPutLectureResponseDto(String lectureStatus) {
        return PutLectureResponse.builder()
                .lectureId(1L)
                .categoryId(1L)
                .zoneId(1L)
                .title("test")
                .address("test")
                .thumbnailImageFileName("test")
                .thumbnailImageFileUrl("test")
                .minParticipants(2)
                .maxParticipants(10)
                .mainText("test")
                .lectureStatus(lectureStatus)
                .finishedProductList(Set.of(PutLectureResponse.FinishedProductResponse.builder()
                        .finishedProductId(1L)
                        .orders(1)
                        .text("테스트")
                        .finishedProductImageName("test")
                        .finishedProductImageUrl("testurl")
                        .build()
                ))
                .curriculumList(Set.of(PutLectureResponse.CurriculumResponse.builder()
                        .curriculumId(1L)
                        .orders(1)
                        .title("test")
                        .detailText("test")
                        .curriculumImageName("test")
                        .curriculumImageUrl("teset")
                        .build()))
                .scheduleList(Set.of(PutLectureResponse.ScheduleResponse.builder()
                        .scheduleId(1L)
                        .lectureDate(LocalDate.now())
                        .startHour(12)
                        .startMinute(0)
                        .endHour(13)
                        .endMinute(0)
                        .progressMinute(60)
                        .build()))
                .price(PutLectureResponse.PriceResponse.builder()
                        .regularPrice(1000)
                        .priceOne(1000)
                        .priceTwo(1000)
                        .priceThree(1000)
                        .priceFour(1000)
                        .build())
                .coupon(PutLectureResponse.CouponResponse.builder()
                        .couponCount(10)
                        .couponPrice(500)
                        .build())
                .build();
    }

    @Test
    @DisplayName("클래스 승인하기")
    void accept_lecture() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(ADMIN_URL + "/{lectureId}/{decideType}", 1, ACCEPT.name())
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("검수를 결정할 클래스 ID"),
                                parameterWithName("decideType").description(generateLinkCode(DocumentLinkGenerator.DocUrl.LECTURE_CONFIRM_DECIDE_TYPE))
                        )
                ));
    }

    @Test
    @DisplayName("클래스 거절하기 / 거절 사유를 적지 않을 경우 예외 발생")
    void reject_lecture_should_write_reject_reason() throws Exception {

        // given
        RejectReason reason = RejectReason.builder().build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(ADMIN_URL + "/{lectureId}/{decideType}", 1, REJECT.name())
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(reason))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    @DisplayName("클래스 거절하기 / 거절 사유가 10자 미만일 경우 예외 발생")
    void reject_lecture_should_min_10_length_reject_reason() throws Exception {

        // given
        RejectReason reason = RejectReason.builder().rejectReason("123").build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(ADMIN_URL + "/{lectureId}/{decideType}", 1, REJECT.name())
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(reason))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }
}
