package com.gjgs.gjgs.modules.team.repositories.implement;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.dtos.TeamAppliersResponse;
import com.gjgs.gjgs.modules.team.entity.TeamApplier;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamApplierQueryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamApplierRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TeamApplierQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired TeamApplierQueryRepository teamApplierQueryRepository;
    @Autowired TeamApplierRepository teamApplierRepository;

    @Test
    @DisplayName("팀의 모든 신청자 및 리더 가져오기")
    void find_by_team_id_appliers_test() throws Exception {

        // given
        long count = memberRepository.count();
        Member applier1 = memberRepository.save(MemberDummy.createDataJpaTestMember((int) (count + 1), zone, category));
        Member applier2 = memberRepository.save(MemberDummy.createDataJpaTestMember((int) (count + 2), zone, category));
        addApplier(applier1);
        addApplier(applier2);
        flushAndClear();

        // when
        List<TeamApplier> teamApplierList = teamApplierQueryRepository.findWithTeamLeaderByTeamId(team.getId());

        // then
        assertAll(
                () -> assertEquals(teamApplierList.size(), 2),
                () -> assertEquals(teamApplierList.get(0).getAppliedTeam().getId(), team.getId())
        );
    }

    @Test
    @DisplayName("지원자 거절하기")
    void delete_applier_list_test() throws Exception {

        // given
        int memberCount = (int) memberRepository.count();
        Member applier1 = memberRepository.save(MemberDummy.createDataJpaTestMember(memberCount + 1, zone, category));
        teamApplierRepository.save(TeamApplier.of(applier1, team));
        flushAndClear();

        // when
        long count = teamApplierQueryRepository.deleteApplier(team.getId(), applier1.getId());

        // then
        assertEquals(count, 1);
    }

    @Test
    @DisplayName("팀 신청자 조회하기")
    void find_appliers_test() throws Exception {

        // given
        setUpTwoAppliers();

        // when
        TeamAppliersResponse response = teamApplierQueryRepository.findAppliers(team.getId());

        // then
        assertEquals(response.getApplierList().size(), 2);
    }

    @Test
    @DisplayName("팀 신청자들의 memberId 리스트 반환하기")
    void findApplierMemberIdListTest() throws Exception {

        // given
        setUpTwoAppliers();

        // when
        List<Long> memberIdList = teamApplierQueryRepository.findApplierMemberIdList(team.getId());

        // then
        assertEquals(memberIdList.size(), 2);
    }

    private void setUpTwoAppliers() {
        int count = (int) memberRepository.count();
        Member applier1 = memberRepository.save(MemberDummy.createDataJpaTestMember(count + 1, zone, category));
        Member applier2 = memberRepository.save(MemberDummy.createDataJpaTestMember(count + 2, zone, category));
        addApplier(applier1);
        addApplier(applier2);
    }
}
