package com.gjgs.gjgs.modules.team.entity;

import com.gjgs.gjgs.modules.exception.team.NotMemberOfTeamException;
import com.gjgs.gjgs.modules.exception.team.TeamMemberCountLargerThanEditCountException;
import com.gjgs.gjgs.modules.exception.team.TeamMemberMaxException;
import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.dtos.CreateTeamRequest;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Team extends BaseEntity implements CheckTeamLeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ZONE_ID", nullable = false)
    private Zone zone;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member leader;

    @OneToMany(mappedBy = "team")
    @Builder.Default
    private List<MemberTeam> teamMembers = new ArrayList<>();

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private int maxPeople;

    @Column(nullable = false, columnDefinition = "int default 1")
    private int currentMemberCount;

    @Column(nullable = false)
    private String dayType;

    @Column(nullable = false)
    private String timeType;

    public static Team of(Long TeamId){
        return Team.builder()
                .id(TeamId)
                .build();
    }

    public static Team of(CreateTeamRequest request, Member leader) {
        return Team.builder()
                .leader(leader)
                .currentMemberCount(1)
                .dayType(request.getDayType())
                .timeType(request.getTimeType())
                .maxPeople(request.getMaxPeople() == 1 ?  4 : request.getMaxPeople())
                .teamName(request.getTeamName())
                .build();
    }

    public static Team createMatchTeam(MatchingRequest matchingRequest, Member member, List<Member> memberList) {

        StringBuilder teamNameBuilder = new StringBuilder(member.getNickname());

        for (Member mem : memberList) {
            teamNameBuilder.append(", ");
            teamNameBuilder.append(mem.getNickname());
        }

        Team team = Team.builder()
                .zone(Zone.of(matchingRequest.getZoneId()))
                .leader(member)
                .teamName(teamNameBuilder.toString())
                .maxPeople(matchingRequest.getPreferMemberCount())
                .currentMemberCount(1)
                .dayType(matchingRequest.getDayType())
                .timeType(matchingRequest.getTimeType())
                .build();

        team.currentMemberCount += memberList.size();
        return team;
    }

    public void delegateLeader(Member member) {
        this.leader = member;
    }

    public void putZone(Zone zone) {
        this.zone = zone;
    }

    public void modify(CreateTeamRequest modifyRequest) {
        checkLowerThanCurrentPeople(modifyRequest.getMaxPeople());
        this.teamName = modifyRequest.getTeamName();
        this.maxPeople = modifyRequest.getMaxPeople();
        this.timeType = modifyRequest.getTimeType();
        this.dayType = modifyRequest.getDayType();
    }

    private void checkLowerThanCurrentPeople(int maxPeople) {
        if (this.currentMemberCount > maxPeople) {
            throw new TeamMemberCountLargerThanEditCountException();
        }
    }

    public void checkTeamIsFullDoThrow() {
        if (isFull()) {
            throw new TeamMemberMaxException();
        }
    }

    public MemberTeam addTeamMember(Member member) {
        MemberTeam memberTeam = MemberTeam.of(member, this);
        this.getTeamMembers().add(memberTeam);
        addTeamMemberCount();
        return memberTeam;
    }

    public void removeTeamMember(Member exitMember) {
        isLeaderDoThrow(this.getLeader(), exitMember);
        checkNotTeamMember(exitMember.getId());
        removeMemberTeam(exitMember);
        --currentMemberCount;
    }

    private void checkNotTeamMember(Long checkMemberId) {
        List<Long> teamMembersIdList = getTeamMembers().stream().map(memberTeam -> memberTeam.getMember().getId()).collect(toList());
        if (!teamMembersIdList.contains(checkMemberId)) {
            throw new NotMemberOfTeamException();
        }
    }

    private void removeMemberTeam(Member exitMember) {
        this.getTeamMembers().removeIf(memberTeam -> memberTeam.getMember().getId().equals(exitMember.getId()));
    }

    public boolean isFull() {
        return this.maxPeople == this.currentMemberCount;
    }

    public boolean isNewTeamOrZoneIsDifferent(Long zoneId) {
        return this.isNew() || this.zoneIsDifferent(zoneId);
    }

    private boolean isNew() {
        return this.getId() == null;
    }

    private boolean zoneIsDifferent(Long zoneId) {
        return !this.getZone().getId().equals(zoneId);
    }

    public MemberTeam changeLeaderReturnWasLeader(Member willLeader) {
        Member currentLeader = this.getLeader();
        checkNotTeamMember(willLeader.getId());
        removeTeamMember(willLeader);
        delegateLeader(willLeader);
        return addTeamMember(currentLeader);
    }

    public List<Member> getAllMembers() {
        List<Member> members = this.getTeamMembers().stream().map(MemberTeam::getMember).collect(toList());
        members.add(this.getLeader());
        return members;
    }

    public void addTeamMemberCount() {
        ++currentMemberCount;
        if (currentMemberCount > maxPeople) {
            throw new TeamMemberMaxException();
        }
    }
}
