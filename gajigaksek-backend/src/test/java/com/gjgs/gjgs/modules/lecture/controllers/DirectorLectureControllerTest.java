package com.gjgs.gjgs.modules.lecture.controllers;

import com.gjgs.gjgs.document.utils.DocumentLinkGenerator;
import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.coupon.dto.DirectorCouponResponse;
import com.gjgs.gjgs.modules.exception.coupon.AvailableCouponException;
import com.gjgs.gjgs.modules.exception.lecture.ExistCreatingLectureException;
import com.gjgs.gjgs.modules.exception.lecture.NotRejectLectureException;
import com.gjgs.gjgs.modules.exception.schedule.CanNotDeleteScheduleException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.DirectorLectureResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.GetLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.director.question.DirectorQuestionResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorLectureScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse;
import com.gjgs.gjgs.modules.question.enums.QuestionStatus;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus.RECRUIT;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DirectorLectureControllerTest extends RestDocsTestSupport {

    private final String LECTURE_URI = "/api/v1/mypage/directors/lectures";
    private final String SCHEDULE_URI = "/api/v1/mypage/directors/schedules";
    private final String QUESTION_URI = "/api/v1/mypage/directors/questions";
    private final String LECTURE_SCHEDULE_URI = "/api/v1/mypage/directors/lectures/{lectureId}/schedules";
    private final String SCHEDULE_DELETE_URI = "/api/v1/mypage/directors/lectures/{lectureId}/schedules/{scheduleId}";
    private final String COUPON_URI = "/api/v1/mypage/directors/coupons";


    @BeforeEach
    void setUpMockUser() {
        securityDirectorMockSetting();
    }

    @Test
    @DisplayName("내가 진행하는 모든 클래스 조회하기")
    void get_director_lectures() throws Exception {

        // given
        GetLectureType all = GetLectureType.ALL;
        when(directorLectureService.getDirectorLectures(all))
                .thenReturn(getDirectorLectureResponse());

         // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(LECTURE_URI + "?condition=ALL")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestParameters(parameterWithName("condition").description(generateLinkCode(DocumentLinkGenerator.DocUrl.GET_LECTURE_TYPE))),
                        responseFields(
                                fieldWithPath("lectureList[].savedLectureId").type(NUMBER).description("검색 결과 클래스 ID"),
                                fieldWithPath("lectureList[].thumbnailImageUrl").type(STRING).description("클래스 썸네일"),
                                fieldWithPath("lectureList[].title").type(STRING).description("클래스명"),
                                fieldWithPath("lectureList[].mainText").type(STRING).description("클래스 설명"),
                                fieldWithPath("lectureList[].lectureStatus").type(STRING).description("클래스 상태"),
                                fieldWithPath("lectureList[].finished").type(BOOLEAN).description("종료 여부"),
                                fieldWithPath("lectureList[].rejectReason").type(STRING).description("거절 사유")
                        )
                ))
                ;
    }

    private DirectorLectureResponse getDirectorLectureResponse() {
        return DirectorLectureResponse.builder()
                .lectureList(List.of(DirectorLectureResponse.LectureResponse.builder()
                        .savedLectureId(1L)
                        .thumbnailImageUrl("test")
                        .title("test")
                        .mainText("test")
                        .lectureStatus("ACCEPT")
                        .finished(false)
                        .rejectReason("")
                        .build()
                )).build();
    }

    @Test
    @DisplayName("내가 진행하는 클래스들의 스케줄 조회하기")
    void get_director_lectures_schedules() throws Exception {

        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(directorLectureService.getDirectorLecturesSchedules(any(), any()))
                .thenReturn(new PageImpl<>(getDirectorLectureScheduleResponse(), pageRequest, 1));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(SCHEDULE_URI + "?searchType=ALL")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        responseFields(
                                fieldWithPath("content[].lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("content[].scheduleId").type(NUMBER).description("스케줄 ID"),
                                fieldWithPath("content[].title").type(STRING).description("클래스 제목"),
                                fieldWithPath("content[].currentParticipants").type(NUMBER).description("현재 참가 인원"),
                                fieldWithPath("content[].maxParticipants").type(NUMBER).description("최대 인원"),
                                fieldWithPath("content[].scheduleDate").type(STRING).description("스케줄 진행 날짜"),
                                fieldWithPath("content[].startHour").type(NUMBER).description("스케줄 시작 시간"),
                                fieldWithPath("content[].startMinute").type(NUMBER).description("스케줄 시작 분"),
                                fieldWithPath("content[].endHour").type(NUMBER).description("스케줄 종료 시간"),
                                fieldWithPath("content[].endMinute").type(NUMBER).description("스케줄 종료 분"),
                                fieldWithPath("content[].regularPrice").type(NUMBER).description("클래스 표준 가격"),
                                fieldWithPath("content[].priceOne").type(NUMBER).description("클래스 1인 신청 가격"),
                                fieldWithPath("content[].priceTwo").type(NUMBER).description("클래스 2인 신청 가격"),
                                fieldWithPath("content[].priceThree").type(NUMBER).description("클래스 3인 신청 가격"),
                                fieldWithPath("content[].priceFour").type(NUMBER).description("클래스 4인 신청 가격"),
                                fieldWithPath("content[].scheduleStatus").type(STRING).description("스케줄 상태")
                        ).and(pageDescriptor())
                ));
    }

    private List<DirectorLectureScheduleResponse> getDirectorLectureScheduleResponse() {
        return List.of(
                DirectorLectureScheduleResponse.builder()
                        .lectureId(1L)
                        .scheduleId(1L)
                        .title("test")
                        .currentParticipants(2)
                        .maxParticipants(10)
                        .scheduleDate(LocalDate.now())
                        .startHour(12)
                        .startMinute(0)
                        .endHour(13)
                        .endMinute(0)
                        .regularPrice(1000)
                        .priceOne(1000)
                        .priceTwo(1000)
                        .priceThree(1000)
                        .priceFour(1000)
                        .scheduleStatus(RECRUIT.name())
                        .build()
        );
    }

    @Test
    @DisplayName("내가 진행하는 클래스들의 스케줄 조회하기 / keyword가 공백일 경우")
    void get_director_lectures_schedules_should_not_blank_keyword() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(SCHEDULE_URI + "?searchType=ALL&keyword=  ")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("내가 진행하는 클래스들의 스케줄 조회하기 / 날짜 형식이 yyyy-MM-dd가 아닐 경우")
    void get_director_schedules_not_YYYYMMDD() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(SCHEDULE_URI + "?searchType=ALL&startDate=0101-23-45&endDate=0101-23-23")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    @DisplayName("내가 운영하는 클래스들의 문의글 가져오기")
    void get_director_questions() throws Exception {

        // given
        List<DirectorQuestionResponse> content = createDirectorQuestionResponse();
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(directorLectureService.getDirectorQuestions(any(), any()))
                .thenReturn(new PageImpl<>(content, pageRequest, 1));

        // when, then
        mockMvc.perform(get(QUESTION_URI + "?questionStatus=COMPLETE&lectureId=1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestParameters(
                                parameterWithName("questionStatus").description(generateLinkCode(DocumentLinkGenerator.DocUrl.QUESTION_STATUS)),
                                parameterWithName("lectureId").description("질문글을 찾을 클래스 ID")
                        ),
                        responseFields(
                                fieldWithPath("content[].lectureInfo.id").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("content[].lectureInfo.title").type(STRING).description("클래스 제목"),
                                fieldWithPath("content[].lectureInfo.lectureCreatedAt").type(STRING).description("클래스가 만들어진 날짜"),
                                fieldWithPath("content[].questionInfo.id").type(NUMBER).description("질문글 ID"),
                                fieldWithPath("content[].questionInfo.questionerId").type(NUMBER).description("질문자 ID"),
                                fieldWithPath("content[].questionInfo.questionerNickname").type(STRING).description("질문자 닉네임"),
                                fieldWithPath("content[].questionInfo.questionMainText").type(STRING).description("질문자의 문의글"),
                                fieldWithPath("content[].questionInfo.questionAnswerText").type(STRING).description("디렉터의 답글"),
                                fieldWithPath("content[].questionInfo.questionStatus").type(STRING).description("질문글 상태"),
                                fieldWithPath("content[].questionInfo.questionCreatedAt").type(STRING).description("질문글 작성 날짜")
                        ).and(pageDescriptor())
                ))
        ;
    }

    private List<DirectorQuestionResponse> createDirectorQuestionResponse() {
        return List.of(
                DirectorQuestionResponse.builder()
                        .lectureInfo(DirectorQuestionResponse.LectureInfo.builder()
                                .id(1L).title("test").lectureCreatedAt(now())
                                .build())
                        .questionInfo(DirectorQuestionResponse.QuestionInfo.builder()
                                .id(1L).questionerId(1L).questionerNickname("test").questionMainText("test")
                                .questionAnswerText("test").questionStatus(QuestionStatus.COMPLETE.name())
                                .questionCreatedAt(now())
                                .build())
                        .build()
        );
    }

    @Test
    @DisplayName("지정한 클래스의 스케줄을 가져온다.")
    void get_lecture_schedules() throws Exception {

        // given
        DirectorScheduleResponse response = createDirectorScheduleResponse();
        when(directorScheduleService.getLectureSchedules(any()))
                .thenReturn(response);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(LECTURE_SCHEDULE_URI, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("스케줄을 찾을 클래스 ID")
                        ),
                        responseFields(
                                fieldWithPath("lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("progressTime").type(NUMBER).description("스케줄 진행 분"),
                                fieldWithPath("minParticipants").type(NUMBER).description("스케줄 신청 최소 인원"),
                                fieldWithPath("maxParticipants").type(NUMBER).description("스케줄 최대 신청 인원"),
                                fieldWithPath("scheduleList[].scheduleId").type(NUMBER).description("스케줄 ID"),
                                fieldWithPath("scheduleList[].lectureDate").type(STRING).description("스케줄 진행 날짜"),
                                fieldWithPath("scheduleList[].startHour").type(NUMBER).description("스케줄 시작 시간"),
                                fieldWithPath("scheduleList[].startMinute").type(NUMBER).description("스케줄 시작 분"),
                                fieldWithPath("scheduleList[].endHour").type(NUMBER).description("스케줄 종료 시간"),
                                fieldWithPath("scheduleList[].endMinute").type(NUMBER).description("스케줄 종료 분"),
                                fieldWithPath("scheduleList[].currentParticipants").type(NUMBER).description("스케줄 현재 신청 인원"),
                                fieldWithPath("scheduleList[].canDelete").type(BOOLEAN).description("이 스케줄을 지울 수 있는지 여부")
                        )
                ))
        ;
    }

    private DirectorScheduleResponse createDirectorScheduleResponse() {
        return DirectorScheduleResponse.builder()
                .lectureId(1L)
                .progressTime(60)
                .minParticipants(5)
                .maxParticipants(10)
                .scheduleList(Set.of(
                        DirectorScheduleResponse.ScheduleDto.builder()
                                .scheduleId(1L)
                                .lectureDate(LocalDate.now())
                                .startHour(12)
                                .startMinute(0)
                                .endHour(13)
                                .endMinute(0)
                                .currentParticipants(3)
                                .canDelete(false)
                                .build()
                ))
                .build();
    }

    @Test
    @DisplayName("스케줄을 추가한다. / 제약 조건을 지키지 않을 경우")
    void add_schedule_should_follow_constraint() throws Exception {

        // given
        CreateLecture.ScheduleDto schedule = createScheduleDtoNotConstraint();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(LECTURE_SCHEDULE_URI, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(schedule))
                .contentType(APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(6)));
    }

    @Test
    @DisplayName("스케줄을 추가한다.")
    void add_schedule() throws Exception {

        // given
        CreateLecture.ScheduleDto schedule = createScheduleDto();
        when(directorScheduleService.createSchedule(any(), any()))
                .thenReturn(DirectorScheduleResponse.PostDelete.builder()
                        .scheduleId(1L).result(DirectorScheduleResponse.PostDelete.Result.CREATE)
                        .build());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(LECTURE_SCHEDULE_URI, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(schedule))
                .contentType(APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("스케줄을 추가할 클래스 ID")
                        ),
                        requestFields(
                                fieldWithPath("progressMinute").type(NUMBER).description("클래스를 진행하는 시간").attributes(field("constraints", "최소 60 이상, 30분 단위로 나눠져야 함")),
                                fieldWithPath("lectureDate").type(STRING).description("진행 날짜").attributes(field("constraints", "yyyy-MM-dd 형식에 맞춰야 함")),
                                fieldWithPath("startHour").type(NUMBER).description("클래스 시작 시간").attributes(field("constraints", "0 ~ 23 까지 입력 가능")),
                                fieldWithPath("startMinute").type(NUMBER).description("클래시 시작 분").attributes(field("constraints", "0 ~ 59 까지 입력 가능, 30분 단위로 나눠져야 함")),
                                fieldWithPath("endHour").type(NUMBER).description("주지 않아도 되는 필드").optional(),
                                fieldWithPath("endMinute").type(NUMBER).description("주지 않아도 되는 필드").optional()
                        ),
                        responseFields(
                                fieldWithPath("scheduleId").type(NUMBER).description("추가되거나 삭제된 스케줄 ID"),
                                fieldWithPath("result").type(STRING).description(generateLinkCode(DocumentLinkGenerator.DocUrl.SCHEDULE_RESULT))
                        )
                ))
        ;
    }

    @Test
    @DisplayName("스케줄을 삭제한다. / 해당 스케줄에 참가자가 있을 경우 삭제할 수 없다.")
    void delete_schedule_should_not_exist_participants() throws Exception {

        // given
        stubbingScheduleInParticipants();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(SCHEDULE_DELETE_URI, 1, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("UTF-8"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("스케줄을 삭제한다.")
    void delete_schedule() throws Exception {

        // given
        DirectorScheduleResponse.PostDelete response = DirectorScheduleResponse.PostDelete.delete(1L);
        when(directorScheduleService.deleteSchedule(any(), any()))
                .thenReturn(response);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(SCHEDULE_DELETE_URI, 1, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("스케줄이 속한 클래스 ID"),
                                parameterWithName("scheduleId").description("지울 스케줄 ID")
                        ),
                        responseFields(
                                fieldWithPath("scheduleId").description("지워진 스케줄 ID"),
                                fieldWithPath("result").description(generateLinkCode(DocumentLinkGenerator.DocUrl.SCHEDULE_RESULT))
                        )))
        ;
    }

    @Test
    @DisplayName("내가 만든 클래스의 쿠폰 가져오기")
    void get_director_coupon() throws Exception {

        // given
        DirectorCouponResponse response = createDirectorCouponResponse();
        when(directorCouponService.getDirectorCoupons())
                .thenReturn(response);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(COUPON_URI)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        responseFields(
                                fieldWithPath("couponResponseList[].lectureId").type(NUMBER).description("발급한 클래스 ID"),
                                fieldWithPath("couponResponseList[].title").type(STRING).description("클래스 제목"),
                                fieldWithPath("couponResponseList[].issueDate").type(STRING).description("발행한 날짜"),
                                fieldWithPath("couponResponseList[].closeDate").type(STRING).description("유저에게 쿠폰이 발급되는 종료 날짜"),
                                fieldWithPath("couponResponseList[].discountPrice").type(NUMBER).description("할인 가격"),
                                fieldWithPath("couponResponseList[].chargeCount").type(NUMBER).description("충전 갯수"),
                                fieldWithPath("couponResponseList[].receivePeople").type(NUMBER).description("받은 사람들 수"),
                                fieldWithPath("couponResponseList[].remainCount").type(NUMBER).description("남은 쿠폰 수")
                        )
                ))
        ;
    }

    private DirectorCouponResponse createDirectorCouponResponse() {
        return DirectorCouponResponse.builder()
                .couponResponseList(List.of(DirectorCouponResponse.CouponResponse.builder()
                        .lectureId(1L)
                        .title("test")
                        .issueDate(LocalDateTime.now())
                        .closeDate(LocalDateTime.now().plusDays(30))
                        .discountPrice(1000)
                        .chargeCount(10)
                        .receivePeople(1)
                        .remainCount(1)
                        .build()
                ))
                .build();
    }

    @Test
    @DisplayName("쿠폰 (재)발행하기")
    void issue_coupon() throws Exception {

        // given
        CreateLecture.CouponDto request = CreateLecture.CouponDto.builder()
                .couponPrice(1000).couponCount(10)
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(COUPON_URI + "/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(APPLICATION_JSON)
                .content(createJson(request)))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("쿠폰을 (재)발행 할 클래스 ID")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("쿠폰 (재)발행하기 / 아직 발행중인 쿠폰이 남아있을 경우")
    void issue_coupon_should_not_remain_coupon() throws Exception {

        // given
        stubbingCouponNotIssueException();
        CreateLecture.CouponDto request = CreateLecture.CouponDto.builder()
                .couponPrice(1000).couponCount(10)
                .build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(COUPON_URI + "/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(APPLICATION_JSON)
                .content(createJson(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("쿠폰 발행 중지하기")
    void stop_issue_coupon() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(COUPON_URI + "/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("쿠폰 발행을 마감할 클래스 ID")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("검수 거절 된 클래스 삭제하기")
    void delete_reject_lecture() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(LECTURE_URI + "/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("삭제할 클래스 ID")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("검수 거절 된 클래스 삭제하기 / reject된 클래스가 없을 경우")
    void delete_reject_lecture_not_found() throws Exception {

        // given
        stubbingNotRejectLecture();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(LECTURE_URI + "/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("검수 거절 된 클래스 재작성하기")
    void rewrite_lecture() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(LECTURE_URI + "/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("재작성 할 클래스 ID")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("검수 거절 된 클래스 재작성하기 / 이 때, 작성중인 클래스가 있으면 안된다.")
    void rewrite_lecture_should_not_exist_creating_lecture() throws Exception {

        // given
        stubbingExistCreatingLectureException();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(LECTURE_URI + "/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isConflict());
    }

    private void stubbingNotRejectLecture() {
        doThrow(new NotRejectLectureException()).when(directorLectureService).deleteRejectLecture(any());
    }

    private void stubbingScheduleInParticipants() {
        when(directorScheduleService.deleteSchedule(any(), any()))
                .thenThrow(new CanNotDeleteScheduleException());
    }

    private void stubbingExistCreatingLectureException() {
        doThrow(new ExistCreatingLectureException()).when(directorLectureService).changeLectureCreating(any());
    }

    private void stubbingCouponNotIssueException() {
        doThrow(new AvailableCouponException()).when(directorCouponService).issue(any(), any());
    }

    private CreateLecture.ScheduleDto createScheduleDtoNotConstraint() {
        return CreateLecture.ScheduleDto.builder()
                .progressMinute(59)
                .startHour(24)
                .startMinute(61)
                .build();
    }

    private CreateLecture.ScheduleDto createScheduleDto() {
        return CreateLecture.ScheduleDto.builder()
                .progressMinute(120)
                .startHour(17)
                .startMinute(30)
                .lectureDate(LocalDate.of(2021, 9, 15))
                .build();
    }
}
