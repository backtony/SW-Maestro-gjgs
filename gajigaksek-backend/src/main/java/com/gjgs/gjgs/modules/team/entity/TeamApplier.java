package com.gjgs.gjgs.modules.team.entity;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"MEMBER_ID", "TEAM_ID"})
        }
)
public class TeamApplier extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_APPLIER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member applier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", nullable = false)
    private Team appliedTeam;

    public static TeamApplier of(Member applier, Team team) {
        return TeamApplier.builder().applier(applier).appliedTeam(team).build();
    }
}
