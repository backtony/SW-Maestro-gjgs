package com.gjgs.gjgs.modules.member.repository.interfaces;

import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.MemberTeam;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamJdbcRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberTeamRepositoryTest extends SetUpMemberRepository {

    @Autowired MemberTeamRepository memberTeamRepository;
    @Autowired TeamJdbcRepository teamJdbcRepository;
    @Autowired TeamRepository teamRepository;

    @AfterEach
    void tearDown() throws Exception {
        memberTeamRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    @DisplayName("teamId로 팀의 멤버들 가져오기")
    void find_by_teamId() throws Exception {

        // given
        Member teamMember2 = anotherMembers.get(0);
        Member teamMember3 = anotherMembers.get(1);
        Member teamMember4 = anotherMembers.get(2);
        Team team = teamRepository.save(TeamDummy.createTeam(leader, zone));
        teamJdbcRepository.insertMemberTeamList(team.getId(), List.of(
                teamMember2.getId(), teamMember3.getId(), teamMember4.getId()
        ));
        flushAndClear();

        // when
        List<MemberTeam> memberTeamList = memberTeamRepository.findByTeamId(team.getId());

        // then
        List<Long> memberTeamIdList = memberTeamList.stream()
                .map(memberTeam -> memberTeam.getMember().getId()).sorted()
                .collect(toList());
        assertAll(
                () -> assertEquals(memberTeamList.size(), 3),
                () -> assertThat(memberTeamIdList).containsExactly(teamMember2.getId(), teamMember3.getId(), teamMember4.getId())
        );
    }
}
