package com.batch.redisbatch.domain;

import com.batch.redisbatch.enums.TeamOrderStatus;
import lombok.*;

import javax.persistence.*;

import static com.batch.redisbatch.enums.TeamOrderStatus.CANCEL;
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

    @Id
    @GeneratedValue(strategy = IDENTITY) @Column(name = "TEAM_ORDER_ID")
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

    public void teamCancel() {
        currentPaymentCount = 0;
        teamOrderStatus = CANCEL;
    }

    public void changeStatus(TeamOrderStatus teamOrderStatus){
        this.teamOrderStatus = teamOrderStatus;
    }
}
