package com.gjgs.gjgs.modules.team.controllers;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.exception.team.NotTeamLeaderException;
import com.gjgs.gjgs.modules.exception.team.TeamMemberCountLargerThanEditCountException;
import com.gjgs.gjgs.modules.exception.team.TeamNotFoundException;
import com.gjgs.gjgs.modules.exception.zone.ZoneNotFoundException;
import com.gjgs.gjgs.modules.team.dtos.*;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.List;
import java.util.Set;

import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class TeamControllerTest extends RestDocsTestSupport {

    private final String URL = "/api/v1/teams";

    @BeforeEach
    void setUpMockUser() {
        securityUserMockSetting();
    }

    @Test
    @DisplayName("공통 에러 1. 팀장 권한이 없을 경우")
    void team_common_errors_should_need_leader_authorization() throws Exception {

        // given
        when(teamService.deleteTeam(any()))
                .thenThrow(new NotTeamLeaderException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(URL + "/{teamId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("공통 에러 2. 팀이 존재하지 않을 경우")
    void common_errors_team_not_found_exception() throws Exception {

        // given
        when(teamService.getTeamDetail(any()))
                .thenThrow(new TeamNotFoundException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL + "/{teamId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("팀 생성하기")
    void create_team() throws Exception {

        // given
        CreateTeamRequest req = createCreateTeamRequest();
        String body = createJson(req);
        stubbingZoneRepository(req.getZoneId());
        stubbingCategoryRepository(req.getCategoryList());
        when(teamService.createTeam(any()))
                .thenReturn(CreateTeamResponse.builder().createdTeamId("1").build());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdTeamId", is("1")))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token")
                        ),
                        requestFields(
                                fieldWithPath("teamName").type(STRING).description("팀 명").attributes(field("constraints", "Not Blank")),
                                fieldWithPath("maxPeople").type(NUMBER).description("팀 최대 인원").attributes(field("constraints", "1 ~ 4")),
                                fieldWithPath("zoneId").type(NUMBER).description("지역 ID").attributes(field("constraints", "Not Null")),
                                fieldWithPath("dayType").type(STRING).description("만남 선호 요일").attributes(field("constraints", "'|'를 구분자로 사용, Not Blank")),
                                fieldWithPath("timeType").type(STRING).description("만남 선호 시간").attributes(field("constraints", "Not Blank")),
                                fieldWithPath("categoryList[]").type(ARRAY).description("관심있는 취미 카테고리").attributes(field("constraints", "하나 이상 선택할 것"))
                        ),
                        responseFields(
                                fieldWithPath("createdTeamId").description("생성된 팀 ID")
                        )
                ));
    }

    @Test
    @DisplayName("팀 생성, 시간과 요일을 형식에 맞지 않게 값을 입력할 경우")
    void create_team_should_follow_day_time_constraints() throws Exception {

        // given
        CreateTeamRequest createTeamRequest = createCreateTeamRequest();
        // set DayType for exception
        createTeamRequest.setDayType("BAR|FOO");
        createTeamRequest.setTimeType("DAWN|DAYBREAK");

        String body = createJson(createTeamRequest);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("FORM-400")));
    }

    @Test
    @DisplayName("팀 생성, DTO에 모든 값 null")
    void create_team_should_follow_constraint() throws Exception {

        // given
        String body = createJson(createCreateTeamRequestValidation());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.length()", is(7)));
    }

    @Test
    @DisplayName("팀 생성, 선택한 지역id가 없을 경우 예외처리")
    void create_team_zone_not_found_exception() throws Exception {

        // given
        CreateTeamRequest request = createCreateTeamRequest();
        String body = createJson(request);
        stubbingCategoryRepository(request.getCategoryList());
        stubbingZoneRepository(request.getZoneId());
        when(teamService.createTeam(any()))
                .thenThrow(new ZoneNotFoundException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body).contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ZONE-400"));
    }

    @Test
    @DisplayName("팀 생성, 선택한 취미id가 없을 경우 예외처리")
    void create_team_category_not_found_exception() throws Exception {

        // given
        CreateTeamRequest request = createCreateTeamRequest();
        String body = createJson(request);
        when(categoryRepository.countCategoryByIdList(request.getCategoryList()))
                .thenReturn((long) request.getCategoryList().size() - 1);

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field", is("categoryList")))
                .andExpect(jsonPath("$.errors[1].field", is("zoneId")))
                ;
    }

    @Test
    @DisplayName("내 팀 조회하기")
    void get_my_teams() throws Exception {

        // given
        when(teamService.getMyTeamList())
                .thenReturn(stubMyTeamListResponse());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.myTeamList", hasSize(1)))
                .andExpect(jsonPath("$.myTeamList[0].categoryList", hasSize(1)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token")
                        ),
                        responseFields(
                                fieldWithPath("myTeamList[0].teamId").type(NUMBER).description("팀 ID"),
                                fieldWithPath("myTeamList[0].teamName").type(STRING).description("팀명"),
                                fieldWithPath("myTeamList[0].applyPeople").type(NUMBER).description("현재 팀의 인원"),
                                fieldWithPath("myTeamList[0].maxPeople").type(NUMBER).description("팀이 모집할 수 있는 최대 인원"),
                                fieldWithPath("myTeamList[0].iamLeader").type(BOOLEAN).description("내가 리더인지 여부"),
                                fieldWithPath("myTeamList[0].categoryList[]").type(ARRAY).description("해당 팀의 취미 카테고리 ID들")
                        )
                ));
    }

    @Test
    @DisplayName("팀 수정하기")
    void update_team() throws Exception {

        // given
        CreateTeamRequest req = createCreateTeamRequest();
        String body = createJson(req);
        stubbingZoneRepository(req.getZoneId());
        stubbingCategoryRepository(req.getCategoryList());
        when(teamService.modifyTeam(any(), any()))
                .thenReturn(ModifyTeamResponse.of(1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(URL +  "/{teamId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modifiedTeamId", is(1)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT Access Token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("수정할 팀의 ID")
                        ),
                        requestFields(
                                fieldWithPath("teamName").type(STRING).description("팀 생성 Request와 동일").attributes(field("constraints", "Not Blank")),
                                fieldWithPath("maxPeople").type(NUMBER).description("팀 생성 Request와 동일").attributes(field("constraints", "1 ~ 4")),
                                fieldWithPath("zoneId").type(NUMBER).description("팀 생성 Request와 동일").attributes(field("constraints", "Not Null")),
                                fieldWithPath("dayType").type(STRING).description("팀 생성 Request와 동일").attributes(field("constraints", "'|'를 구분자로 사용, Not Blank")),
                                fieldWithPath("timeType").type(STRING).description("팀 생성 Request와 동일").attributes(field("constraints", "Not Blank")),
                                fieldWithPath("categoryList[]").type(ARRAY).description("팀 생성 Request와 동일").attributes(field("constraints", "하나 이상 선택할 것"))
                        ),
                        responseFields(
                                fieldWithPath("modifiedTeamId").type(NUMBER).description("수정한 팀 ID")
                        )
                ));
    }

    @Test
    @DisplayName("팀 수정, 현재 인원보다 적은 그룹 최대 인원 예외 확인")
    void update_team_should_modify_member_count_over_current_member_count() throws Exception {

        // given
        CreateTeamRequest request = createCreateTeamRequest();
        String body = createJson(request);
        when(zoneRepository.existsById(any())).thenReturn(true);
        when(categoryRepository.countCategoryByIdList(any())).thenReturn((long) request.getCategoryList().size());
        when(teamService.modifyTeam(any(), any()))
                .thenThrow(new TeamMemberCountLargerThanEditCountException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(URL + "/{teamId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(body)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is("TEAM-400")))
                ;
    }

    @Test
    @DisplayName("팀 상세 조회")
    void get_team_detail() throws Exception {

        // given
        when(teamService.getTeamDetail(any()))
                .thenReturn(stubMyTeamDetailResponse());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL + "/{teamId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName", is("테스트1")))
                .andExpect(jsonPath("$.day", is("MON")))
                .andExpect(jsonPath("$.time", is("MORNING")))
                .andExpect(jsonPath("$.applyPeople", is(2)))
                .andExpect(jsonPath("$.maxPeople", is(3)))
                .andExpect(jsonPath("$.zoneId", is(1)))
                .andExpect(jsonPath("$.iamLeader", is(true)))
                .andExpect(jsonPath("$.teamsLeader.nickname", is("leader")))
                .andExpect(jsonPath("$.categoryList", hasSize(2)))
                .andExpect(jsonPath("$.teamMemberList", hasSize(1)))
                .andExpect(jsonPath("$.favoriteLectureList", hasSize(1)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("상세 조회할 팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("teamName").type(STRING).description("팀 명"),
                                fieldWithPath("day").type(STRING).description("만남 선호 요일"),
                                fieldWithPath("time").type(STRING).description("만남 선호 시간대"),
                                fieldWithPath("applyPeople").type(NUMBER).description("현재 인원"),
                                fieldWithPath("maxPeople").type(NUMBER).description("팀의 최대 인원"),
                                fieldWithPath("zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("iamLeader").type(BOOLEAN).description("내가 리더인지 여부"),
                                fieldWithPath("teamsLeader.memberId").type(NUMBER).description("리더의 멤버 ID"),
                                fieldWithPath("teamsLeader.imageURL").type(STRING).description("리더의 프로필 사진 URL"),
                                fieldWithPath("teamsLeader.nickname").type(STRING).description("리더의 닉네임"),
                                fieldWithPath("teamsLeader.sex").type(STRING).description("리더의 성별"),
                                fieldWithPath("teamsLeader.age").type(NUMBER).description("리더의 나이"),
                                fieldWithPath("teamsLeader.text").type(STRING).description("리더의 소개글"),
                                fieldWithPath("categoryList[]").type(ARRAY).description("지역 카테고리 리스트"),
                                fieldWithPath("teamMemberList[0].memberId").type(NUMBER).description("팀에 속한 멤버 ID"),
                                fieldWithPath("teamMemberList[0].imageURL").type(STRING).description("팀에 속한 팀원의 프로필 사진 URL"),
                                fieldWithPath("teamMemberList[0].nickname").type(STRING).description("팀에 속한 팀원의 닉네임"),
                                fieldWithPath("teamMemberList[0].sex").type(STRING).description("팀에 속한 팀원의 성별"),
                                fieldWithPath("teamMemberList[0].age").type(NUMBER).description("팀에 속한 팀원의 나이"),
                                fieldWithPath("teamMemberList[0].text").type(STRING).description("팀에 속한 팀원의 소개글"),
                                fieldWithPath("favoriteLectureList[0].lectureId").type(NUMBER).description("팀이 찜한 클래스 ID"),
                                fieldWithPath("favoriteLectureList[0].lecturesZoneId").type(NUMBER).description("팀이 찜한 클래스 지역 ID"),
                                fieldWithPath("favoriteLectureList[0].lecturesTitle").type(STRING).description("팀이 찜한 클래스 제목"),
                                fieldWithPath("favoriteLectureList[0].lecturesPrice").type(NUMBER).description("팀이 찜한 클래스 표준 가격"),
                                fieldWithPath("favoriteLectureList[0].lecturesImageURL").type(STRING).description("클래스 사진 URL"),
                                fieldWithPath("favoriteLectureList[0].myFavoriteLecture").type(BOOLEAN).description("내가 찜한 클래스 여부")
                        )
                ));
    }

    @Test
    @DisplayName("팀 삭제하기")
    void delete_team() throws Exception {

        // given
        Long teamId = 1L;
        when(teamService.deleteTeam(teamId))
                .thenReturn(TeamExitResponse.teamDelete(teamId, 100L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(URL + "/{teamId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.teamId", is(1)))
                .andExpect(jsonPath("$.memberId", is(100)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("삭제할 팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("teamId").type(NUMBER).description("삭제한 팀 ID"),
                                fieldWithPath("memberId").type(NUMBER).description("삭제한 리더 ID"),
                                fieldWithPath("result").type(STRING).description("결과")
                        )
                ));
    }

    @Test
    @DisplayName("내가 팀장인 팀 가져오기")
    void get_my_lead_teams() throws Exception {

        // given
        when(teamService.getMyLeadTeamWithBulletinLecture())
                .thenReturn(createMyLeadTeamResponse());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL + "/lead")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.myLeadTeams", hasSize(1)))
                .andExpect(jsonPath("$.myLeadTeams[0].teamId", is(1)))
                .andExpect(jsonPath("$.myLeadTeams[0].teamName", is("test")))
                .andExpect(jsonPath("$.myLeadTeams[0].hasBulletin", is(true)))
                .andExpect(jsonPath("$.myLeadTeams[0].teamsRecruit", is(true)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        responseFields(
                                fieldWithPath("myLeadTeams[0].teamId").type(NUMBER).description("내가 팀장인 팀의 ID"),
                                fieldWithPath("myLeadTeams[0].teamName").type(STRING).description("내가 팀장인 팀명"),
                                fieldWithPath("myLeadTeams[0].hasBulletin").type(BOOLEAN).description("내가 팀장인 팀이 모집글이 있는지 여부"),
                                fieldWithPath("myLeadTeams[0].teamsRecruit").type(BOOLEAN).description("내가 팀장인 팀이 모집중인지 여부"),
                                fieldWithPath("myLeadTeams[0].bulletinData.bulletinId").type(NUMBER).description("모집글 ID"),
                                fieldWithPath("myLeadTeams[0].bulletinData.bulletinTitle").type(STRING).description("모집글 제목"),
                                fieldWithPath("myLeadTeams[0].bulletinData.age").type(STRING).description("모집하는 나이대"),
                                fieldWithPath("myLeadTeams[0].bulletinData.time").type(STRING).description("모집하는 시간대"),
                                fieldWithPath("myLeadTeams[0].bulletinData.text").type(STRING).description("모집글 소개글"),
                                fieldWithPath("myLeadTeams[0].bulletinData.day").type(STRING).description("모집하는 요일"),
                                fieldWithPath("myLeadTeams[0].lectureData.lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("myLeadTeams[0].lectureData.lectureTitle").type(STRING).description("클래스 제목"),
                                fieldWithPath("myLeadTeams[0].lectureData.lectureImageUrl").type(STRING).description("클래스 썸네일 URL"),
                                fieldWithPath("myLeadTeams[0].lectureData.zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("myLeadTeams[0].lectureData.categoryId").type(NUMBER).description("취미 카테고리 ID"),
                                fieldWithPath("myLeadTeams[0].lectureData.priceOne").type(NUMBER).description("클래스 1인 신청 가격"),
                                fieldWithPath("myLeadTeams[0].lectureData.priceTwo").type(NUMBER).description("클래스 2인 신청 가격"),
                                fieldWithPath("myLeadTeams[0].lectureData.priceThree").type(NUMBER).description("클래스 3인 신청 가격"),
                                fieldWithPath("myLeadTeams[0].lectureData.priceFour").type(NUMBER).description("클래스 4인 신청 가격")
                        )
                ));
    }

    private MyLeadTeamsResponse createMyLeadTeamResponse() {
        return MyLeadTeamsResponse.builder()
                .myLeadTeams(List.of(MyLeadTeamsResponse.MyLeadTeamsWithBulletin.builder()
                        .teamId(1L).teamName("test").teamsRecruit(true).hasBulletin(true)
                        .bulletinData(
                                MyLeadTeamsResponse.BulletinData.builder()
                                        .bulletinId(1L)
                                        .bulletinTitle("test")
                                        .age("test")
                                        .time("test")
                                        .text("test")
                                        .day("test")
                                        .build()
                        )
                        .lectureData(
                                MyLeadTeamsResponse.LectureData.builder()
                                        .lectureId(1L)
                                        .lectureTitle("test")
                                        .lectureImageUrl("test")
                                        .zoneId(1L)
                                        .categoryId(1L)
                                        .priceOne(1000)
                                        .priceTwo(1000)
                                        .priceThree(1000)
                                        .priceFour(1000)
                                        .build()
                        )
                        .build()))
                .build();
    }

    private void stubbingCategoryRepository(List<Long> categoryList) {
        when(categoryRepository.countCategoryByIdList(categoryList)).thenReturn((long) categoryList.size());
    }

    private void stubbingZoneRepository(Long zoneId) {
        when(zoneRepository.existsById(zoneId)).thenReturn(true);
    }

    private CreateTeamRequest createCreateTeamRequestValidation() {
        return CreateTeamRequest.builder().build();
    }

    private CreateTeamRequest createCreateTeamRequest() {
        return CreateTeamRequest.builder()
                .teamName("test")
                .maxPeople(4)
                .zoneId(1L)
                .timeType("MORNING")
                .dayType("MON|TUE")
                .categoryList(List.of(1L, 2L, 3L))
                .build();
    }

    private MyTeamListResponse stubMyTeamListResponse() {
        return MyTeamListResponse
                .builder()
                .myTeamList(
                        Set.of(MyTeamListResponse.MyTeam.builder()
                                .teamId(1L)
                                .teamName("테스트1")
                                .applyPeople(3)
                                .maxPeople(4)
                                .iAmLeader(false)
                                .categoryList(Set.of(1L))
                                .build())
                ).build();
    }

    private TeamDetailResponse stubMyTeamDetailResponse() {
        return TeamDetailResponse.builder()
                .teamName("테스트1")
                .day("MON")
                .time("MORNING")
                .applyPeople(2)
                .maxPeople(3)
                .zoneId(1L)
                .iAmLeader(true)
                .teamsLeader(
                        TeamDetailResponse.TeamMembers.builder()
                                .memberId(1L)
                                .imageURL("test")
                                .nickname("leader")
                                .sex("M")
                                .age(20)
                                .text("test")
                                .build())
                .categoryList(Set.of(1L, 2L))
                .teamMemberList(Set.of(
                        TeamDetailResponse.TeamMembers.builder()
                                .memberId(1L)
                                .imageURL("test")
                                .nickname("member1")
                                .sex("M")
                                .age(20)
                                .text("test")
                                .build()))
                .favoriteLectureList(Set.of(
                        TeamDetailResponse.FavoriteLecture.builder()
                                .lectureId(1L)
                                .lecturesZoneId(1L)
                                .lecturesTitle("lecture1")
                                .lecturesPrice(20000)
                                .lecturesImageURL("test")
                                .myFavoriteLecture(false)
                                .build()))
                .build();
    }
}
