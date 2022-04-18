package com.gjgs.gjgs.dummy;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamJdbcRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class TeamDummy {

    private final String[] days = {"MON|TUE", "WED|THU|FRI", "SAT|SUN", "MON|TUE|WED|THU|FRI"};
    private final String[] times = {"MORNING", "NOON", "AFTERNOON"};
    private final TeamRepository teamRepository;
    private final TeamJdbcRepository teamJdbcRepository;

    public List<Team> create25teams(Member leader, Member member1, Member member2, List<Zone> zones, Category category) {
        Zone seoul = zones.get(0);
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            Team team = Team.builder()
                    .zone(seoul)
                    .leader(leader)
                    .teamName(i + "번째팀")
                    .maxPeople(4)
                    .currentMemberCount(3)
                    .dayType(days[i % days.length])
                    .timeType(times[i % times.length])
                    .build();
            teams.add(team);
        }

        List<Team> saved25Teams = teamRepository.saveAll(teams);
        saved25Teams.forEach(team -> {
            teamJdbcRepository.insertMemberTeamList(team.getId(), List.of(member1.getId(), member2.getId()));
            teamJdbcRepository.insertTeamCategoryList(team.getId(), List.of(category.getId()));
        });
        return saved25Teams;

    }
}
