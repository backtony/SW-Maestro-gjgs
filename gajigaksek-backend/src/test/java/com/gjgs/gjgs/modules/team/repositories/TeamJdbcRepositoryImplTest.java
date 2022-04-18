package com.gjgs.gjgs.modules.team.repositories;

import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamJdbcRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamQueryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamJdbcRepositoryImplTest extends SetUpMemberRepository {

    @Autowired TeamRepository teamRepository;
    @Autowired TeamJdbcRepository teamJdbcRepository;
    @Autowired TeamQueryRepository teamQueryRepository;

    @AfterEach
    void tearDown() throws Exception {
        teamRepository.deleteAll();
    }

    @Test
    @DisplayName("팀이 설정한 카테고리 insert")
    void insert_team_category_list_test() {

        // given
        Team team = teamRepository.save(TeamDummy.createTeam(leader, zone));
        flushAndClear();

        // when
        teamJdbcRepository.insertTeamCategoryList(team.getId(), List.of(category.getId()));
        flushAndClear();

        // then
        assertEquals(teamQueryRepository.deleteTeamCategories(team.getId()), 1);
    }

    @Test
    @DisplayName("팀에 속한 멤버 리스트 insert")
    void insert_member_team_list_test() throws Exception {

        // given
        Member member1 = anotherMembers.get(0);
        Member member2 = anotherMembers.get(1);
        Team team = teamRepository.save(TeamDummy.createTeam(leader, zone));
        flushAndClear();

        // when
        teamJdbcRepository.insertMemberTeamList(team.getId(), List.of(member1.getId(), member2.getId()));
        flushAndClear();

        // then
        Team findTeam = teamRepository.findById(team.getId()).orElseThrow();
        List<Long> findTeamMemberList = findTeam.getTeamMembers().stream()
                .map(teamMember -> teamMember.getMember().getId()).sorted()
                .collect(toList());
        assertAll(
                () -> assertEquals(findTeamMemberList.size(), 2),
                () -> assertThat(findTeamMemberList).containsExactly(member1.getId(), member2.getId())
        );
    }

    @DisplayName("매칭시 팀 만들 때 멤버 한번에 벌크 insert")
    @Test
    void insert_member_team_list() throws Exception {

        //given
        Member m1 = anotherMembers.get(0);
        Member m2 = anotherMembers.get(1);
        Team team = teamRepository.save(TeamDummy.createTeam(leader, zone));
        flushAndClear();

        //when
        teamJdbcRepository.insertMemberTeamList(team.getId(), Arrays.asList(m1.getId(),m2.getId()));
        flushAndClear();

        Team t1 = teamRepository.findById(team.getId()).get();

        //then
        assertEquals(2, t1.getTeamMembers().size());
    }
}
