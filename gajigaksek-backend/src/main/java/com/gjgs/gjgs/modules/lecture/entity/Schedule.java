package com.gjgs.gjgs.modules.lecture.entity;

import com.gjgs.gjgs.modules.exception.schedule.*;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus.*;
import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;
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

    @OneToMany(mappedBy = "schedule")
    @Builder.Default
    private List<Participant> participantList = new ArrayList<>();

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

    public static List<Schedule> of(List<CreateLecture.ScheduleDto> scheduleList, Lecture lecture) {
        return scheduleList.stream()
                .map(scheduleDto -> Schedule.of(scheduleDto, lecture))
                .collect(toList());
    }

    public static Schedule of(CreateLecture.ScheduleDto scheduleDto, Lecture lecture) {
        return Schedule.builder()
                .lecture(lecture)
                .lectureDate(scheduleDto.getLectureDate())
                .currentParticipants(0)
                .startTime(LocalTime.of(scheduleDto.getStartHour(), scheduleDto.getStartMinute()))
                .endTime(LocalTime.of(scheduleDto.getEndHour(), scheduleDto.getEndMinute()))
                .progressMinutes(scheduleDto.getProgressMinute())
                .scheduleStatus(RECRUIT)
                .build();
    }

    public LocalDateTime getStartLocalDateTime() {
        return LocalDateTime.of(this.getLectureDate(), this.getStartTime());
    }

    public void canDelete() {
        if (existParticipants()) {
            throw new CanNotDeleteScheduleException();
        }
    }

    private boolean existParticipants() {
        return this.getCurrentParticipants() > 0;
    }

    public void addParticipants(List<Member> applyTeamMembers) {
        enableToEnter(applyTeamMembers);
        List<Participant> participantList = Participant.of(applyTeamMembers, this);
        this.getParticipantList().addAll(participantList);
        this.currentParticipants += participantList.size();
        changeFullStatus();
    }

    private void enableToEnter(List<Member> applyTeamMembers) {
        isRecruit();
        haveDuplicateMember(applyTeamMembers);
        containsParticipantsInDirector(applyTeamMembers);
        isOverParticipants(applyTeamMembers);
    }

    private void isRecruit() {
        if (!this.getScheduleStatus().equals(RECRUIT)) {
            throw new ScheduleNotRecruitException();
        }
    }

    private void haveDuplicateMember(List<Member> applyTeamMembers) {
        List<Long> participantIdList = this.getParticipantList().stream().map(participant -> participant.getMember().getId()).collect(toList());
        List<Long> applyMemberIdList = applyTeamMembers.stream().map(Member::getId).collect(toList());
        applyMemberIdList.forEach(applyMemberId -> {
            if (participantIdList.contains(applyMemberId)) {
                throw new AlreadyEnteredScheduledException();
            }
        });
    }

    private void containsParticipantsInDirector(List<Member> applyTeamMembers) {
        if (applyTeamMembers.contains(this.getLecture().getDirector())) {
            throw new DirectorCanNotParticipateMyClassException();
        }
    }

    private void isOverParticipants(List<Member> applyTeamMembers) {
        if (this.getCurrentParticipants() + applyTeamMembers.size() > this.getLecture().getMaxParticipants()) {
            throw new ExceedScheduleParticipantException();
        }
    }

    private void changeFullStatus() {
        if (this.getLecture().getMaxParticipants() == this.currentParticipants) {
            this.scheduleStatus = FULL;
        }
    }

    public List<Long> cancelAndReturnParticipantIdList(List<Member> teamMembers) {
        checkStatusIsClosedOrEnd();
        List<Long> teamMemberIdList = teamMembers.stream().map(Member::getId).collect(toList());
        List<Participant> cancelParticipantList = this.getParticipantList().stream().filter(participant -> teamMemberIdList.contains(participant.getMember().getId())).collect(toList());
        removeCancelParticipantList(cancelParticipantList);
        changeRecruitStatus();
        return cancelParticipantList.stream().map(Participant::getId).collect(toList());
    }

    private void checkStatusIsClosedOrEnd() {
        if (isCloseOrEnd()) {
            throw new CanNotExitScheduleException();
        }
    }

    private boolean isCloseOrEnd() {
        return this.getScheduleStatus().equals(CLOSE) || this.getScheduleStatus().equals(END);
    }

    private void removeCancelParticipantList(List<Participant> cancelParticipantList) {
        this.getParticipantList().removeIf(cancelParticipantList::contains);
        this.currentParticipants -= cancelParticipantList.size();
    }

    private void changeRecruitStatus() {
        if (this.getScheduleStatus().equals(FULL)) {
            this.scheduleStatus = RECRUIT;
        }
    }

    public int getLecturePrice(int memberSize) {
        return this.getLecture().getPricePerMember(memberSize);
    }

    public Long getLectureId() {
        return this.getLecture().getId();
    }

    public void setEnd() {
        if(now().isBefore(LocalDateTime.of(lectureDate, endTime))) {
            throw new NotPassDeadlineScheduleException();
        }
        this.scheduleStatus = END;
    }
}
