package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Participant;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.Team;

import java.util.ArrayList;
import java.util.List;

import static com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus.FULL;
import static com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus.RECRUIT;
import static java.time.LocalDate.now;
import static java.time.LocalTime.of;
import static java.util.stream.Collectors.toList;

public class ScheduleDummy {

    public static Schedule createSchedule(Lecture lecture) {
        return Schedule.builder()
                .id(10L)
                .lecture(lecture)
                .scheduleStatus(RECRUIT).lectureDate(now()).currentParticipants(0).startTime(of(18, 0)).endTime(of(20, 0)).progressMinutes(120).build();
    }

    public static Schedule createDataJpaTestSchedule(Lecture lecture) {
        return Schedule.builder()
                .lecture(lecture)
                .scheduleStatus(RECRUIT).lectureDate(now()).currentParticipants(0).startTime(of(18, 0)).endTime(of(20, 0)).progressMinutes(120).build();
    }

    public static Schedule createScheduleWith7Participants() {
        return Schedule.builder().currentParticipants(7)
                .scheduleStatus(RECRUIT)
                .participantList(new ArrayList<>(List.of(
                        Participant.builder().id(1L).member(Member.builder().id(1L).build()).build(),
                        Participant.builder().id(2L).member(Member.builder().id(2L).build()).build(),
                        Participant.builder().id(3L).member(Member.builder().id(6L).build()).build(),
                        Participant.builder().id(4L).member(Member.builder().id(8L).build()).build(),
                        Participant.builder().id(5L).member(Member.builder().id(10L).build()).build(),
                        Participant.builder().id(6L).member(Member.builder().id(11L).build()).build(),
                        Participant.builder().id(7L).member(Member.builder().id(55L).build()).build()
                )))
                .build();
    }

    public static Schedule createScheduleDuplicateParticipants(Member member) {
        return Schedule.builder().currentParticipants(7)
                .scheduleStatus(RECRUIT)
                .participantList(new ArrayList<>(List.of(
                        Participant.builder().id(1L).member(Member.builder().id(1L).build()).build(),
                        Participant.builder().id(2L).member(Member.builder().id(2L).build()).build(),
                        Participant.builder().id(3L).member(Member.builder().id(6L).build()).build(),
                        Participant.builder().id(4L).member(Member.builder().id(8L).build()).build(),
                        Participant.builder().id(5L).member(Member.builder().id(10L).build()).build(),
                        Participant.builder().id(6L).member(Member.builder().id(11L).build()).build(),
                        Participant.builder().id(7L).member(member).build()
                )))
                .build();
    }

    public static Schedule createScheduleWithTeamParticipant(Lecture lecture, Team team) {
        List<Member> allMembers = team.getAllMembers();
        return Schedule.builder().currentParticipants(allMembers.size()).lecture(lecture)
                .scheduleStatus(RECRUIT)
                .participantList(allMembers.stream().map(member -> Participant.builder()
                        .member(member)
                        .build()).collect(toList()))
                .build();
    }

    public static Schedule createScheduleWithParticipants(Lecture lecture, List<Member> participant) {
        return Schedule.builder().currentParticipants(participant.size())
                .lecture(lecture).scheduleStatus(FULL)
                .participantList(participant.stream().map(member -> Participant.builder()
                        .member(member)
                        .build()).collect(toList()))
                .build();
    }
}
