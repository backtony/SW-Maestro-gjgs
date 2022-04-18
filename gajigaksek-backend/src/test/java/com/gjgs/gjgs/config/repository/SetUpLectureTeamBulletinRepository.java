package com.gjgs.gjgs.config.repository;

import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.bulletin.repositories.BulletinRepository;
import com.gjgs.gjgs.modules.dummy.BulletinDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.entity.TeamApplier;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamApplierRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Getter
public class SetUpLectureTeamBulletinRepository extends SetUpMemberRepository{

    @Autowired protected LectureRepository lectureRepository;
    @Autowired protected TeamRepository teamRepository;
    @Autowired protected BulletinRepository bulletinRepository;
    @Autowired protected TeamApplierRepository teamApplierRepository;

    protected Lecture lecture;
    protected Team team;
    protected Bulletin bulletin;

    @BeforeEach
    void setUpLectureTeamBulletin() {
        lecture = lectureRepository.save(LectureDummy.createDataJpaTestLecture(zone, category, director));
        team = teamRepository.save(TeamDummy.createTeam(leader, zone));
        bulletin = bulletinRepository.save(BulletinDummy.createBulletin(team, lecture));
    }

    @AfterEach
    void tearDownLectureTeamBulletin() {
        bulletinRepository.deleteAll();
        teamApplierRepository.deleteAll();
        teamRepository.deleteAll();
        lectureRepository.deleteAll();
    }

    protected void addApplier(Member applier) {
        teamApplierRepository.save(TeamApplier.of(applier, team));
    }
}
