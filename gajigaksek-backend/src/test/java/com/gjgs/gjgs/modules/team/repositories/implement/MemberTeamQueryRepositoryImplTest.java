package com.gjgs.gjgs.modules.team.repositories.implement;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberTeamRepository;
import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import com.gjgs.gjgs.modules.team.repositories.interfaces.MemberTeamQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberTeamQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired MemberTeamQueryRepository memberTeamQueryRepository;
    @Autowired MemberTeamRepository memberTeamRepository;

    @Test
    @DisplayName("해당 지원자가 이미 팀에 속해있는지 테스트")
    void exist_by_applier_in_team_member_test() throws Exception {

        // given
        int count = (int) memberRepository.count();
        Member applier = memberRepository.save(MemberDummy.createDataJpaTestMember(count + 1, zone, category));
        memberTeamRepository.save(MemberTeam.of(applier, team));
        flushAndClear();

        // when, then
        assertTrue(memberTeamQueryRepository.existByApplierInTeamMember(team.getId(), applier.getId()));
    }
}
