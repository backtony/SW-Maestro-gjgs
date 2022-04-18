package com.gjgs.gjgs.modules.member.repository.interfaces;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberRepositoryTest extends SetUpLectureTeamBulletinRepository {

    @Autowired MemberCategoryRepository memberCategoryRepository;

    @DisplayName("username으로 회원 id 찾기")
    @Test
    void find_id_by_username() throws Exception{
        // given
        flushAndClear();

        //when
        Member member = anotherMembers.get(0);
        Long memberId = memberRepository.findIdByUsername(member.getUsername()).get();

        //then
        assertEquals(member.getId(),memberId);
    }

    @DisplayName("username으로 memberCategory 까지 페치 조인한 멤버 가져오기")
    @Test
    void find_with_memberCategory_by_username() throws Exception{
        //given
        Member member = anotherMembers.get(0);
        memberCategoryRepository.save(MemberCategory.of(member, category));
        flushAndClear();

        //when
        Member m1 = memberRepository.findWithMemberCategoryByUsername(member.getUsername()).get();

        //then
        assertAll(
                () -> assertEquals(1,m1.getMemberCategories().size()),
                () -> assertEquals(member.getUsername(),m1.getUsername())
        );
    }

}
