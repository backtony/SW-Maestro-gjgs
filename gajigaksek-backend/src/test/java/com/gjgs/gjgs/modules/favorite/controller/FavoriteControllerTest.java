package com.gjgs.gjgs.modules.favorite.controller;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.dummy.BulletinDtoDummy;
import com.gjgs.gjgs.modules.dummy.LectureDtoDummy;
import com.gjgs.gjgs.modules.favorite.dto.FavoriteBulletinDto;
import com.gjgs.gjgs.modules.favorite.dto.LectureMemberDto;
import com.gjgs.gjgs.modules.favorite.dto.LectureTeamDto;
import com.gjgs.gjgs.modules.favorite.dto.MyTeamAndIsIncludeFavoriteLectureDto;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
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

class FavoriteControllerTest extends RestDocsTestSupport {



    final String TOKEN = "Bearer AccessToken";
    final String URL = "/api/v1/favorites";

    @BeforeEach
    void setUpMockUser() {
        securityUserMockSetting();
    }

    @DisplayName("클래스 찜하기 클릭시 조회창에 필요한 정보")
    @Test
    void get_my_favorite_lecture_and_team() throws Exception {
        // given
        Team team = Team.builder()
                .id(1L)
                .teamName("test")
                .build();

        MyTeamAndIsIncludeFavoriteLectureDto dto
                = MyTeamAndIsIncludeFavoriteLectureDto.of(team,true);

        when(favoriteService.getMyTeamAndIsIncludeFavoriteLecture(any())).thenReturn(Arrays.asList(dto));


        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL + "/my-teams/info/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("myTeamAndIsIncludeFavoriteLectureDtoList[0].teamId").value(team.getId().intValue()))
                .andExpect(jsonPath("myTeamAndIsIncludeFavoriteLectureDtoList[0].teamName").value(team.getTeamName()))
                .andExpect(jsonPath("myTeamAndIsIncludeFavoriteLectureDtoList[0].include").value(dto.isInclude()))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("찜 할 클래스 ID")
                        ),
                        responseFields(
                                fieldWithPath("myTeamAndIsIncludeFavoriteLectureDtoList[].teamId").type(NUMBER).description("조회한 팀 ID"),
                                fieldWithPath("myTeamAndIsIncludeFavoriteLectureDtoList[].teamName").type(STRING).description("조회한 팀 명"),
                                fieldWithPath("myTeamAndIsIncludeFavoriteLectureDtoList[].include").type(BOOLEAN).description("해당 팀의 클래스 찜 여부")
                        )
                ))
        ;
    }

    @DisplayName("개인 클래스 찜")
    @Test
    void add_my_favorite_lecture() throws Exception {

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL + "/lectures/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("개인 찜 할 클래스 ID")
                        )
                ))
        ;
    }

    @DisplayName("개인 찜 클래스 목록 조회")
    @Test
    void get_my_favorite_lectures() throws Exception {
        // given
        LectureMemberDto lectureMemberDto = LectureDtoDummy.createLectureMemberDto();
        when(favoriteService.getMyFavoriteLectures()).thenReturn(Arrays.asList(lectureMemberDto));

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL + "/lectures")
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("lectureMemberDtoList[0].lectureMemberId").value(lectureMemberDto.getLectureMemberId()))
                .andExpect(jsonPath("lectureMemberDtoList[0].lectureId").value(lectureMemberDto.getLectureId()))
                .andExpect(jsonPath("lectureMemberDtoList[0].thumbnailImageFileUrl").value(lectureMemberDto.getThumbnailImageFileUrl()))
                .andExpect(jsonPath("lectureMemberDtoList[0].zoneId").value(lectureMemberDto.getZoneId()))
                .andExpect(jsonPath("lectureMemberDtoList[0].price.priceOne").value(lectureMemberDto.getPrice().getPriceOne()))
                .andExpect(jsonPath("lectureMemberDtoList[0].price.priceTwo").value(lectureMemberDto.getPrice().getPriceTwo()))
                .andExpect(jsonPath("lectureMemberDtoList[0].price.priceThree").value(lectureMemberDto.getPrice().getPriceThree()))
                .andExpect(jsonPath("lectureMemberDtoList[0].price.priceFour").value(lectureMemberDto.getPrice().getPriceFour()))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        responseFields(
                                fieldWithPath("lectureMemberDtoList[].lectureMemberId").type(NUMBER).description("찜한 클래스의 ID"),
                                fieldWithPath("lectureMemberDtoList[].lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("lectureMemberDtoList[].thumbnailImageFileUrl").type(STRING).description("썸네일"),
                                fieldWithPath("lectureMemberDtoList[].zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("lectureMemberDtoList[].title").type(STRING).description("클래스 제목"),
                                fieldWithPath("lectureMemberDtoList[].price.regularPrice").type(NUMBER).description("클래스 신청 표준 가격"),
                                fieldWithPath("lectureMemberDtoList[].price.priceOne").type(NUMBER).description("한명 신청 시 가격"),
                                fieldWithPath("lectureMemberDtoList[].price.priceTwo").type(NUMBER).description("두명 신청 시 가격"),
                                fieldWithPath("lectureMemberDtoList[].price.priceThree").type(NUMBER).description("세명 신청 시 가격"),
                                fieldWithPath("lectureMemberDtoList[].price.priceFour").type(NUMBER).description("네명 신청 시 가격"))
                        )
                )
        ;
    }



    @DisplayName("개인 찜 삭제")
    @Test
    void delete_my_favorite_lecture() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(URL + "/lectures/{lectureId}", 1)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("찜 목록에서 삭제할 클래스 ID")
                        )
                ))
        ;
    }

    @DisplayName("팀 클래스 찜하기")
    @Test
    void add_team_favorite_lecture() throws Exception {

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL + "/teams/{teamId}/{lectureId}", 1, 1)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("찜할 팀의 ID"),
                                parameterWithName("lectureId").description("찜할 클래스의 ID")
                        )
                ))
        ;
    }

    @DisplayName("팀 찜 조회하기")
    @Test
    void get_team_favorite_lectures() throws Exception {
        // given
        LectureTeamDto lectureTeamDto = LectureDtoDummy.createLectureTeamDto();
        when(favoriteService.getTeamFavoriteLectures(any())).thenReturn(Arrays.asList(lectureTeamDto));

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL + "/teams/{teamId}", 1)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("lectureTeamDtoList[0].lectureTeamId").value(lectureTeamDto.getLectureTeamId()))
                .andExpect(jsonPath("lectureTeamDtoList[0].lectureId").value(lectureTeamDto.getLectureId()))
                .andExpect(jsonPath("lectureTeamDtoList[0].thumbnailImageFileUrl").value(lectureTeamDto.getThumbnailImageFileUrl()))
                .andExpect(jsonPath("lectureTeamDtoList[0].zoneId").value(lectureTeamDto.getZoneId()))
                .andExpect(jsonPath("lectureTeamDtoList[0].price.priceOne").value(lectureTeamDto.getPrice().getPriceOne()))
                .andExpect(jsonPath("lectureTeamDtoList[0].price.priceTwo").value(lectureTeamDto.getPrice().getPriceTwo()))
                .andExpect(jsonPath("lectureTeamDtoList[0].price.priceThree").value(lectureTeamDto.getPrice().getPriceThree()))
                .andExpect(jsonPath("lectureTeamDtoList[0].price.priceFour").value(lectureTeamDto.getPrice().getPriceFour()))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("팀으로 찜한 클래스들 조회하기")
                        ),
                        responseFields(
                                fieldWithPath("lectureTeamDtoList[].lectureTeamId").type(NUMBER).description("찜한 클래스의 ID"),
                                fieldWithPath("lectureTeamDtoList[].lectureId").type(NUMBER).description("클래스 ID"),
                                fieldWithPath("lectureTeamDtoList[].thumbnailImageFileUrl").type(STRING).description("썸네일"),
                                fieldWithPath("lectureTeamDtoList[].zoneId").type(NUMBER).description("지역 ID"),
                                fieldWithPath("lectureTeamDtoList[].title").type(STRING).description("클래스 제목"),
                                fieldWithPath("lectureTeamDtoList[].price.regularPrice").type(NUMBER).description("클래스 신청 표준 가격"),
                                fieldWithPath("lectureTeamDtoList[].price.priceOne").type(NUMBER).description("한명 신청 시 가격"),
                                fieldWithPath("lectureTeamDtoList[].price.priceTwo").type(NUMBER).description("두명 신청 시 가격"),
                                fieldWithPath("lectureTeamDtoList[].price.priceThree").type(NUMBER).description("세명 신청 시 가격"),
                                fieldWithPath("lectureTeamDtoList[].price.priceFour").type(NUMBER).description("네명 신청 시 가격"))
                        )
                );
        ;
    }



    @DisplayName("팀 찜 삭제")
    @Test
    void delete_team_favorite_lecture() throws Exception {

        mockMvc.perform(RestDocumentationRequestBuilders.delete(URL + "/teams/{teamId}/{lectureId}", 1, 1)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("teamId").description("찜 목록에서 삭제할 팀의 ID"),
                                parameterWithName("lectureId").description("찜 목록에서 삭제할 클래스의 ID")
                        )
                ));
    }

    @DisplayName("개인 게시글 찜하기")
    @Test
    void add_my_favorite_bulletin() throws Exception {

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.post(URL + "/bulletins/{bulletinId}", 1)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("bulletinId").description("찜할 게시글의 ID")
                        )
                ));
        ;
    }

    @DisplayName("내가 찜한 게시글 조회")
    @Test
    void get_my_favorite_bulletins() throws Exception {
        // given
        FavoriteBulletinDto favoriteBulletinDto = BulletinDtoDummy.createFavoriteBulletinDto();

        when(favoriteService.getMyFavoriteBulletins()).thenReturn(Arrays.asList(favoriteBulletinDto));

        // when then
        mockMvc.perform(RestDocumentationRequestBuilders.get(URL + "/bulletins")
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("favoriteBulletinDtoList[0].bulletinId").value(favoriteBulletinDto.getBulletinId()))
                .andExpect(jsonPath("favoriteBulletinDtoList[0].bulletinMemberId").value(favoriteBulletinDto.getBulletinId()))
                .andExpect(jsonPath("favoriteBulletinDtoList[0].thumbnailImageFileUrl").value(favoriteBulletinDto.getThumbnailImageFileUrl()))
                .andExpect(jsonPath("favoriteBulletinDtoList[0].zoneId").value(favoriteBulletinDto.getZoneId()))
                .andExpect(jsonPath("favoriteBulletinDtoList[0].title").value(favoriteBulletinDto.getTitle()))
                .andExpect(jsonPath("favoriteBulletinDtoList[0].timeType").value(favoriteBulletinDto.getTimeType()))
                .andExpect(jsonPath("favoriteBulletinDtoList[0].currentPeople").value(favoriteBulletinDto.getCurrentPeople()))
                .andExpect(jsonPath("favoriteBulletinDtoList[0].maxPeople").value(favoriteBulletinDto.getMaxPeople()))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        responseFields(
                                fieldWithPath("favoriteBulletinDtoList[].bulletinId").type(NUMBER).description("게시글의 ID"),
                                fieldWithPath("favoriteBulletinDtoList[].bulletinMemberId").type(NUMBER).description("멤버가 찜한 게시글의 ID"),
                                fieldWithPath("favoriteBulletinDtoList[].thumbnailImageFileUrl").type(STRING).description("썸네일"),
                                fieldWithPath("favoriteBulletinDtoList[].zoneId").type(NUMBER).description("지역"),
                                fieldWithPath("favoriteBulletinDtoList[].title").type(STRING).description("모집글 제목"),
                                fieldWithPath("favoriteBulletinDtoList[].timeType").type(STRING).description("모집글의 선호하는 모임 시간대"),
                                fieldWithPath("favoriteBulletinDtoList[].currentPeople").type(NUMBER).description("현재 모인 인원"),
                                fieldWithPath("favoriteBulletinDtoList[].maxPeople").type(NUMBER).description("팀의 최대 인원"),
                                fieldWithPath("favoriteBulletinDtoList[].age").type(STRING).description("모집하는 나이대")
                        )
                ))
        ;
    }


    @DisplayName("내가 찜한 게시글 삭제")
    @Test
    void delete_my_favorite_bulletin() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(URL + "/bulletins/{bulletinId}", 1)
                .header(HttpHeaders.AUTHORIZATION, TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("bulletinId").description("삭제할 찜한 게시글 ID")
                        )
                ))
        ;
    }
}
