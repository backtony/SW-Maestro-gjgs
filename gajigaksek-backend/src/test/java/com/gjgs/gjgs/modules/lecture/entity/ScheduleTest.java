package com.gjgs.gjgs.modules.lecture.entity;

import com.gjgs.gjgs.modules.dummy.ScheduleDummy;
import com.gjgs.gjgs.modules.exception.schedule.*;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus.*;
import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {

    @Test
    @DisplayName("해당 스케줄이 1명 있을 경우 예외 발생")
    void delete_schedule_should_not_delete_test() {

        // given
        Schedule schedule = Schedule.builder()
                .scheduleStatus(RECRUIT)
                .currentParticipants(1)
                .build();

        // when, then
        assertThrows(CanNotDeleteScheduleException.class,
                schedule::canDelete,
                "지우지 못할 경우 에러 발생");
    }

    @Test
    @DisplayName("참가자 1명 넣어주기")
    void add_participant_test() throws Exception {

        // given
        Member member = Member.builder().id(30L).build();
        Member director = Member.builder().id(20L).build();
        Lecture lecture = Lecture.builder().director(director).maxParticipants(10).build();
        Schedule schedule = Schedule.builder().id(1L).lecture(lecture).scheduleStatus(RECRUIT).currentParticipants(0).build();
        lecture.addSchedule(schedule);

        // when
        schedule.addParticipants(List.of(member));

        // then
        assertAll(
                () -> assertEquals(schedule.getParticipantList().size(), 1),
                () -> assertEquals(schedule.getParticipantList().get(0).getMember(), member)
        );
    }

    @Test
    @DisplayName("모집 중인 상태가 아닐 경우 예외 발생")
    void add_participants_should_bulletin_is_recruit() throws Exception {

        // given
        Member participant = Member.builder().id(1L).build();
        Schedule schedule = Schedule.builder().scheduleStatus(HOLD).build();

        // when, then
        assertThrows(ScheduleNotRecruitException.class,
                () -> schedule.addParticipants(List.of(participant)),
                "모집 중인 상태가 아닐 경우 예외 발생");
    }

    @Test
    @DisplayName("참여 인원이 다 찼을 경우 자동으로 FULL 상태 변경 처리")
    void add_participant_full_test() throws Exception {

        // given
        Member member = Member.builder().id(30L).build();
        Member director = Member.builder().id(20L).build();
        Lecture lecture = Lecture.builder().director(director).maxParticipants(2).build();
        Schedule schedule = Schedule.builder().id(1L).currentParticipants(1).lecture(lecture).scheduleStatus(RECRUIT).build();
        lecture.addSchedule(schedule);

        // when
        schedule.addParticipants(List.of(member));

        // then
        assertEquals(schedule.getScheduleStatus(), FULL);
    }

    @Test
    @DisplayName("스케줄이 CLOSED나 END일 경우 예외 발생")
    void add_schedule_should_not_schedule_status_is_closed_and_end() throws Exception {

        // given
        List<Member> teamMembers = createTeamMembers();
        Schedule closeSchedule = Schedule.builder().scheduleStatus(CLOSE).build();

        // when, then
        assertThrows(CanNotExitScheduleException.class,
                () -> closeSchedule.cancelAndReturnParticipantIdList(teamMembers),
                "닫힌 클래스의 경우 예외 발생");
    }

    @Test
    @DisplayName("스케줄에 속한 팀원들의 신청을 취소한다. / 만약 팀 인원이 4명이어도 3명만 감소시킨다.")
    void cancel_test() throws Exception {

        // given
        Schedule with7Participants = ScheduleDummy.createScheduleWith7Participants();
        List<Member> cancelMembers = createTeamMembers();
        cancelMembers.add(Member.builder().id(31L).build());

        // when
        List<Long> cancelParticipantIdList = with7Participants.cancelAndReturnParticipantIdList(cancelMembers);

        // then
        assertAll(
                () -> assertEquals(with7Participants.getCurrentParticipants(), 4),
                () -> assertEquals(with7Participants.getParticipantList().size(), 4),
                () -> assertEquals(cancelParticipantIdList.size(), 3)
        );
    }

    @Test
    @DisplayName("중복된 멤버가 있을 경우 예외 발생")
    void check_duplicate_member_exception() throws Exception {

        // given
        List<Member> teamMembers = createTeamMembers();
        Schedule schedule = ScheduleDummy.createScheduleDuplicateParticipants(teamMembers.get(0));

        // when, then
        assertThrows(AlreadyEnteredScheduledException.class,
                () -> schedule.addParticipants(teamMembers),
                "중복된 회원이 있을 경우 예외 발생");
    }

    @Test
    @DisplayName("현재 시간이 스케줄이 끝나는 시간을 지나지 않을 경우 예외 발생")
    void set_end_exception_test() throws Exception {

        // given
        Schedule schedule = Schedule.builder().endTime(LocalTime.of(10, 0))
                .lectureDate(LocalDate.now().plusDays(2))
                .build();

        // when, then
        assertThrows(NotPassDeadlineScheduleException.class,
                schedule::setEnd);
    }

    @Test
    @DisplayName("스케줄 마감하기")
    void set_end_test() throws Exception {

        // given
        Schedule schedule = Schedule.builder().endTime(LocalTime.of(10, 0))
                .lectureDate(LocalDate.now().minusDays(1))
                .build();

        // when
        schedule.setEnd();

        // then
        assertEquals(schedule.getScheduleStatus(), END);
    }

    private List<Member> createTeamMembers() {
        return new ArrayList<>(List.of(
                Member.builder().id(6L).build(),
                Member.builder().id(8L).build(),
                Member.builder().id(10L).build()
        ));
    }
}
