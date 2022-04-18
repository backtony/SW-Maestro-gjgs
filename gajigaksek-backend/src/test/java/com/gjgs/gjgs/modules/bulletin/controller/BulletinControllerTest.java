package com.gjgs.gjgs.modules.bulletin.controller;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.bulletin.dto.BulletinChangeRecruitResponse;
import com.gjgs.gjgs.modules.bulletin.dto.BulletinIdResponse;
import com.gjgs.gjgs.modules.bulletin.dto.CreateBulletinRequest;
import com.gjgs.gjgs.modules.bulletin.dto.RecruitStatus;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.dummy.BulletinDtoDummy;
import com.gjgs.gjgs.modules.exception.lecture.LectureNotFoundException;
import com.gjgs.gjgs.modules.exception.team.TeamMemberMaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.DocUrl;
import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BulletinControllerTest extends RestDocsTestSupport {


    @BeforeEach
    void set_up_mock_user() {
        securityUserMockSetting();
    }

    @Test
    @DisplayName("모집글 생성")
    void create_bulletin() throws Exception {

        // given
        String body = createJson(create_create_bulletin_request());
        when(bulletinService.createBulletin(any()))
                .thenReturn(BulletinIdResponse.builder()
                        .bulletinId(1L)
                        .teamId(1L).lectureId(1L)
                        .build());

        // when, then
        mockMvc.perform(post("/api/v1/bulletins")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bulletinId", is(1)))
                .andExpect(jsonPath("$.teamId", is(1)))
                .andExpect(jsonPath("$.lectureId", is(1)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestFields(
                                fieldWithPath("teamId").type(NUMBER).description("게시글을 작성할 팀의 ID").attributes(field("constraints", "팀의 ID가 없을 경우 예외 발생")),
                                fieldWithPath("title").type(STRING).description("게시글 제목").attributes(field("constraints", "게시글 제목이 5자 미만(공백 포함)일 경우 예외 발생")),
                                fieldWithPath("age").type(STRING).description(generateLinkCode(DocUrl.BULLETIN_AGE)),
                                fieldWithPath("timeType").type(STRING).description(generateLinkCode(DocUrl.TIME_TYPE)),
                                fieldWithPath("text").type(STRING).description("게시글 소개글").attributes(field("constraints", "최소 10자, 최대 500자 입력 가능")),
                                fieldWithPath("lectureId").type(NUMBER).description("지정한 클래스의 ID").attributes(field("constraints", "클래스 ID가 존재하지 않을 경우 예외 발생")),
                                fieldWithPath("dayType").type(STRING).description(generateLinkCode(DocUrl.DAY_TYPE))),
                        responseFields(
                                fieldWithPath("bulletinId").type(NUMBER).description("생성된 모집 게시글의 ID"),
                                fieldWithPath("lectureId").type(NUMBER).description("지정한 클래스의 ID"),
                                fieldWithPath("teamId").type(NUMBER).description("모집글을 생성한 팀의 ID")
                        )));
    }

    @Test
    @DisplayName("모집글 생성 NotNull 테스트")
    void create_bulletin_should_not_null_required_attribute_validation() throws Exception {

        // given
        String body = createJson(create_create_bulletin_not_valid_request());

        // when, then
        mockMvc.perform(post("/api/v1/bulletins")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("모집글 생성시 title과 text의 글자 수 제약조건을 지키지 않을 경우")
    void create_bulletin_should_text_length_size_minimum() throws Exception {

        // given
        CreateBulletinRequest request = create_create_bulletin_request();
        request.setTitle("g");
        request.setText("g");
        String body = createJson(request);

        // when, then
        mockMvc.perform(post("/api/v1/bulletins")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    @DisplayName("모집글 생성시 시간, 나이, 요일 형식이 맞지 않은 값이 올 경우")
    void create_bulletin_should_follow_constraint_time_age_value() throws Exception {

        // given
        CreateBulletinRequest request = create_create_bulletin_request();
        request.setAge("g");
        request.setTimeType("g");
        request.setDayType("g");
        String body = createJson(request);

        // when, then
        mockMvc.perform(post("/api/v1/bulletins")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    @Test
    @DisplayName("지정한 클래스를 찾을 수 없을 경우")
    void create_bulletin_should_pick_lecture() throws Exception {

        // given
        String body = createJson(create_create_bulletin_request());
        when(bulletinService.createBulletin(any()))
                .thenThrow(new LectureNotFoundException());

        // when, then
        mockMvc.perform(post("/api/v1/bulletins")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("LECTURE-404")));
    }

    @Test
    @DisplayName("게시글 삭제(비활성화)")
    void delete_bulletin() throws Exception {

        // given
        when(bulletinService.deleteBulletin(any()))
                .thenReturn(BulletinIdResponse.of(1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/bulletins/{bulletinId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bulletinId", is(1)))
                .andExpect(jsonPath("$.lectureId").isEmpty())
                .andExpect(jsonPath("$.teamId").isEmpty())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("bulletinId").description("모집을 해제할 모집글 ID")
                        ),
                        responseFields(
                                fieldWithPath("bulletinId").type(NUMBER).description("생성된 모집 게시글의 ID"),
                                fieldWithPath("lectureId").type(NULL).optional().description("null"),
                                fieldWithPath("teamId").type(NULL).optional().description("null")
                        )));
    }

    @Test
    @DisplayName("게시글 수정하기")
    void update_bulletin() throws Exception {

        // given
        String body = createJson(create_create_bulletin_request());
        when(bulletinService.modifyBulletin(any(), any()))
                .thenReturn(BulletinIdResponse.of(1L, 1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/bulletins/{bulletinId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bulletinId", is(1)))
                .andExpect(jsonPath("$.lectureId", is(1)))
                .andExpect(jsonPath("$.teamId").isEmpty())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("bulletinId").description("모집글을 수정할 모집글 ID")
                        ),
                        requestFields(
                                fieldWithPath("teamId").type(NUMBER).description("게시글을 작성할 팀의 ID").attributes(field("constraints", "팀의 ID가 없을 경우 예외 발생")),
                                fieldWithPath("title").type(STRING).description("게시글 제목").attributes(field("constraints", "게시글 제목이 5자 미만(공백 포함)일 경우 예외 발생")),
                                fieldWithPath("age").type(STRING).description(generateLinkCode(DocUrl.BULLETIN_AGE)),
                                fieldWithPath("timeType").type(STRING).description(generateLinkCode(DocUrl.TIME_TYPE)),
                                fieldWithPath("text").type(STRING).description("게시글 소개글").attributes(field("constraints", "최소 10자, 최대 500자 입력 가능")),
                                fieldWithPath("lectureId").type(NUMBER).description("지정한 클래스의 ID").attributes(field("constraints", "클래스 ID가 존재하지 않을 경우 예외 발생")),
                                fieldWithPath("dayType").type(STRING).description(generateLinkCode(DocUrl.DAY_TYPE))
                        ),
                        responseFields(
                                fieldWithPath("bulletinId").type(NUMBER).description("생성된 모집 게시글의 ID"),
                                fieldWithPath("lectureId").type(NUMBER).description("수정한 클래스 ID"),
                                fieldWithPath("teamId").type(NULL).optional().description("팀을 바꿀 수 없으므로 null")
                )));
    }

    @Test
    @DisplayName("게시글 검색하기(페이징), 검색어 null 허용")
    void read_bulletin() throws Exception {

        // given
        when(bulletinService.getBulletins(any(), any()))
                .thenReturn(create_search_response());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/bulletins?categoryIdList=1,2,3&zoneId=1&page=0&size=12&keyword=테스트")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.pageable.offset", is(0)))
                .andExpect(jsonPath("$.pageable.pageSize", is(12)))
                .andExpect(jsonPath("$.totalPages", is(9)))
                .andExpect(jsonPath("$.first", is(true)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        requestParameters(
                                parameterWithName("categoryIdList").description("선택한 취미 카테고리"),
                                parameterWithName("zoneId").description("선택한 지역"),
                                parameterWithName("page").description("조회할 페이지 0부터 시작"),
                                parameterWithName("size").description("한 페이지에 보여줄 컨텐츠의 사이즈"),
                                parameterWithName("keyword").description("검색하는 키워드, 공백일 경우 예외 발생")
                        ),
                        responseFields(
                                fieldWithPath("content[].myFavorite").type(BOOLEAN).description("내가 찜한 모집글의 여부"),
                                fieldWithPath("content[].bulletinId").type(NUMBER).description("검색된 모집글의 ID"),
                                fieldWithPath("content[].lectureImageUrl").type(STRING).description("모집글의 썸네일"),
                                fieldWithPath("content[].zoneId").type(NUMBER).description("지역"),
                                fieldWithPath("content[].categoryId").type(NUMBER).description("취미 카테고리"),
                                fieldWithPath("content[].bulletinTitle").type(STRING).description("모집글의 제목"),
                                fieldWithPath("content[].age").type(STRING).description("모집하는 사람들의 나이대"),
                                fieldWithPath("content[].time").type(STRING).description("선호하는 모임 시간대"),
                                fieldWithPath("content[].nowMembers").type(NUMBER).description("현재 팀에 속한 인원"),
                                fieldWithPath("content[].maxMembers").type(NUMBER).description("팀의 최대 인원")
                        ).and(pageDescriptor())
                ));
    }

    @Test
    @DisplayName("게시글 검색하기 / 키워드가 빈 문자열일 경우 에러")
    void get_bulletin_should_not_keyword_is_not_blank() throws Exception {

        // when, then
        mockMvc.perform(get("/api/v1/bulletins?keyword=")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 상세 조회하기")
    void get_bulletin_detail() throws Exception {

        // given
        when(bulletinService.getBulletinDetails(any()))
                .thenReturn(BulletinDtoDummy.createBulletinDetailResponse());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/bulletins/{bulletinId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.bulletinId", is(1)))
                .andExpect(jsonPath("$.bulletinTitle", is("testTitle")))
                .andExpect(jsonPath("$.day", is("testDay")))
                .andExpect(jsonPath("$.time", is("testTime")))
                .andExpect(jsonPath("$.age", is("TWENTYFIVE_TO_THIRTY")))
                .andExpect(jsonPath("$.bulletinText", is("testText")))
                .andExpect(jsonPath("$.bulletinsLecture.lectureId", is(2)))
                .andExpect(jsonPath("$.bulletinsLecture.myFavoriteLecture", is(false)))
                .andExpect(jsonPath("$.bulletinsLecture.lecturesZoneId", is(3)))
                .andExpect(jsonPath("$.bulletinsLecture.lecturesCategoryId", is(4)))
                .andExpect(jsonPath("$.bulletinsLecture.lecturesThumbnailUrl", is("testImageUrl")))
                .andExpect(jsonPath("$.bulletinsLecture.lectureName", is("testLectureTitle")))
                .andExpect(jsonPath("$.bulletinsLecture.priceOne", is(100)))
                .andExpect(jsonPath("$.bulletinsLecture.priceTwo", is(200)))
                .andExpect(jsonPath("$.bulletinsLecture.priceThree", is(300)))
                .andExpect(jsonPath("$.bulletinsLecture.priceFour", is(400)))
                .andExpect(jsonPath("$.bulletinsTeam.teamId", is(10)))
                .andExpect(jsonPath("$.bulletinsTeam.iamLeader", is(false)))
                .andExpect(jsonPath("$.bulletinsTeam.currentPeople", is(3)))
                .andExpect(jsonPath("$.bulletinsTeam.maxPeople", is(4)))
                .andExpect(jsonPath("$.bulletinsTeam.leader.memberId", is(1)))
                .andExpect(jsonPath("$.bulletinsTeam.leader.imageUrl", is("testUrl1")))
                .andExpect(jsonPath("$.bulletinsTeam.leader.nickname", is("testNick1")))
                .andExpect(jsonPath("$.bulletinsTeam.leader.sex", is("M")))
                .andExpect(jsonPath("$.bulletinsTeam.leader.age", is(21)))
                .andExpect(jsonPath("$.bulletinsTeam.leader.text", is("testText1")))
                .andExpect(jsonPath("$.bulletinsTeam.members", hasSize(2)))
                .andExpect(jsonPath("$.bulletinsTeam.members[0].memberId", is(2)))
                .andExpect(jsonPath("$.bulletinsTeam.members[0].imageUrl", is("testUrl2")))
                .andExpect(jsonPath("$.bulletinsTeam.members[0].nickname", is("testNick2")))
                .andExpect(jsonPath("$.bulletinsTeam.members[0].sex", is("M")))
                .andExpect(jsonPath("$.bulletinsTeam.members[0].age", is(22)))
                .andExpect(jsonPath("$.bulletinsTeam.members[0].text", is("testText2")))
                .andExpect(jsonPath("$.bulletinsTeam.members[1].memberId", is(3)))
                .andExpect(jsonPath("$.bulletinsTeam.members[1].imageUrl", is("testUrl3")))
                .andExpect(jsonPath("$.bulletinsTeam.members[1].nickname", is("testNick3")))
                .andExpect(jsonPath("$.bulletinsTeam.members[1].sex", is("M")))
                .andExpect(jsonPath("$.bulletinsTeam.members[1].age", is(23)))
                .andExpect(jsonPath("$.bulletinsTeam.members[1].text", is("testText3")))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("bulletinId").description("상세 조회할 ID")
                        ),
                        responseFields(
                                fieldWithPath("bulletinId").type(NUMBER).description("찾은 모집글 ID"),
                                fieldWithPath("bulletinTitle").type(STRING).description("모집글 제목"),
                                fieldWithPath("day").type(STRING).description("모집글의 선호 만남 요일"),
                                fieldWithPath("time").type(STRING).description("모집글의 선호 만남 시간대"),
                                fieldWithPath("age").type(STRING).description("모집글의 모집원 선호 나이대"),
                                fieldWithPath("bulletinText").type(STRING).description("모집글의 소개글"),
                                fieldWithPath("bulletinsLecture.lectureId").type(NUMBER).description("지정한 클래스 ID"),
                                fieldWithPath("bulletinsLecture.myFavoriteLecture").type(BOOLEAN).description("내가 좋아요 한 클래스인지 여부"),
                                fieldWithPath("bulletinsLecture.lecturesZoneId").type(NUMBER).description("클래스의 지역"),
                                fieldWithPath("bulletinsLecture.lecturesCategoryId").type(NUMBER).description("클래스의 카테고리"),
                                fieldWithPath("bulletinsLecture.lecturesThumbnailUrl").type(STRING).description("클래스 썸네일 URL"),
                                fieldWithPath("bulletinsLecture.lectureName").type(STRING).description("지정한 클래스 이름"),
                                fieldWithPath("bulletinsLecture.priceOne").type(NUMBER).description("지정한 클래스의 1명 신청 가격"),
                                fieldWithPath("bulletinsLecture.priceTwo").type(NUMBER).description("지정한 클래스의 2명 신청 가격"),
                                fieldWithPath("bulletinsLecture.priceThree").type(NUMBER).description("지정한 클래스의 3명 신청 가격"),
                                fieldWithPath("bulletinsLecture.priceFour").type(NUMBER).description("지정한 클래스의 4명 신청 가격"),
                                fieldWithPath("bulletinsTeam.teamId").type(NUMBER).description("모집하는 팀의 ID"),
                                fieldWithPath("bulletinsTeam.iamLeader").type(BOOLEAN).description("내가 리더인지 판별"),
                                fieldWithPath("bulletinsTeam.currentPeople").type(NUMBER).description("현재 팀에 속한 인원 수"),
                                fieldWithPath("bulletinsTeam.maxPeople").type(NUMBER).description("팀의 최대 모집 인원 수"),
                                fieldWithPath("bulletinsTeam.leader.memberId").type(NUMBER).description("리더의 ID"),
                                fieldWithPath("bulletinsTeam.leader.imageUrl").type(STRING).description("리더의 프로필 URL"),
                                fieldWithPath("bulletinsTeam.leader.nickname").type(STRING).description("리더의 닉네임"),
                                fieldWithPath("bulletinsTeam.leader.sex").type(STRING).description("리더의 성별"),
                                fieldWithPath("bulletinsTeam.leader.age").type(NUMBER).description("리더의 나이"),
                                fieldWithPath("bulletinsTeam.leader.text").type(STRING).description("리더의 소개글"),
                                fieldWithPath("bulletinsTeam.members[0].memberId").type(NUMBER).description("팀원의 ID"),
                                fieldWithPath("bulletinsTeam.members[0].imageUrl").type(STRING).description("팀원의 프로필 URL"),
                                fieldWithPath("bulletinsTeam.members[0].nickname").type(STRING).description("팀원의 닉네임"),
                                fieldWithPath("bulletinsTeam.members[0].sex").type(STRING).description("팀원의 성별"),
                                fieldWithPath("bulletinsTeam.members[0].age").type(NUMBER).description("팀원의 나이"),
                                fieldWithPath("bulletinsTeam.members[0].text").type(STRING).description("팀원의 소개글")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("게시글 모집 상태로 변경하기")
    void update_bulletin_change_status() throws Exception {

        // given
        when(bulletinService.changeRecruitStatus(1L))
                .thenReturn(BulletinChangeRecruitResponse.builder()
                        .bulletinId(1L).recruitStatus(RecruitStatus.RECRUIT.name())
                        .build());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/bulletins/{bulletinId}/recruit", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.bulletinId", is(1)))
                .andExpect(jsonPath("$.recruitStatus", is("RECRUIT")))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("bulletinId").description("상세 조회할 ID")
                        ),
                        responseFields(
                                fieldWithPath("bulletinId").type(NUMBER).description("모집 상태를 변경한 모집글"),
                                fieldWithPath("recruitStatus").type(STRING).description("변경된 모집 상태")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 모집 상태로 변경하기 / 인원이 다 차있을 경우")
    void update_bulletin_should_not_team_members_full() throws Exception {

        // given
        when(bulletinService.changeRecruitStatus(any()))
                .thenThrow(new TeamMemberMaxException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/bulletins/{bulletinId}/recruit", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.code", is("TEAM-400")))
                .andExpect(status().isBadRequest());
    }

    private Page<BulletinSearchResponse> create_search_response() {
        return new PageImpl<>(BulletinDtoDummy.createBulletinSearchResponseListForOne(),
                PageRequest.of(0, 12),
                100);
    }

    private CreateBulletinRequest create_create_bulletin_not_valid_request() {
        return CreateBulletinRequest.builder()
                .text("test")
                .build();
    }

    private CreateBulletinRequest create_create_bulletin_request() {
        return CreateBulletinRequest.builder()
                .teamId(1L)
                .title("testtest")
                .age("TWENTY_TO_TWENTYFIVE")
                .timeType("MORNING")
                .text("testststest")
                .lectureId(1L)
                .dayType("MON")
                .build();
    }
}
