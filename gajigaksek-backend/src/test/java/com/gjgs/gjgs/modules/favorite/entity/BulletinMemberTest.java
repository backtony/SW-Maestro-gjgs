package com.gjgs.gjgs.modules.favorite.entity;

import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.dummy.BulletinDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BulletinMemberTest {

    @DisplayName("BulletinMember 생성")
    @Test
    void create_bulletinMember() throws Exception {
        //given
        Team team = TeamDummy.createUniqueTeam();
        Lecture lecture = LectureDummy.createLecture(1);
        Bulletin bulletin = BulletinDummy.createBulletin(team, lecture);
        Member member = MemberDummy.createTestMember();

        //when
        BulletinMember bulletinMember = BulletinMember.of(bulletin, member);

        //then
        assertAll(
                () -> assertEquals(bulletin, bulletinMember.getBulletin()),
                () -> assertEquals(member, bulletinMember.getMember())
        );
    }
}
