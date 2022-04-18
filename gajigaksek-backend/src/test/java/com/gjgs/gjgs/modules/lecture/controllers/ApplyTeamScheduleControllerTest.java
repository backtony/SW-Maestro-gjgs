package com.gjgs.gjgs.modules.lecture.controllers;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.exception.schedule.*;
import com.gjgs.gjgs.modules.lecture.dtos.apply.ApplyLectureTeamRequest;
import com.gjgs.gjgs.modules.lecture.dtos.apply.ApplyLectureTeamResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApplyTeamScheduleControllerTest extends RestDocsTestSupport {


    private final String TEAM_URL = "/api/v1/schedules/{scheduleId}";
    private final String TEAM_CANCEL_URL = "/api/v1/schedules/{scheduleId}/teams/{teamId}";

    @BeforeEach
    void setUpMockUser() {
        securityUserMockSetting();
    }

    @Test
    @DisplayName("팀 신청")
    void apply_team() throws Exception {

        // given
        ApplyLectureTeamRequest request = getApplyLectureRequest();
        when(applyTeamService.apply(any(), any())).thenReturn(ApplyLectureTeamResponse.builder()
                .scheduleId(1L)
                .build());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(TEAM_URL, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(request))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(parameterWithName("scheduleId").description("신청할 스케줄 ID")),
                        requestFields(
                                fieldWithPath("teamId").type(NUMBER).description("신청할 팀 ID").attributes(field("constraints", "NOT NULL")),
                                fieldWithPath("lectureId").type(NUMBER).description("신청할 클래스 ID").attributes(field("constraints", "NOT NULL"))
                        ),
                        responseFields(
                                fieldWithPath("scheduleId").type(NUMBER).description("신청 완료된 스케줄 ID")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("팀 신청 / 신청할 팀과 클래스 ID가 없을 경우")
    void apply_team_should_not_null_team_id_lecture_id() throws Exception {

        // given
        ApplyLectureTeamRequest request = ApplyLectureTeamRequest.builder().build();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(TEAM_URL, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(request))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팀 신청 / 팀 신청은 스케줄을 한 시간 전에 신청해야 한다.")
    void apply_team_should_team_apply_before_one_hour() throws Exception {

        // given
        ApplyLectureTeamRequest request = getApplyLectureRequest();
        when(applyTeamService.apply(any(), any()))
                .thenThrow(new ScheduleTimeOverException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(TEAM_URL, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(request))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팀 신청 / 신청시 해당 스케줄이 모집중이지 않을 경우")
    void apply_team_should_schedule_status_is_recruit() throws Exception {

        // given
        ApplyLectureTeamRequest request = getApplyLectureRequest();
        when(applyTeamService.apply(any(), any()))
                .thenThrow(new ScheduleNotRecruitException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(TEAM_URL, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(request))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팀 신청 / 신청시 해당 스케줄을 이미 신청한 팀원이 있는 경우")
    void apply_team_should_not_exist_previous_apply_member() throws Exception {

        // given
        ApplyLectureTeamRequest request = getApplyLectureRequest();
        when(applyTeamService.apply(any(), any()))
                .thenThrow(new AlreadyEnteredScheduledException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(TEAM_URL, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(request))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("팀 신청 / 신청시 본인의 클래스에 신청한 경우")
    void apply_team_should_not_member_is_own_lecture() throws Exception {

        // given
        ApplyLectureTeamRequest request = getApplyLectureRequest();
        when(applyTeamService.apply(any(), any()))
                .thenThrow(new DirectorCanNotParticipateMyClassException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(TEAM_URL, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(request))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팀 신청 / 신청시 참가 인원이 넘은 경우")
    void apply_team_should_not_over_participants() throws Exception {

        // given
        ApplyLectureTeamRequest request = getApplyLectureRequest();
        when(applyTeamService.apply(any(), any()))
                .thenThrow(new ExceedScheduleParticipantException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(TEAM_URL, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(request))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("팀 신청 취소")
    void delete_apply_team() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(TEAM_CANCEL_URL, 1, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("scheduleId").description("신청 취소 할 스케줄 ID"),
                                parameterWithName("teamId").description("신청 취소 할 팀 ID")
                        )
                ));
    }

    private ApplyLectureTeamRequest getApplyLectureRequest() {
        return ApplyLectureTeamRequest.builder()
                .teamId(1L).lectureId(1L).build();
    }
}
