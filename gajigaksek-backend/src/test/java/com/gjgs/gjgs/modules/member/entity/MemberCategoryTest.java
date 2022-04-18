package com.gjgs.gjgs.modules.member.entity;


import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.dummy.CategoryDummy;
import com.gjgs.gjgs.modules.dummy.MemberDummy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MemberCategoryTest {
    @DisplayName("멤버 카테고리 생성")
    @Test
    void create_memberCategory() throws Exception {
        //given
        Member member = MemberDummy.createTestMember();
        Category category = CategoryDummy.createCategory();

        //when
        MemberCategory memberCategory = MemberCategory.of(member, category);

        //then
        assertAll(
                () -> assertEquals(member, memberCategory.getMember()),
                () -> assertEquals(category, memberCategory.getCategory())
        );
    }
}
