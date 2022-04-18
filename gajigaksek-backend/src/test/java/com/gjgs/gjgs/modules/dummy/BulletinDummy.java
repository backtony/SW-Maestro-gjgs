package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.bulletin.enums.Age;
import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.dto.mypage.MyBulletinDto;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Sex;
import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.zone.entity.Zone;

import java.util.ArrayList;
import java.util.List;

public class BulletinDummy {

    public static Bulletin createBulletin(Team team, Lecture lecture) {
        return Bulletin.builder()
                .team(team)
                .lecture(lecture)
                .title("title")
                .description("description")
                .status(true)
                .age(Age.THIRTY_TO_THIRTYFIVE)
                .dayType("MON|TUE")
                .timeType("AFTERNOON|NOON")
                .build();
    }

    public static Bulletin createBulletin(Lecture lecture) {
        return Bulletin.builder()
                .lecture(lecture)
                .title("title")
                .description("description")
                .status(true)
                .age(Age.THIRTY_TO_THIRTYFIVE)
                .dayType("MON|TUE")
                .timeType("AFTERNOON|NOON")
                .build();
    }

    public static Bulletin createBulletin(Team team, Lecture lecture, String title, Age age) {
        return Bulletin.builder()
                .team(team)
                .lecture(lecture)
                .title(title)
                .description("description")
                .status(true)
                .age(age)
                .dayType("MON|TUE")
                .timeType("AFTERNOON|NOON")
                .build();

    }


    public static MyBulletinDto createBulletinDto() {
        return MyBulletinDto.builder()
                .bulletinId(1L)
                .thumbnailImageFileUrl("Test")
                .zoneId(1L)
                .title("test")
                .age(Age.THIRTY_TO_THIRTYFIVE)
                .timeType("AFTERNOON|NOON")
                .currentPeople(2)
                .maxPeople(4)
                .build();
    }

    public static List<Bulletin> createBulletinList(List<Lecture> lectureList, List<Team> teamList) {
        List<Bulletin> bulletinList = new ArrayList<>();
        for (int i = 0; i < teamList.size(); i++) {
            Bulletin bulletin = Bulletin.builder()
                    .lecture(lectureList.get(i % lectureList.size()))
                    .team(teamList.get(i))
                    .status(true)
                    .title("테스트 " + i)
                    .description("테스트" + i)
                    .age(Age.THIRTY_TO_THIRTYFIVE)
                    .dayType("TUE")
                    .timeType("MORNING")
                    .build();
            bulletin.setTeam(teamList.get(i));
            bulletinList.add(bulletin);
        }

        return bulletinList;
    }

    public static List<Bulletin> createBulletinList(Lecture pickedLecture, List<Team> teamList) {
        List<Bulletin> bulletinList = new ArrayList<>();
        int i = 1;
        for (Team team : teamList) {
            bulletinList.add(
                    Bulletin.builder()
                            .team(team)
                            .lecture(pickedLecture)
                            .title("테스트" + i++)
                            .description("테스트" + i++)
                            .status(true)
                            .age(Age.THIRTY_TO_THIRTYFIVE)
                            .dayType("MON")
                            .timeType("MORNING")
                            .build()
            );
        }

        return bulletinList;
    }

    public static Bulletin createBulletin(String title) {
        return Bulletin.builder()
                .title(title)
                .description("description")
                .status(true)
                .age(Age.THIRTY_TO_THIRTYFIVE)
                .dayType("MON|TUE")
                .timeType("AFTERNOON|NOON")
                .build();
    }

    public static Bulletin createBulletinWithId(Long id, String title) {
        return Bulletin.builder()
                .id(id)
                .title(title)
                .description("description")
                .status(true)
                .age(Age.THIRTY_TO_THIRTYFIVE)
                .dayType("MON|TUE")
                .timeType("AFTERNOON|NOON")
                .build();
    }

    public static Bulletin createBulletin() {
        Member leader = Member.builder()
                .id(1L).imageFileUrl("testUrl1").nickname("testNick1")
                .sex(Sex.M).age(21).profileText("testText1")
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
                .price(Price.builder()
                        .priceOne(100).priceTwo(200).priceThree(300).priceFour(400)
                        .build())
                .thumbnailImageFileUrl("testImageUrl")
                .title("testLectureTitle")
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

        return bulletin;
    }
}
