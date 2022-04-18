package com.gjgs.gjgs.modules.team.entity;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"MEMBER_ID", "TEAM_ID"},
                        name = "UK_MEMBER_TEAM_MEMBER_ID_TEAM_ID")
        }

)

public class MemberTeam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_TEAM_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", nullable = false)
    private Team team;

    public static MemberTeam of(Member member, Team team) {
        return MemberTeam.builder()
                .team(team)
                .member(member)
                .build();
    }
}
