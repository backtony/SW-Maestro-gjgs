package com.gjgs.gjgs.modules.utils.aop;

import com.gjgs.gjgs.modules.bulletin.dto.BulletinDetailResponse;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.dummy.BulletinDtoDummy;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.BulletinMemberQueryRepository;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.LectureMemberQueryRepository;
import com.gjgs.gjgs.modules.lecture.dtos.FavoriteResponse;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.team.dtos.TeamDetailResponse;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarkFavoriteAspectTest {

    @Mock SecurityUtil securityUtil;
    @Mock BulletinMemberQueryRepository bulletinMemberQueryRepository;
    @Mock LectureMemberQueryRepository lectureMemberQueryRepository;
    @Mock MemberRepository memberRepository;
    @InjectMocks MarkFavoriteAspect markFavoriteAspect;

    @Test
    @DisplayName("게시글 좋아요 표시, 로그인 한 경우 및 좋아요 표시한 게시글이 있을 경우")
    void get_bulletins_test() throws Exception {

        // given
        Member member = Member.builder().id(1L).username("username").build();
        stubbingSecurityUtil(member);
        Pageable pageable = PageRequest.of(0, 12);
        List<BulletinSearchResponse> mockResponse = BulletinDtoDummy.createBulletinSearchResponseList();
        stubbingGetFavoriteBulletin(mockResponse);
        Page<BulletinSearchResponse> res = new PageImpl<>(mockResponse, pageable, 100);

        // when
        markFavoriteAspect.markFavoriteBulletin(res);

        // then
        List<BulletinSearchResponse> favoriteBulletinList = res.getContent().stream().filter(BulletinSearchResponse::isMyFavorite)
                .collect(toList());
        assertAll(
                () -> verify(bulletinMemberQueryRepository).findFavoriteBulletinIdListByUsername(member.getUsername()),
                () -> assertEquals(favoriteBulletinList.size(), 3)
        );
    }

    @Test
    @DisplayName("게시글 상세 조회, 클래스 좋아요 표시와 내가 리더인 경우")
    void get_bulletin_details_iam_leader_test() throws Exception {

        // given
        Member member = Member.builder().id(1L).username("notLeader").build();
        stubbingSecurityUtil(member);
        stubbingFindMemberId(member);
        BulletinDetailResponse res = BulletinDtoDummy.createBulletinDetailResponse();
        stubbingGetFavoriteLecture(res.getBulletinsLecture().getLectureId());

        // when
        markFavoriteAspect.markFavoriteLecture(res);

        // then
        assertAll(
                () -> verify(memberRepository).findIdByUsername(member.getUsername()),
                () -> verify(lectureMemberQueryRepository).findFavoriteLectureIdListByMemberId(member.getId()),
                () -> assertTrue(res.getBulletinsLecture().isMyFavoriteLecture()),
                () -> assertTrue(res.getBulletinsTeam().isIAmLeader())
        );
    }

    @Test
    @DisplayName("현재 유저가 해당 팀의 리더이고, 찜한 클래스가 한개 있을 경우 찜표시")
    void set_leader_favorite_lecture_test() throws Exception {

        // given
        Member member = Member.builder().id(1L).username("username").build();
        stubbingSecurityUtil(member);
        stubbingFindMemberId(member);
        TeamDetailResponse response = createTeamDetailResponse();
        stubbingGetFavoriteLecture(response);

        // when
        markFavoriteAspect.markFavoriteLecture(response);

        // then
        Set<TeamDetailResponse.FavoriteLecture> favoriteLectureSet = response.getFavoriteLectureList()
                .stream()
                .filter(TeamDetailResponse.FavoriteLecture::isMyFavoriteLecture)
                .collect(toSet());
        assertAll(
                () -> assertTrue(response.isIAmLeader()),
                () -> assertEquals(favoriteLectureSet.size(), response.getFavoriteLectureList().size())
        );
    }

    @Test
    @DisplayName("lecture 페이징 조회 시 좋아요 표시하기")
    void mark_favorite_paging_test() throws Exception {

        // given
        Member member = Member.builder().id(1L).username("username").build();
        stubbingSecurityUtil(member);
        stubbingFindMemberId(member);
        Page<LectureSearchResponse> response = new PageImpl<>(createLectureResponse());
        stubbingGetFavoriteLecture(response.getContent().stream()
                .map(LectureSearchResponse::getLectureId).collect(toList()));

        // when
        markFavoriteAspect.markFavoriteLecture(response);

        // then
        List<LectureSearchResponse> content = response.getContent();
        List<LectureSearchResponse> favorite = content.stream().filter(FavoriteResponse::isMyFavorite).collect(toList());
        assertEquals(favorite.size(), content.size());
    }

    @Test
    @DisplayName("lecture 상세 조회 시 좋아요 표시하기")
    void mark_favorite_detail_test() throws Exception {

        // given
        Member member = Member.builder().id(1L)
                .username("username").build();
        stubbingSecurityUtil(member);
        stubbingFindMemberId(member);
        LectureSearchResponse response = createLectureResponse().get(0);
        stubbingGetFavoriteLecture(createLectureResponse().stream().map(LectureSearchResponse::getLectureId).collect(toList()));

        // when
        markFavoriteAspect.markFavoriteLecture(response);

        // then
        assertTrue(response.isMyFavorite());
    }

    private void stubbingGetFavoriteLecture(List<Long> lectureIdList) {
        when(lectureMemberQueryRepository.findFavoriteLectureIdListByMemberId(any())).thenReturn(lectureIdList);
    }

    private void stubbingGetFavoriteLecture(Long lectureId) {
        when(lectureMemberQueryRepository.findFavoriteLectureIdListByMemberId(any()))
                .thenReturn(List.of(lectureId));
    }

    private void stubbingFindMemberId(Member member) {
        when(memberRepository.findIdByUsername(member.getUsername()))
                .thenReturn(Optional.of(member.getId()));
    }

    private void stubbingGetFavoriteLecture(TeamDetailResponse response) {
        when(lectureMemberQueryRepository.findFavoriteLectureIdListByMemberId(any()))
                .thenReturn(response.getFavoriteLectureList().stream()
                        .map(TeamDetailResponse.FavoriteLecture::getLectureId)
                        .collect(toList()));
    }

    private void stubbingGetFavoriteBulletin(List<BulletinSearchResponse> contents) {
        when(bulletinMemberQueryRepository
                .findFavoriteBulletinIdListByUsername(any()))
                .thenReturn(contents.stream().map(BulletinSearchResponse::getBulletinId).collect(toList())
                        .subList(0, 3));
    }

    private List<LectureSearchResponse> createLectureResponse() {
        return List.of(
                LectureSearchResponse.builder().lectureId(1L).build(),
                LectureSearchResponse.builder().lectureId(2L).build(),
                LectureSearchResponse.builder().lectureId(3L).build()
        );
    }

    private void stubbingSecurityUtil(Member member) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(member.getUsername()));
    }

    private TeamDetailResponse createTeamDetailResponse() {
        return TeamDetailResponse.builder()
                .teamName("테스트1")
                .day("MON")
                .time("MORNING")
                .applyPeople(2)
                .maxPeople(3)
                .zoneId(1L)
                .iAmLeader(false)
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
                                .build(),
                        TeamDetailResponse.TeamMembers.builder()
                                .memberId(2L)
                                .imageURL("test")
                                .nickname("member2")
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
                                .build(),
                        TeamDetailResponse.FavoriteLecture.builder()
                                .lectureId(2L)
                                .lecturesZoneId(2L)
                                .lecturesTitle("lecture2")
                                .lecturesPrice(20000)
                                .lecturesImageURL("test")
                                .myFavoriteLecture(false)
                                .build(),
                        TeamDetailResponse.FavoriteLecture.builder()
                                .lectureId(3L)
                                .lecturesZoneId(3L)
                                .lecturesTitle("lecture3")
                                .lecturesPrice(20000)
                                .lecturesImageURL("test")
                                .myFavoriteLecture(false)
                                .build())).build();
    }
}