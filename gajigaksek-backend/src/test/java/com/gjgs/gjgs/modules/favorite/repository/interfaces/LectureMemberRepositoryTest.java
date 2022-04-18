package com.gjgs.gjgs.modules.favorite.repository.interfaces;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import com.gjgs.gjgs.modules.favorite.entity.LectureMember;
import com.gjgs.gjgs.modules.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LectureMemberRepositoryTest extends SetUpLectureTeamBulletinRepository {

    @Autowired LectureMemberRepository lectureMemberRepository;

    @DisplayName("LectureMember 조회시 Member 같이 가져오기")
    @Test
    void find_lectureMember_with_member_by_id() throws Exception {

        //given
        Member director = memberRepository.save(MemberDummy.createDataJpaTestDirector(zone, category));
        lectureRepository.save(LectureDummy.createDataJpaTestLecture(zone, category, director));

        Member member = anotherMembers.get(0);
        LectureMember saveLm = lectureMemberRepository.save(LectureMember.of(lecture, member));
        flushAndClear();

        //when
        LectureMember lm1 = lectureMemberRepository.findLectureMemberWithMemberById(saveLm.getId()).get();

        //then
        assertAll(
                () -> assertEquals(member.getUsername(), lm1.getMember().getUsername()),
                () -> assertEquals(member.getPhone(), lm1.getMember().getPhone()),
                () -> assertEquals(member.getNickname(), lm1.getMember().getNickname()),
                () -> assertEquals(member.getName(), lm1.getMember().getName()),
                () -> assertEquals(member.getProfileText(), lm1.getMember().getProfileText()),
                () -> assertEquals(member.getImageFileName(), lm1.getMember().getImageFileName()),
                () -> assertEquals(member.getSex(), lm1.getMember().getSex()),
                () -> assertEquals(member.getAuthority(), lm1.getMember().getAuthority()),
                () -> assertEquals(member.getDirectorText(), lm1.getMember().getDirectorText())
        );
    }

    @DisplayName("memberId와 LectureId로 LectureMember 찾기")
    @Test
    void find_by_memberId_and_lectureId() throws Exception {

        //given
        Member member = anotherMembers.get(0);
        LectureMember lectureMember = LectureMember.of(lecture, member);
        lectureMemberRepository.save(lectureMember);
        flushAndClear();

        //when
        LectureMember lm1 = lectureMemberRepository
                .findByMemberIdAndLectureId(member.getId(), lecture.getId()).get();

        //then
        assertAll(
                () -> assertEquals(member.getUsername(), lm1.getMember().getUsername()),
                () -> assertEquals(lecture.getThumbnailImageFileUrl(), lm1.getLecture().getThumbnailImageFileUrl()),
                () -> assertEquals(lecture.getThumbnailImageFileName(), lm1.getLecture().getThumbnailImageFileName()),
                () -> assertEquals(lecture.getTitle(), lm1.getLecture().getTitle()),
                () -> assertEquals(lecture.getFavoriteCount(), lm1.getLecture().getFavoriteCount()),
                () -> assertEquals(lecture.getPrice(), lm1.getLecture().getPrice()),
                () -> assertEquals(lecture.getMainText(), lm1.getLecture().getMainText()),
                () -> assertEquals(lecture.getMaxParticipants(), lm1.getLecture().getMaxParticipants()),
                () -> assertEquals(lecture.getMinParticipants(), lm1.getLecture().getMinParticipants()),
                () -> assertEquals(lecture.getProgressTime(), lm1.getLecture().getProgressTime()),
                () -> assertEquals(lecture.getClickCount(), lm1.getLecture().getClickCount()),
                () -> assertEquals(lecture.getFullAddress(), lm1.getLecture().getFullAddress())
        );
    }
}
