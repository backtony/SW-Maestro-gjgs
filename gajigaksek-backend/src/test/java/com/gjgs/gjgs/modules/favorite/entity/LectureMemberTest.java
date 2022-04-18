package com.gjgs.gjgs.modules.favorite.entity;

import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LectureMemberTest {

    @DisplayName("LectureMember 생성")
    @Test
    void create_lectureMember() throws Exception {
        //given
        Lecture lecture = LectureDummy.createLecture(1);
        Member member = MemberDummy.createTestMember();

        //when
        LectureMember lectureMember = LectureMember.of(lecture, member);

        //then
        assertAll(
                () -> assertEquals(lecture, lectureMember.getLecture()),
                () -> assertEquals(member, lectureMember.getMember())
        );
    }
}
