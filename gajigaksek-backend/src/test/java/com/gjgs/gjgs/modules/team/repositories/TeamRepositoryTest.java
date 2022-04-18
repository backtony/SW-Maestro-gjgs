package com.gjgs.gjgs.modules.team.repositories;


import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberTeamRepository;
import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamRepositoryTest extends SetUpLectureTeamBulletinRepository {

    @Autowired MemberTeamRepository memberTeamRepository;

    @DisplayName("팀 id로 팀을 가져오면서 members와 leader 같이 가져오기")
    @Test
    void find_with_members_and_leader_by_id() throws Exception {
        //given
        // 팀 멤버 리버
        Member member = anotherMembers.get(0);
        memberTeamRepository.save(MemberTeam.of(member, team));
        flushAndClear();

        //when
        Team team1 = teamRepository.findWithMembersAndLeaderById(team.getId()).get();

        //then
        assertAll(
                () -> assertEquals(member.getUsername(), team1.getTeamMembers().get(0).getMember().getUsername()),
                () -> assertEquals(leader.getUsername(), team1.getLeader().getUsername())
        );
    }
}
