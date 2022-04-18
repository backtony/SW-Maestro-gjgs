package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.zone.entity.Zone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamDummy {

    public static Team createUniqueTeam() {
        return createTeam();
    }

    public static Team createUniqueTeam(int i, Member member, Zone zone) {
        return createTeam(i, member, zone);
    }

    private static Team createTeam(int i, Member member, Zone zone) {
        return Team.builder()
                .zone(zone)
                .teamName("팀" + String.valueOf(i))
                .leader(member)
                .maxPeople(4)
                .currentMemberCount(2)
                .timeType("AFTERNOON|NOON")
                .dayType("MON|TUE")
                .build();
    }

    public static Team createUniqueTeam(int i, Member leader) {
        return createTeam(i, leader);
    }

    private static Team createTeam(int i, Member leader) {
        return Team.builder()
                .leader(leader)
                .teamName("팀" + String.valueOf(i))
                .maxPeople(4)
                .currentMemberCount(2)
                .timeType("AFTERNOON|NOON")
                .dayType("MON|TUE")
                .build();
    }

    private static Team createTeam() {
        return Team.builder()
                .teamName("팀1")
                .maxPeople(4)
                .currentMemberCount(2)
                .timeType("AFTERNOON|NOON")
                .dayType("MON|TUE")
                .teamMembers(new ArrayList<>())
                .build();
    }

    public static Team createTeam(Member leader, Zone zone) {
        return Team.builder()
                .zone(zone)
                .leader(leader)
                .teamName("팀1")
                .maxPeople(4)
                .currentMemberCount(1)
                .timeType("AFTERNOON|NOON")
                .dayType("MON|TUE")
                .teamMembers(new ArrayList<>())
                .build();
    }

    public static Team createTeamByIdAndTeamName(Long id, String teamName) {
        return Team.builder()
                .id(id)
                .teamName(teamName)
                .build();
    }


    public static Team createTeamWithZoneCategories(Member leader, Zone zone, List<Category> categoryList) {
        Team team = Team.builder()
                .zone(zone)
                .leader(leader)
                .teamName("팀1")
                .maxPeople(4)
                .currentMemberCount(1)
                .timeType("AFTERNOON|NOON")
                .dayType("MON|TUE")
                .build();
        return team;
    }



    public static List<Team> createTeamList() {
        List<Team> teamList = new ArrayList<>();
        for (int i = 1; i < 201; i++) {
            Team team = Team.builder()
                    .maxPeople(4).currentMemberCount(2).dayType("MON").timeType("MORNING")
                    .build();
            teamList.add(team);
        }
        return teamList;
    }

    public static Team createTeamOfManyMembers(Zone zone, Member leader, Member... members) {
        Team team = Team.builder()
                .zone(zone)
                .teamName("testteam")
                .leader(leader)
                .maxPeople(4)
                .currentMemberCount(1)
                .timeType("AFTERNOON|NOON")
                .dayType("MON|TUE")
                .build();
        Arrays.stream(members).forEach(team::addTeamMember);
        return team;
    }





    public static List<Team> createTeamListWithLeaders(List<Member> leaders) {
        List<Team> teamList = new ArrayList<>();

        int i = 1;
        for (Member leader : leaders) {
            teamList.add(
                    Team.builder()
                            .leader(leader)
                            .teamName("테스트팀" + i++)
                            .currentMemberCount(1).maxPeople(4)
                            .dayType("MON")
                            .timeType("MORNING")
                            .build());
        }
        return teamList;
    }



    public static Team createDataJpaTestTeam(Member leader, Member... members) {
        Team team =  Team.builder()
                .leader(leader)
                .teamName("testTeam").currentMemberCount(1).maxPeople(4)
                .timeType("AFTERNOON|NOON")
                .dayType("MON|TUE")
                .build();
        Arrays.stream(members).forEach(team::addTeamMember);
        return team;
    }
}
