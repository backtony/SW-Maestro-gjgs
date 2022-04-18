package com.batch.redisbatch.domain.lecture;

import com.batch.redisbatch.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.batch.redisbatch.domain.lecture.ScheduleStatus.*;
import static javax.persistence.EnumType.STRING;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LECTURE_ID", nullable = false)
    private Lecture lecture;

    @Column(nullable = false)
    private LocalDate lectureDate;

    @Column(nullable = false)
    private int currentParticipants;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private int progressMinutes;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ScheduleStatus scheduleStatus;

    public void setCurrentMember(int size) {
        checkStatusIsClosedOrEnd();
        this.currentParticipants -= size;
        changeRecruitStatus();
    }

    private void checkStatusIsClosedOrEnd() {
        if (isCloseOrEnd()) {
            throw new IllegalStateException("클래스가 종료되었으면 취소할 수 없습니다.");
        }
    }

    private boolean isCloseOrEnd() {
        return this.getScheduleStatus().equals(CLOSE) || this.getScheduleStatus().equals(END);
    }

    private void changeRecruitStatus() {
        if (this.getScheduleStatus().equals(FULL)) {
            this.scheduleStatus = RECRUIT;
        }
    }
}
