package com.gjgs.gjgs.dummy;

import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.bulletin.enums.Age;
import com.gjgs.gjgs.modules.bulletin.repositories.BulletinRepository;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.team.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class BulletinDummy {

    private final String[] days = {"MON|TUE", "WED|THU|FRI", "SAT|SUN", "MON|TUE|WED|THU|FRI"};
    private final String[] times = {"MORNING", "NOON", "AFTERNOON"};
    private final BulletinRepository bulletinRepository;


    public List<Bulletin> createBoardBulletins(List<Team> boardTeams, List<Lecture> board) {
        List<Bulletin> boardBulletins = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            boardBulletins.add(
                    Bulletin.builder()
                            .team(boardTeams.get(i)).lecture(board.get(i))
                            .title("같이 보드 탈 팀원들 모집합니다!")
                            .description("25 ~ 30세 분들이었으면 좋겠습니다.\n현재 마지막 한분을 기다리고 있어요!\n")
                            .status(true)
                            .age(Age.TWENTYFIVE_TO_THIRTY)
                            .dayType(days[i % days.length])
                            .timeType(times[i % times.length])
                            .build()
            );
        }
        List<Bulletin> savedBoardBulletins = bulletinRepository.saveAll(boardBulletins);
        return savedBoardBulletins;
    }

    public List<Bulletin> createClimbingBulletins(List<Team> climbingTeams, List<Lecture> climbing) {
        List<Bulletin> climbingBulletins = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            climbingBulletins.add(
                    Bulletin.builder()
                            .team(climbingTeams.get(i)).lecture(climbing.get(i))
                            .title("실내 클라이밍 같이 하실 분들을 모집합니다.")
                            .description("위만 보고 달립니다.\n저희 팀이 40대 분들이 대부분이라 모집할 한분도 40대 이상이셨으면 좋겠읍니다.\n")
                            .status(true)
                            .age(Age.FORTY)
                            .dayType(days[i % days.length])
                            .timeType(times[i % times.length])
                            .build()
            );
        }
        List<Bulletin> savedClimbingBulletins = bulletinRepository.saveAll(climbingBulletins);
        return savedClimbingBulletins;
    }

    public Bulletin createCookingBulletin(Team cookingTeam, Lecture cooking) {
        Bulletin bulletin = Bulletin.builder()
                .team(cookingTeam).lecture(cooking)
                .title("한식 같이 만들어볼 분들 찾습니다!!")
                .description("같이 비빔밥 만들어 먹어보고 싶어요!\n")
                .status(true)
                .age(Age.FORTY)
                .dayType(days[1])
                .timeType(times[2])
                .build();
        Bulletin savedBulletin = bulletinRepository.save(bulletin);
        return savedBulletin;
    }
}
