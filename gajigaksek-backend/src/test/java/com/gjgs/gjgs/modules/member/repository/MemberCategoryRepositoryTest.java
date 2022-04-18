package com.gjgs.gjgs.modules.member.repository;


import com.gjgs.gjgs.config.repository.SetUpMemberRepository;
import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.dummy.CategoryDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCategory;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberCategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class MemberCategoryRepositoryTest extends SetUpMemberRepository {

    @Autowired MemberCategoryRepository memberCategoryRepository;

    @AfterEach
    void teardown(){
        memberCategoryRepository.deleteAll();
    }

    @DisplayName("내가 선호하는 카테고리 벌크성 삭제")
    @Test
    void delete_all_with_bulk_by_id() throws Exception {

        //given
        Member member = anotherMembers.get(0);
        Category category1 = categoryRepository.save(CategoryDummy.createCategory(1));
        Category category2 = categoryRepository.save(CategoryDummy.createCategory(2));

        member.setMemberCategories(Arrays.asList(category1, category2));
        MemberCategory savedMc1 = memberCategoryRepository.save(MemberCategory.of(member, category1));
        MemberCategory savedMc2 = memberCategoryRepository.save(MemberCategory.of(member, category2));
        flushAndClear();

        //when
        memberCategoryRepository.deleteAllWithBulkById(List.of(savedMc1.getId(), savedMc2.getId()));
        Optional<MemberCategory> memberCategory1 = memberCategoryRepository.findById(savedMc1.getId());
        Optional<MemberCategory> memberCategory2 = memberCategoryRepository.findById(savedMc2.getId());

        //then
        assertAll(
                () -> assertEquals(Optional.empty(), memberCategory1),
                () -> assertEquals(Optional.empty(), memberCategory2)
        );
    }


    @DisplayName("memberId로 memberCategory 존재하는지 여부 확인")
    @Test
    void exists_by_member_id() throws Exception{
        //given
        Member member = anotherMembers.get(0);
        memberCategoryRepository.save(MemberCategory.of(member,category));
        flushAndClear();

        //when then
        assertTrue(memberCategoryRepository.existsByMemberId(member.getId()));
    }

    @DisplayName("memberId로 memberCategory 벌크 삭제")
    @Test
    void delete_all_with_bulk_by_memberId() throws Exception{
        //given
        Member member = anotherMembers.get(0);
        Category category2 = categoryRepository.save(CategoryDummy.createCategory(2));
        memberCategoryRepository.save(MemberCategory.of(member,category));
        memberCategoryRepository.save(MemberCategory.of(member,category2));
        flushAndClear();

        //when
        memberCategoryRepository.deleteAllWithBulkByMemberId(member.getId());
        flushAndClear();

        //then
        assertFalse(memberCategoryRepository.existsByMemberId(member.getId()));
    }



}
