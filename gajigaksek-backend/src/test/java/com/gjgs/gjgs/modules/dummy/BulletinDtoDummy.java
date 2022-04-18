package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.bulletin.dto.BulletinDetailResponse;
import com.gjgs.gjgs.modules.bulletin.dto.CreateBulletinRequest;
import com.gjgs.gjgs.modules.bulletin.dto.search.BulletinSearchResponse;
import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.bulletin.enums.Age;
import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.favorite.dto.FavoriteBulletinDto;
import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Sex;
import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.zone.entity.Zone;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BulletinDtoDummy {

    public static CreateBulletinRequest buildCreateBulletinDto() {
        return CreateBulletinRequest.builder()
                .teamId(1L)
                .title("test")
                .age("TWENTY_TO_TWENTYFIVE")
                .timeType("MORNING")
                .dayType("MON")
                .text("test")
                .lectureId(2L)
                .build();
    }

    public static List<BulletinSearchResponse> createBulletinSearchResponseList() {
        List<BulletinSearchResponse> resList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            resList.add(BulletinSearchResponse.builder()
                    .bulletinId((long) (i + 1))
                    .lectureImageUrl("test" + i)
                    .zoneId((long) (i + 1))
                    .categoryId((long) (i + 1))
                    .bulletinTitle("test" + i)
                    .age("20~25세")
                    .time("MORNING")
                    .nowMembers(2)
                    .maxMembers(4)
                    .build());
        }
        return resList;
    }

    public static List<BulletinSearchResponse> createBulletinSearchResponseListForOne() {
        List<BulletinSearchResponse> resList = new ArrayList<>();
        resList.add(BulletinSearchResponse.builder()
                .bulletinId((long) (1))
                .lectureImageUrl("test")
                .zoneId((long) (1))
                .categoryId((long) (1))
                .bulletinTitle("test")
                .age("20~25세")
                .time("MORNING")
                .nowMembers(2)
                .maxMembers(4)
                .build());
        return resList;
    }

    public static BulletinDetailResponse createBulletinDetailResponse() {
        Member leader = Member.builder()
                .id(1L).imageFileUrl("testUrl1").nickname("testNick1")
                .sex(Sex.M).age(21).profileText("testText1").username("leaderUsername")
                .build();
        Member teamMember1 = Member.builder()
                .id(2L).imageFileUrl("testUrl2").nickname("testNick2")
                .sex(Sex.M).age(22).profileText("testText2")
                .build();
        Member teamMember2 = Member.builder()
                .id(3L).imageFileUrl("testUrl3").nickname("testNick3")
                .sex(Sex.M).age(23).profileText("testText3")
                .build();

        Lecture lecture = Lecture.builder()
                .id(2L)
                .zone(Zone.builder()
                        .id(3L)
                        .build())
                .category(Category.builder()
                        .id(4L)
                        .build())
                .thumbnailImageFileUrl("testImageUrl")
                .title("testLectureTitle")
                .price(Price.builder()
                        .priceOne(100).priceTwo(200).priceThree(300).priceFour(400)
                        .build())
                .build();
        Team team = Team.builder()
                .leader(leader)
                .id(10L).currentMemberCount(3).maxPeople(4)
                .teamMembers(List.of(
                        MemberTeam.builder().member(teamMember1).build(),
                        MemberTeam.builder().member(teamMember2).build())).build();

        Bulletin bulletin = Bulletin.builder()
                .id(1L).title("testTitle").dayType("testDay")
                .timeType("testTime").description("testText")
                .age(Age.TWENTYFIVE_TO_THIRTY)
                .team(team)
                .lecture(lecture)
                .build();

        return BulletinDetailResponse.builder()
                .bulletinId(bulletin.getId())
                .bulletinTitle(bulletin.getTitle())
                .day(bulletin.getDayType())
                .time(bulletin.getTimeType())
                .age(bulletin.getAge().name())
                .bulletinText(bulletin.getDescription())
                .bulletinsTeam(
                        BulletinDetailResponse.BulletinsTeam.builder()
                                .teamId(team.getId())
                                .iAmLeader(false)
                                .currentPeople(team.getCurrentMemberCount())
                                .maxPeople(team.getMaxPeople())
                                .leader(BulletinDetailResponse.TeamMemberResponse.builder()
                                        .memberId(leader.getId())
                                        .imageUrl(leader.getImageFileUrl())
                                        .nickname(leader.getNickname())
                                        .sex(leader.getSex().name())
                                        .age(leader.getAge())
                                        .text(leader.getProfileText())
                                        .build())
                                .members(team.getTeamMembers().stream().map(teamMember ->
                                    BulletinDetailResponse.TeamMemberResponse.builder()
                                            .memberId(teamMember.getMember().getId())
                                            .imageUrl(teamMember.getMember().getImageFileUrl())
                                            .nickname(teamMember.getMember().getNickname())
                                            .sex(teamMember.getMember().getSex().name())
                                            .age(teamMember.getMember().getAge())
                                            .text(teamMember.getMember().getProfileText())
                                            .build()).collect(toList()))
                                .build()
                )
                .bulletinsLecture(
                        BulletinDetailResponse.BulletinsLecture.builder()
                                .lectureId(lecture.getId())
                                .myFavoriteLecture(false)
                                .lecturesZoneId(lecture.getZone().getId())
                                .lecturesCategoryId(lecture.getCategory().getId())
                                .lecturesThumbnailUrl(lecture.getThumbnailImageFileUrl())
                                .lectureName(lecture.getTitle())
                                .priceOne(lecture.getPrice().getPriceOne())
                                .priceTwo(lecture.getPrice().getPriceTwo())
                                .priceThree(lecture.getPrice().getPriceThree())
                                .priceFour(lecture.getPrice().getPriceFour())
                                .build()
                )
                .build();
    }

    public static FavoriteBulletinDto createFavoriteBulletinDto() {
        return FavoriteBulletinDto.builder()
                .bulletinId(1L)
                .bulletinMemberId(1L)
                .thumbnailImageFileUrl("test")
                .zoneId(1L)
                .title("test")
                .age(Age.THIRTY_TO_THIRTYFIVE)
                .timeType("AFTERNOON")
                .currentPeople(3)
                .maxPeople(5)
                .build();
    }


}
