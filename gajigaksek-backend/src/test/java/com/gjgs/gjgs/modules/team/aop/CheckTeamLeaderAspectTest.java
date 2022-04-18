package com.gjgs.gjgs.modules.team.aop;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.dtos.TeamAppliersResponse;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.team.entity.TeamApplier;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamApplierQueryRepository;
import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamQueryRepository;
import com.gjgs.gjgs.modules.team.services.manage.TeamManageService;
import com.gjgs.gjgs.modules.team.services.manage.TeamManageServiceImpl;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckTeamLeaderAspectTest {

    @Mock SecurityUtil securityUtil;
    @Mock TeamQueryRepository teamQueryRepository;
    @Mock TeamApplierQueryRepository teamApplierQueryRepository;
    @InjectMocks CheckLeaderAspect aspect;
    @InjectMocks TeamManageServiceImpl teamManageService;

    @Test
    @DisplayName("쿼리 이후 리더 체크 확인하기")
    void check_leader_test() {

        // given
        Member leader = Member.builder().id(1L).username("test").build();
        Team team = Team.builder().leader(leader).build();
        stubbingGetCurrentMember(leader);

        // when
        aspect.checkTeamLeader(team);

        // when, then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> assertDoesNotThrow(() -> aspect.checkTeamLeader(team))
        );
    }

    @Test
    @DisplayName("ApplierList를 가져올 때 리더인지 체크하기")
    void check_team_leader_for_applier_list_test() throws Exception {

        // given
        Member leader = Member.builder().id(1L).username("test").build();
        Team team = Team.builder().leader(leader).build();
        List<TeamApplier> applierList = List.of(
                TeamApplier.builder().id(1L).appliedTeam(team).build(),
                TeamApplier.builder().id(2L).appliedTeam(team).build());
        stubbingGetCurrentMember(leader);

        // when
        aspect.checkTeamLeaderForApplierList(applierList);

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> assertDoesNotThrow(() -> aspect.checkTeamLeaderForApplierList(applierList))
        );
    }

    @Test
    @DisplayName("teamId와 username으로 팀장인지 조회하기")
    void check_team_leader_before_test() throws Exception {

        // given
        TeamManageService proxy = getAopProxy();
        Member leader = Member.builder().id(1L).username("test").build();
        Team team = Team.builder().leader(leader).build();
        stubbingGetCurrentMember(leader);
        stubbingExistByIdLeaderUsername();
        stubbingFindAppliers();

        // when
        proxy.getTeamAppliers(team.getId());

        // then
        assertAll(
                () -> verify(teamQueryRepository).existByIdLeaderUsername(team.getId(), leader.getUsername()),
                () -> verify(teamApplierQueryRepository).findAppliers(team.getId())
        );
    }

    private void stubbingFindAppliers() {
        when(teamApplierQueryRepository.findAppliers(any()))
                .thenReturn(TeamAppliersResponse.builder().build());
    }

    private void stubbingExistByIdLeaderUsername() {
        when(teamQueryRepository.existByIdLeaderUsername(any(), any()))
                .thenReturn(true);
    }

    private void stubbingGetCurrentMember(Member director) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(director.getUsername()));
    }

    private TeamManageService getAopProxy() {
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(teamManageService);
        aspectJProxyFactory.addAspect(aspect);
        TeamManageService proxy = aspectJProxyFactory.getProxy();
        return proxy;
    }
}