package com.gjgs.gjgs.modules.payment.entity;

import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.payment.enums.TeamOrderStatus;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

import static com.gjgs.gjgs.modules.payment.enums.TeamOrderStatus.*;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class TeamOrder extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY) @Column(name = "TEAM_ORDER_ID")
    private Long id;

    @Column(nullable = false)
    private Long teamId;

    @Column(nullable = false)
    private Long scheduleId;

    @Column(nullable = false)
    private int completePaymentCount;

    @Column(nullable = false)
    private int currentPaymentCount;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TeamOrderStatus teamOrderStatus;

    public static TeamOrder of(Schedule schedule, Team team) {
        return TeamOrder.builder()
                .teamId(team.getId())
                .scheduleId(schedule.getId())
                .completePaymentCount(team.getCurrentMemberCount())
                .currentPaymentCount(0)
                .teamOrderStatus(WAIT)
                .build();
    }

    public void paid() {
        ++currentPaymentCount;
        if (currentPaymentCount == completePaymentCount) {
            teamOrderStatus = COMPLETE;
        }
    }

    public void cancel() {
        if (currentPaymentCount == completePaymentCount
                && teamOrderStatus.equals(COMPLETE)) {
            teamOrderStatus = WAIT;
        }
        --currentPaymentCount;
    }

    public void teamCancel() {
        currentPaymentCount = 0;
        teamOrderStatus = CANCEL;
    }
}
