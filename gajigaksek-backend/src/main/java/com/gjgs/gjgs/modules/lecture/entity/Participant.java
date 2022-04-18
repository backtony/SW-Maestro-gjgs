package com.gjgs.gjgs.modules.lecture.entity;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Participant extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY) @Column(name = "PARTICIPANT_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SCHEDULE_ID", nullable = false)
    private Schedule schedule;

    public static List<Participant> of(List<Member> members, Schedule schedule) {
        return members.stream().map(member -> Participant.of(member, schedule)).collect(toList());
    }

    public static Participant of(Member member, Schedule schedule) {
        return Participant.builder().member(member).schedule(schedule).build();
    }
}
