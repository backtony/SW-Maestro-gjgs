package com.gjgs.gjgs.modules.team.controllers;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.exception.payment.TeamHaveWaitOrderException;
import com.gjgs.gjgs.modules.exception.team.*;
import com.gjgs.gjgs.modules.team.dtos.DelegateLeaderResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamAppliersResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamExitResponse;
import com.gjgs.gjgs.modules.team.dtos.TeamManageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeamManageControllerTest extends RestDocsTestSupport {

    private final String URL = "/api/v1/teams/{teamId}";


    @BeforeEach
    void setUpMockUser() {
        securityUserMockSetting();
    }

    @Test
    @DisplayName("공통 에러 1. 팀 리더가 아닐 경우")
    void common_errors_not_leader_exception() throws Exception {

        // given
        when(teamManageService.acceptApplier(any(), any()))
                .thenThrow(new NotTeamLeaderException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL + "/appliers/{applierId}", 1, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))

                .andExpect(jsonPath("$.code", is("TEAM-403")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("공통 에러 2. 팀 멤버가 아닐 경우")
    void common_errors_not_team_member_exception() throws Exception {

        // given
        when(teamManageService.acceptApplier(any(), any()))
                .thenThrow(new NotMemberOfTeamException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL + "/appliers/{applierId}", 1, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                
                .andExpect(jsonPath("$.code", is("TEAM-404")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("공통 에러 3. 리더 위임, 팀원 추방, 나가기 시 대기 중인 주문이 있을 경우")
    void common_errors_has_wait_order_exception() throws Exception {

        // given
        when(teamManageService.changeLeader(1L, 2L))
                .thenThrow(new TeamHaveWaitOrderException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(URL + "/members/{memberId}", 1, 2)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                ;
    }

    @Test
    @DisplayName("팀 가입 신청하기")
    void apply_team() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL + "/appliers", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("신청할 팀 ID")
                        )
                ));
    }

    @Test
    @DisplayName("팀 가입 신청시, 팀이 없을 경우 예외 발생")
    void apply_team_not_found_exception() throws Exception {

        // given
        doThrow(new TeamNotFoundException())
                .when(teamManageService).applyTeam(any());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL + "/appliers", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.code", is("TEAM-404")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("팀 가입 신청시, 현재 팀이 모집중이지 않을 경우 예외 발생")
    void apply_team_should_recruit_status() throws Exception {

        // given
        doThrow(new TeamNotRecruitmentException())
                .when(teamManageService).applyTeam(any());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL + "/appliers", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.code", is("TEAM-400")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팀 가입 신청시, 신청자가 그룹에 속해 있을 경우 예외 발생")
    void apply_team_should_not_in_team_members() throws Exception {

        // given
        doThrow(new AlreadyInTeamException())
                .when(teamManageService).applyTeam(any());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL + "/appliers", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.code", is("TEAM-409")))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("그룹 가입 승인하기")
    void accept_applier() throws Exception {

        // given
        Long teamId = 1L;
        Long applierId = 1L;
        when(teamManageService.acceptApplier(teamId, applierId))
                .thenReturn(TeamManageResponse.of(teamId, applierId, true));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL + "/appliers/{applierId}", 1, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.teamId", is(1)))
                .andExpect(jsonPath("$.memberId", is(1)))
                .andExpect(jsonPath("$.accept", is(true)))
                .andExpect(jsonPath("$.reject", is(false)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("승인할 팀 ID"),
                                parameterWithName("applierId").description("신청한 멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("teamId").type(NUMBER).description("팀 ID"),
                                fieldWithPath("memberId").type(NUMBER).description("승인한 멤버 ID"),
                                fieldWithPath("accept").type(BOOLEAN).description("가입 승인 여부"),
                                fieldWithPath("reject").type(BOOLEAN).description("가입 거절 여부")
                        )
                ));

    }

    @Test
    @DisplayName("그룹 가입 거절하기")
    void reject_applier() throws Exception {

        // given
        Long teamId = 1L;
        Long applierId = 1L;
        when(teamManageService.rejectApplier(teamId, applierId))
                .thenReturn(TeamManageResponse.of(teamId, applierId, false));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(URL + "/appliers/{applierId}", 1, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.teamId", is(1)))
                .andExpect(jsonPath("$.memberId", is(1)))
                .andExpect(jsonPath("$.accept", is(false)))
                .andExpect(jsonPath("$.reject", is(true)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("거절할 팀 ID"),
                                parameterWithName("applierId").description("신청한 멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("teamId").type(NUMBER).description("팀 ID"),
                                fieldWithPath("memberId").type(NUMBER).description("승인한 멤버 ID"),
                                fieldWithPath("accept").type(BOOLEAN).description("가입 승인 여부"),
                                fieldWithPath("reject").type(BOOLEAN).description("가입 거절 여부")
                        )
                ));
    }

    @Test
    @DisplayName("그룹 가입 신청자들 조회하기")
    void get_team_appliers() throws Exception {

        // given
        when(teamManageService.getTeamAppliers(any()))
                .thenReturn(createTeamAppliersResponse());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL + "/appliers", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applierList", hasSize(1)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("신청자들을 조회할 팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("applierList[0].memberId").type(NUMBER).description("신청한 멤버 ID"),
                                fieldWithPath("applierList[0].thumbnailImageUrl").type(STRING).description("신청한 멤버의 프로필 URL"),
                                fieldWithPath("applierList[0].nickname").type(STRING).description("신청한 멤버 닉네임"),
                                fieldWithPath("applierList[0].sex").type(STRING).description("신청한 멤버 성별"),
                                fieldWithPath("applierList[0].age").type(NUMBER).description("신청한 멤버 나이")
                        )
                ));
    }

    @Test
    @DisplayName("팀에 속한 멤버 추방하기")
    void exclude_team_member() throws Exception {

        // given
        Long teamId = 1L;
        Long memberId = 2L;
        when(teamManageService.excludeMember(teamId, memberId))
                .thenReturn(TeamExitResponse.excludeMember(teamId, memberId));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(URL + "/members/{memberId}", 1, 2)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.teamId", is(1)))
                .andExpect(jsonPath("$.memberId", is(2)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("추방할 팀 ID"),
                                parameterWithName("memberId").description("추방할 멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("teamId").type(NUMBER).description("추방한 팀 ID"),
                                fieldWithPath("memberId").type(NUMBER).description("추방한 멤버의 ID"),
                                fieldWithPath("result").type(STRING).description("결과")
                        )
                ));
    }

    @Test
    @DisplayName("팀 나가기(팀원)")
    void exit_team_member() throws Exception {

        // given
        Long teamId = 1L;
        Long exitMemberId = 2L;
        when(teamManageService.exitMember(teamId))
                .thenReturn(TeamExitResponse.exitMember(teamId, exitMemberId));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(URL + "/members", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.teamId", is(1)))
                .andExpect(jsonPath("$.memberId", is(2)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("나갈 팀 ID")
                        ),
                        responseFields(
                                fieldWithPath("teamId").type(NUMBER).description("나간 팀 ID"),
                                fieldWithPath("memberId").type(NUMBER).description("나간 멤버 ID"),
                                fieldWithPath("result").type(STRING).description("결과")
                        )
                ));
    }

    @Test
    @DisplayName("리더 위임하기")
    void delegate_team_leader() throws Exception {

        // given
        when(teamManageService.changeLeader(1L, 2L))
                .thenReturn(DelegateLeaderResponse.of(1L, 2L, 1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(URL + "/members/{memberId}", 1, 2)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.teamId", is(1)))
                .andExpect(jsonPath("$.changedLeaderId", is(2)))
                .andExpect(jsonPath("$.toTeamMemberId", is(1)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("위임할 팀 ID"),
                                parameterWithName("memberId").description("위임할 멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("teamId").type(NUMBER).description("위임한 팀 ID"),
                                fieldWithPath("changedLeaderId").type(NUMBER).description("리더가 된 멤버 ID"),
                                fieldWithPath("toTeamMemberId").type(NUMBER).description("팀원이 된 멤버 ID")
                        )
                ));
    }

    private TeamAppliersResponse createTeamAppliersResponse() {
        return TeamAppliersResponse.builder()
                .applierList(Set.of(
                        TeamAppliersResponse
                                .TeamApplierResponse
                                .builder()
                                .memberId(1L)
                                .nickname("applier1")
                                .sex("M")
                                .age(20)
                                .thumbnailImageUrl("test")
                                .build())).build();
    }
}
