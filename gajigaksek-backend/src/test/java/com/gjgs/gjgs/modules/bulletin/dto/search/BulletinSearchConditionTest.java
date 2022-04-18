package com.gjgs.gjgs.modules.bulletin.dto.search;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

class BulletinSearchConditionTest {

    @Test
    @DisplayName("keyword 테스트 - 1, 빈 문자열일 경우")
    void keyword_empty_test() throws Exception {

        // given
        BulletinSearchCondition condition = BulletinSearchCondition
                .builder().keyword("").build();

        // when, then
        assertTrue(condition.getKeyword().isBlank());
    }

    @Test
    @DisplayName("keyword 테스트 - 2, 공백 문자열일 경우")
    void keyword_white_space_test() throws Exception {

        // given
        BulletinSearchCondition condition = BulletinSearchCondition
                .builder().keyword("             ").build();

        // when, then
        assertTrue(condition.getKeyword().isBlank());
    }

    @Test
    @DisplayName("keyword 테스트 - 3, 정상적인 키워드일 경우")
    void keyword_test() throws Exception {

        // given
        BulletinSearchCondition condition = BulletinSearchCondition
                .builder().keyword("test").build();

        // when, then
        assertFalse(condition.getKeyword().isBlank());
    }

    @Test
    @DisplayName("keyword 테스트 - 4, 앞 뒤에 공백이 있고, 키워드가 있는 경우")
    void keyword_white_space_mixed_test() throws Exception {

        // given
        BulletinSearchCondition condition = BulletinSearchCondition
                .builder().keyword("    test     test    ").build();

        // when, then
        assertFalse(condition.getKeyword().isBlank());
    }

    @Test
    @DisplayName("keyword 테스트 - 5, 앞 뒤에 공백이 있는 경우 trim 처리")
    void keyword_trim_test() throws Exception {

        // given
        BulletinSearchCondition condition = BulletinSearchCondition
                .builder().keyword("    test     test    ").build();

        // when
        String actual = StringUtils.trimWhitespace(condition.getKeyword());

        // then
        assertEquals("test     test", actual);
    }
}
