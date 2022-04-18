package com.gjgs.gjgs.modules.lecture.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.exception.lecture.MissingFileException;
import com.gjgs.gjgs.modules.exception.lecture.ProductAndFileNotEqualException;
import com.gjgs.gjgs.modules.exception.lecture.TemporaryNotSaveLectureException;
import com.gjgs.gjgs.modules.exception.lecture.ThumbnailIsNotOneException;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureProcessResponse;
import com.gjgs.gjgs.modules.lecture.dtos.create.TemporaryStorageLectureManageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.DocUrl.CREATE_LECTURE_STEP;
import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.DocUrl.LECTURE_SAVE_DELETE_RESPONSE;
import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TemporaryStorageLectureControllerTest extends RestDocsTestSupport {

    private final String PUT = "PUT";
    private final String URL = "/api/v2/director/lectures";

    @BeforeEach
    void setUpMockUser() {
        securityDirectorMockSetting();
    }



    @Test
    @DisplayName("공통 에러 1, 썸네일을 복수로 입력했을 경우")
    void common_exception_should_thumbnail_is_one() throws Exception {

        // given
        CreateLecture.FirstRequest dto = CreateLecture.FirstRequest.builder()
                .createLectureStep(FIRST).categoryId(1L)
                .address("서울시 광진구").title("안녕하세요 안녕하세요 안녕하세요!").zoneId(1L).build();
        MockMultipartFile firstRequest = getMockMultipartJson(dto);
        MockMultipartFile thumbnailImageFileUrl = getMockMultipartFile();
        when(factory.getService(any())).thenReturn(putFirstService);
        when(putFirstService.putLectureProcess(any(), any()))
                .thenThrow(new ThumbnailIsNotOneException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(firstRequest)
                .file(thumbnailImageFileUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("공통 에러 2, 파일을 아예 주지 않았을 경우")
    void common_exception_should_not_file_missing() throws Exception {

        // given
        CreateLecture.FirstRequest dto = CreateLecture.FirstRequest.builder()
                .createLectureStep(FIRST).categoryId(1L)
                .address("서울시 광진구").title("안녕하세요 안녕하세요 안녕하세요!").zoneId(1L).build();
        MockMultipartFile firstRequest = getMockMultipartJson(dto);
        MockMultipartFile thumbnailImageFileUrl = getMockMultipartFile();
        when(factory.getService(any())).thenReturn(putFirstService);

        when(putFirstService.putLectureProcess(any(), any()))
                .thenThrow(new MissingFileException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(firstRequest)
                .file(thumbnailImageFileUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("공통 에러 3, 파일 사이즈가 다른 경우")
    void common_exception_should_equal_file_size_and_contents_size() throws Exception {

        // given
        CreateLecture.FirstRequest dto = CreateLecture.FirstRequest.builder()
                .createLectureStep(FIRST).categoryId(1L)
                .address("서울시 광진구").title("안녕하세요 안녕하세요 안녕하세요!").zoneId(1L).build();
        MockMultipartFile firstRequest = getMockMultipartJson(dto);
        MockMultipartFile thumbnailImageFileUrl = getMockMultipartFile();
        when(factory.getService(any())).thenReturn(putFirstService);
        when(putFirstService.putLectureProcess(any(), any()))
                .thenThrow(new ProductAndFileNotEqualException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(firstRequest)
                .file(thumbnailImageFileUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("클래스 취미 카테고리, 사진 선택 / 제목, 주소 입력")
    void save_lecture_first() throws Exception {

        // given
        CreateLecture.FirstRequest dto = CreateLecture.FirstRequest.builder()
                .createLectureStep(FIRST).categoryId(1L)
                .address("서울시 광진구").title("안녕하세요 안녕하세요 안녕하세요!").zoneId(1L).build();
        MockMultipartFile firstRequest = getMockMultipartJson(dto);
        MockMultipartFile thumbnailImageFileUrl = getMockMultipartFile();
        when(factory.getService(any())).thenReturn(putFirstService);
        when(putFirstService.putLectureProcess(any(), any()))
                .thenReturn(CreateLectureProcessResponse.completeSaveFirst(1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(firstRequest)
                .file(thumbnailImageFileUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestParts(
                                partWithName("request").description("클래스를 처음 저장할 요청 JSON"),
                                partWithName("files").description("클래스 썸네일").attributes(field("constraints", "한장만 필요"))
                        ),
                        requestPartFields("request",
                                fieldWithPath("createLectureStep").type(STRING).description(generateLinkCode(CREATE_LECTURE_STEP)),
                                fieldWithPath("lectureId").optional().type(NUMBER).description("클래스 ID (기존의 클래스를 수정할 경우 필요)").attributes(field("constraints","nullable")),
                                fieldWithPath("categoryId").type(NUMBER).description("취미 카테고리 ID").attributes(field("constraints","NOT NULL")),
                                fieldWithPath("zoneId").type(NUMBER).description("클래스 지역 ID").attributes(field("constraints","NOT NULL")),
                                fieldWithPath("title").type(STRING).description("클래스 제목").attributes(field("constraints","10자 이상, 100자 이하")),
                                fieldWithPath("address").type(STRING).description("클래스를 진행하는 주소").attributes(field("constraint", "NOT NULL")),
                                fieldWithPath("thumbnailImageFileName").optional().description("필요 없는 값").attributes(field("constraint", "NULL")),
                                fieldWithPath("thumbnailImageFileUrl").optional().description("필요 없는 값").attributes(field("constraint", "NULL"))
                        ),
                        responseFields(
                                fieldWithPath("lectureId").description("만들거나 수정한 클래스 ID"),
                                fieldWithPath("completedStepName").description(generateLinkCode(CREATE_LECTURE_STEP))
                        )
                ));
    }

    @Test
    @DisplayName("클래스 취미 카테고리, 제목, zoneId, 주소가 없을 경우")
    void save_lecture_first_should_follow_constraints() throws Exception {

        // given
        CreateLecture.FirstRequest dto = CreateLecture.FirstRequest.builder()
                .createLectureStep(FIRST).build();
        MockMultipartFile firstRequest = getMockMultipartJson(dto);
        MockMultipartFile thumbnailImageFileUrl = getMockMultipartFile();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(firstRequest)
                .file(thumbnailImageFileUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                })
                .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(4)));
    }

    @Test
    @DisplayName("클래스 제목이 10자 미만인 경우")
    void save_lecture_first_should_lecture_title_over_10_length() throws Exception {

        // given
        CreateLecture.FirstRequest dto = CreateLecture.FirstRequest.builder().createLectureStep(FIRST)
                .categoryId(1L).address("서울시 광진구").title("gg").zoneId(1L).build();
        MockMultipartFile firstRequest = getMockMultipartJson(dto);
        MockMultipartFile thumbnailImageFileUrl = getMockMultipartFile();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(firstRequest)
                .file(thumbnailImageFileUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    @DisplayName("클래스 소개 저장하기")
    void save_lecture_intro() throws Exception {

        // given
        CreateLecture.IntroRequest dto = createIntro();
        MockMultipartFile intro = getMockMultipartJson(dto);
        List<MockMultipartFile> files = getMockMultipartFileList(3);
        when(factory.getService(any())).thenReturn(putIntroService);
        when(putIntroService.putLectureProcess(any(), any()))
                .thenReturn(CreateLectureProcessResponse.completeIntro(1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(intro)
                .file(files.get(0))
                .file(files.get(1))
                .file(files.get(2))
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestParts(
                                partWithName("request").description("클래스를 처음 저장할 요청 JSON"),
                                partWithName("files").description("클래스 수강후 완성 작품 사진").attributes(field("constraints", "1 ~ 4장 전송 가능, finishedProductInfoList size와 맞지 않을 경우 예외 발생"))
                        ),
                        requestPartFields("request",
                                fieldWithPath("mainText").type(STRING).description("클래스 소개글").attributes(field("constraints", "10자 이상 100자 이하")),
                                fieldWithPath("createLectureStep").type(STRING).description(generateLinkCode(CREATE_LECTURE_STEP)),
                                fieldWithPath("finishedProductInfoList[]").description("완성작 리스트").attributes(field("constraints", "완성작 정보를 1개에서 4개 사이로 입력")),
                                fieldWithPath("finishedProductInfoList[].order").type(NUMBER).description("클래스 완성작 순서"),
                                fieldWithPath("finishedProductInfoList[].text").type(STRING).description("클래스 완성작 소제목"),
                                fieldWithPath("finishedProductInfoList[].finishedProductImageName").description("주지 않아도 되는 필드"),
                                fieldWithPath("finishedProductInfoList[].finishedProductImageUrl").description("주지 않아도 되는 필드")
                                ),
                        responseFields(
                                fieldWithPath("lectureId").description("만들거나 수정한 클래스 ID"),
                                fieldWithPath("completedStepName").description(generateLinkCode(CREATE_LECTURE_STEP))
                        )
                ));
    }

    @Test
    @DisplayName("클래스 소개 저장하기 / 완성작 4장 초과")
    void lecture_intro_finished_product_exceed_4() throws Exception {

        // given
        CreateLecture.IntroRequest dto = createExceedFinishedListIntro();
        MockMultipartFile intro = getMockMultipartJson(dto);
        List<MockMultipartFile> files = getMockMultipartFileList(3);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(intro)
                .file(files.get(0))
                .file(files.get(1))
                .file(files.get(2))
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    @DisplayName("클래스 소개 저장하기 / 클래스 소개글, 완성 작품 정보가 없을 경우")
    void save_lecture_intro_should_follow_constraints() throws Exception {

        // given
        CreateLecture.IntroRequest dto = CreateLecture.IntroRequest.builder().createLectureStep(INTRO).build();
        MockMultipartFile intro = getMockMultipartJson(dto);
        List<MockMultipartFile> files = getMockMultipartFileList(3);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(intro)
                .file(files.get(0))
                .file(files.get(1))
                .file(files.get(2))
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    @Test
    @DisplayName("클래스 소개 저장하기 / 완성작 리스트의 순서와 제목을 입력하지 않았을 경우와 설명글이 10자 이하인 경우")
    void save_lecture_intro_should_main_text_over_10_length_and_not_input_order_text() throws Exception {

        // given
        CreateLecture.IntroRequest dto = createEmptyOrderTextListAndNotOver10MainText();
        MockMultipartFile intro = getMockMultipartJson(dto);
        List<MockMultipartFile> files = getMockMultipartFileList(3);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(intro)
                .file(files.get(0))
                .file(files.get(1))
                .file(files.get(2))
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    @Test
    @DisplayName("커리큘럼 등록하기")
    void save_lecture_curriculum() throws Exception {

        // given
        CreateLecture.CurriculumRequest dto = createCurriculumList();
        MockMultipartFile curriculum = getMockMultipartJson(dto);
        List<MockMultipartFile> files = getMockMultipartFileList(2);
        when(factory.getService(any())).thenReturn(putCurriculumService);
        when(putCurriculumService.putLectureProcess(any(), any()))
                .thenReturn(CreateLectureProcessResponse.completeCurriculum(1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(curriculum)
                .file(files.get(0))
                .file(files.get(1))
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestParts(
                                partWithName("request").description("클래스를 처음 저장할 요청 JSON"),
                                partWithName("files").description("클래스 진행 사진").attributes(field("constraints", "curriculumList size와 맞지 않을 경우 예외 발생"))
                        ),
                        requestPartFields("request",
                                fieldWithPath("createLectureStep").type(STRING).description(generateLinkCode(CREATE_LECTURE_STEP)),
                                fieldWithPath("curriculumList[].order").type(NUMBER).description("클래스 커리큘럼 순서"),
                                fieldWithPath("curriculumList[].title").type(STRING).description("클래스 커리큘럼 소제목").attributes(field("constraints", "5자 이상 50자 이하")),
                                fieldWithPath("curriculumList[].detailText").type(STRING).description("커리큘럼 상세 설명").attributes(field("constraints", "10자 이상 300자 이하")),
                                fieldWithPath("curriculumList[].curriculumImageName").description("주지 않아도 되는 필드"),
                                fieldWithPath("curriculumList[].curriculumImageUrl").description("주지 않아도 되는 필드")
                        ),
                        responseFields(
                                fieldWithPath("lectureId").description("만들거나 수정한 클래스 ID"),
                                fieldWithPath("completedStepName").description(generateLinkCode(CREATE_LECTURE_STEP))
                        )
                ));
    }

    @Test
    @DisplayName("커리큘럼 등록하기 / 커리큘럼 입력을 하지 않았을 경우")
    void save_lecture_curriculum_should_not_input_curriculum() throws Exception {

        // given
        CreateLecture.CurriculumRequest dto = CreateLecture.CurriculumRequest.builder()
                .createLectureStep(CURRICULUM).build();
        MockMultipartFile curriculum = getMockMultipartJson(dto);
        List<MockMultipartFile> files = getMockMultipartFileList(2);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(curriculum)
                .file(files.get(0))
                .file(files.get(1))
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    @DisplayName("커리큘럼 등록하기 / 커리큘럼 리스트 내부에 제약조건을 지키지 않았을 경우")
    void save_lecture_curriculum_should_not_constraint() throws Exception {

        // given
        List<MockMultipartFile> files = getMockMultipartFileList(2);
        CreateLecture.CurriculumRequest dto = createNotSatisfiedConstraintCurriculumList();
        MockMultipartFile curriculum = getMockMultipartJson(dto);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(curriculum)
                .file(files.get(0))
                .file(files.get(1))
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(8)));
    }

    @Test
    @DisplayName("스케줄 등록하기")
    void save_lecture_schedules() throws Exception {

        // given
        CreateLecture.ScheduleRequest dto = createScheduleRequest();
        MockMultipartFile json = getMockMultipartJson(dto);
        MockMultipartFile file = getMockMultipartFile();
        when(factory.getService(any())).thenReturn(putScheduleService);
        when(putScheduleService.putLectureProcess(any(), any()))
                .thenReturn(CreateLectureProcessResponse.completeSchedule(1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(json)
                .file(file)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestPartFields("request",
                                fieldWithPath("createLectureStep").type(STRING).description(generateLinkCode(CREATE_LECTURE_STEP)),
                                fieldWithPath("minParticipants").optional().type(NUMBER).description("클래스 진행 최소 인원").attributes(field("constraints","0 또는 1명 이상")),
                                fieldWithPath("maxParticipants").type(NUMBER).description("클래스 진행 최대 인원").attributes(field("constraints","0 또는 1명 이상")),
                                fieldWithPath("scheduleList[]").type(ARRAY).description("스케줄 리스트"),
                                fieldWithPath("scheduleList[].progressMinute").type(NUMBER).description("진행 시간 (분 단위)").attributes(field("constraints","최소 60 이상, 30으로 나눠져야 한다.")),
                                fieldWithPath("scheduleList[].lectureDate").type(STRING).description("진행 날짜").attributes(field("constraint", "yyyy-MM-dd 형식으로 작성")),
                                fieldWithPath("scheduleList[].startHour").description("시작 시간").attributes(field("constraint", "0 ~ 23")),
                                fieldWithPath("scheduleList[].startMinute").description("시작 분").attributes(field("constraint", "0 ~ 59 && 30으로 나뉘어야 한다.")),
                                fieldWithPath("scheduleList[].endHour").optional().description("필요 없는 값").attributes(field("constraint", "NULL")),
                                fieldWithPath("scheduleList[].endMinute").optional().description("필요 없는 값").attributes(field("constraint", "NULL"))
                        ),
                        responseFields(
                                fieldWithPath("lectureId").description("만들거나 수정한 클래스 ID"),
                                fieldWithPath("completedStepName").description(generateLinkCode(CREATE_LECTURE_STEP))
                        )
                ));
    }

    @Test
    @DisplayName("스케줄 등록하기 / Date 빈 값, startHour, endHour 제약조건 체크하기")
    void save_lecture_schedules_should_follow_date_and_hour() throws Exception {

        // given
        CreateLecture.ScheduleRequest dto = createScheduleNonConstraintRequest();
        MockMultipartFile json = getMockMultipartJson(dto);
        MockMultipartFile file = getMockMultipartFile();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(json)
                .file(file)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(5)));
    }

    @Test
    @DisplayName("스케줄 등록하기 / 날짜에 맞지 않는 값, startMinute 60 초과, participants 음수 제약조건 체크하기")
    void save_lecture_schedules_should_divide_and_constraint_minute() throws Exception {

        // given
        CreateLecture.ScheduleRequest dto = createScheduleNonConstraintRequest();
        dto.getScheduleList().get(0).setStartMinute(90);
        dto.setMinParticipants(-1);
        dto.setMaxParticipants(-1);
        MockMultipartFile json = getMockMultipartJson(dto);
        MockMultipartFile file = getMockMultipartFile();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(json)
                .file(file)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(7)));
    }

    @Test
    @DisplayName("쿠폰 및 가격 정보 등록하기")
    void save_lecture_price_coupon() throws Exception {

        // given
        CreateLecture.PriceCouponRequest dto = createPriceCouponRequest();
        MockMultipartFile json = getMockMultipartJson(dto);
        MockMultipartFile file = getMockMultipartFile();
        when(factory.getService(any())).thenReturn(putPriceCouponService);
        when(putPriceCouponService.putLectureProcess(any(), any()))
                .thenReturn(CreateLectureProcessResponse.completePriceCoupon(1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(URL)
                .file(json)
                .file(file)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .with(req -> {
                    req.setMethod(PUT);
                    return req;
                }).contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestPartFields("request",
                                fieldWithPath("createLectureStep").type(STRING).description(generateLinkCode(CREATE_LECTURE_STEP)),
                                fieldWithPath("price.regularPrice").type(NUMBER).description("클래스 표준 가격"),
                                fieldWithPath("price.priceOne").type(NUMBER).description("클래스 1인 결제 가격"),
                                fieldWithPath("price.priceTwo").type(NUMBER).description("클래스 2인 결제 가격"),
                                fieldWithPath("price.priceThree").type(NUMBER).description("클래스 3인 결제 가격"),
                                fieldWithPath("price.priceFour").type(NUMBER).description("클래스 4인 결제 가격"),
                                fieldWithPath("coupon.couponPrice").type(NUMBER).description("쿠폰 할인 가격"),
                                fieldWithPath("coupon.couponCount").type(NUMBER).description("쿠폰 갯수")
                        ),
                        responseFields(
                                fieldWithPath("lectureId").description("만들거나 수정한 클래스 ID"),
                                fieldWithPath("completedStepName").description(generateLinkCode(CREATE_LECTURE_STEP))
                        )
                ));
    }

    @Test
    @DisplayName("정책 동의하기 및 클래스 생성 완료")
    void save_lecture_terms() throws Exception {

        // given
        CreateLecture.TermsRequest dto = CreateLecture.TermsRequest.builder().createLectureStep(TERMS)
                .termsOne(true).termsTwo(true).termsThree(true).termsFour(true).build();
        when(temporaryStorageLectureManageService.saveLecture(any()))
                .thenReturn(TemporaryStorageLectureManageResponse.save(1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJsonBody(dto))
                .contentType(APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("createLectureStep").description(generateLinkCode(CREATE_LECTURE_STEP)),
                                fieldWithPath("termsOne").type(BOOLEAN).description("이용 약관 동의 1").attributes(field("constraints", "반드시 true")),
                                fieldWithPath("termsTwo").type(BOOLEAN).description("이용 약관 동의 2").attributes(field("constraints", "반드시 true")),
                                fieldWithPath("termsThree").type(BOOLEAN).description("이용 약관 동의 3").attributes(field("constraints", "반드시 true")),
                                fieldWithPath("termsFour").type(BOOLEAN).description("이용 약관 동의 4").attributes(field("constraints", "반드시 true"))
                        ),
                        responseFields(
                                fieldWithPath("lectureId").description("검수 대기중 이거나 삭제된 클래스 ID"),
                                fieldWithPath("resultResponse").description(generateLinkCode(LECTURE_SAVE_DELETE_RESPONSE)),
                                fieldWithPath("description").description("결과에 대한 상세 설명")
                        )
                ));
        verify(temporaryStorageLectureManageService).saveLecture(any());
    }

    @Test
    @DisplayName("약관에 동의하지 않을 경우 에러 발생")
    void save_lecture_terms_should_all_true() throws Exception {

        // given
        CreateLecture.TermsRequest dto = CreateLecture.TermsRequest.builder().createLectureStep(TERMS)
                .termsOne(false).termsTwo(false).termsThree(false).termsFour(false).build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJsonBody(dto))
                .contentType(APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(4)));
    }

    @Test
    @DisplayName("접속한 유저가 작성한 임시 저장된 클래스 정보 가져오기 / 비어있을 경우")
    void get_temporary_stored_lecture_not_found() throws Exception {

        // given
        doThrow(new TemporaryNotSaveLectureException())
                .when(temporaryStorageLectureManageService).getTemporaryStoredLecture();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is("LECTURE-404")));
    }

    @Test
    @DisplayName("임시 저장된 클래스 삭제하기")
    void delete_temporary_store_lecture() throws Exception {

        // given
        when(temporaryStorageLectureManageService.deleteTemporaryStorageLecture())
                .thenReturn(TemporaryStorageLectureManageResponse.delete());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultResponse", is("DELETE")))
                .andExpect(jsonPath("$.description", is("임시 저장된 클래스 정보가 삭제되었습니다.")))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        responseFields(
                                fieldWithPath("lectureId").description("검수 대기중 이거나 삭제된 클래스 ID"),
                                fieldWithPath("resultResponse").description(generateLinkCode(LECTURE_SAVE_DELETE_RESPONSE)),
                                fieldWithPath("description").description("결과에 대한 상세 설명")
                        )
                ));
    }

    private CreateLecture.PriceCouponRequest createPriceCouponRequest() {
        return CreateLecture.PriceCouponRequest.builder().createLectureStep(PRICE_COUPON)
                .price(CreateLecture.PriceDto.builder().regularPrice(30000).priceOne(30000).priceTwo(29000).priceThree(28000).priceFour(27000).build())
                .coupon(CreateLecture.CouponDto.builder().couponCount(50).couponPrice(2000).build())
                .build();
    }

    private CreateLecture.ScheduleRequest createScheduleNonConstraintRequest() {
        return CreateLecture.ScheduleRequest.builder().createLectureStep(SCHEDULE)
                .minParticipants(1).maxParticipants(6)
                .scheduleList(List.of(
                        CreateLecture.ScheduleDto.builder()
                                .progressMinute(59)
                                .lectureDate(null)
                                .startHour(24)
                                .startMinute(60)
                                .build()
                        )
                )
                .build();
    }

    private CreateLecture.ScheduleRequest createScheduleRequest() {
        return CreateLecture.ScheduleRequest.builder().createLectureStep(SCHEDULE)
                .minParticipants(1).maxParticipants(6)
                .scheduleList(List.of(
                        CreateLecture.ScheduleDto.builder()
                                .progressMinute(60)
                                .lectureDate(LocalDate.of(2021, 3, 2))
                                .startHour(17)
                                .startMinute(30)
                                .build(),
                        CreateLecture.ScheduleDto.builder()
                                .progressMinute(60)
                                .lectureDate(LocalDate.of(2021, 3, 3))
                                .startHour(17)
                                .startMinute(30)
                                .build()))
                .build();
    }

    private CreateLecture.CurriculumRequest createNotSatisfiedConstraintCurriculumList() {
        return CreateLecture.CurriculumRequest.builder().createLectureStep(CURRICULUM)
                .curriculumList(List.of(
                        CreateLecture.CurriculumDto.builder().title(" ").detailText(" ").build(),
                        CreateLecture.CurriculumDto.builder().title(" ").detailText(" ").build()
                ))
                .build();
    }

    private CreateLecture.IntroRequest createEmptyOrderTextListAndNotOver10MainText() {
        return CreateLecture.IntroRequest.builder().createLectureStep(INTRO)
                .mainText("test")
                .finishedProductInfoList(List.of(
                        CreateLecture.FinishedProductInfoDto.builder().build(),
                        CreateLecture.FinishedProductInfoDto.builder().build()))
                .build();
    }

    private CreateLecture.CurriculumRequest createCurriculumList() {
        return CreateLecture.CurriculumRequest.builder().createLectureStep(CURRICULUM)
                .curriculumList(List.of(
                        CreateLecture.CurriculumDto.builder().order(0).detailText("testtesttest").title("titletitle").build(),
                        CreateLecture.CurriculumDto.builder().order(1).detailText("testtesttest").title("titletitle").build()
                ))
                .build();
    }

    private CreateLecture.IntroRequest createIntro() {
        return CreateLecture.IntroRequest.builder().createLectureStep(INTRO)
                .mainText("testtesttesttest")
                .finishedProductInfoList(List.of(
                        CreateLecture.FinishedProductInfoDto.builder()
                                .order(0).text("testtesttesttest")
                                .build(),
                        CreateLecture.FinishedProductInfoDto.builder()
                                .order(1).text("testtesttesttest")
                                .build(),
                        CreateLecture.FinishedProductInfoDto.builder()
                                .order(2).text("testtesttesttest")
                                .build()))
                .build();
    }

    private CreateLecture.IntroRequest createExceedFinishedListIntro() {
        return CreateLecture.IntroRequest.builder().createLectureStep(INTRO)
                .mainText("testtesttesttest")
                .finishedProductInfoList(List.of(
                        CreateLecture.FinishedProductInfoDto.builder()
                                .order(0).text("testtesttesttest")
                                .build(),
                        CreateLecture.FinishedProductInfoDto.builder()
                                .order(1).text("testtesttesttest")
                                .build(),
                        CreateLecture.FinishedProductInfoDto.builder()
                                .order(2).text("testtesttesttest")
                                .build(),
                        CreateLecture.FinishedProductInfoDto.builder()
                                .order(3).text("testtesttesttest")
                                .build(),
                        CreateLecture.FinishedProductInfoDto.builder()
                                .order(4).text("testtesttesttest")
                                .build(),
                        CreateLecture.FinishedProductInfoDto.builder()
                                .order(5).text("testtesttesttest")
                                .build()
                ))
                .build();
    }

    private MockMultipartFile getMockMultipartJson(Object dto) throws JsonProcessingException {
        return new MockMultipartFile("request",
                "first",
                MediaType.APPLICATION_JSON_VALUE,
                createJsonBody(dto).getBytes());
    }

    private List<MockMultipartFile> getMockMultipartFileList(int count) {
        List<MockMultipartFile> files = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            files.add(getMockMultipartFile());
        }
        return files;
    }

    private MockMultipartFile getMockMultipartFile() {
        return new MockMultipartFile("files",
                "image.img",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "<<image>>".getBytes());
    }

    private String createJsonBody(Object request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }
}