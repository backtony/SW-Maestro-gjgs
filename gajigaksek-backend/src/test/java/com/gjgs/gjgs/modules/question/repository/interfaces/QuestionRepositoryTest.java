package com.gjgs.gjgs.modules.question.repository.interfaces;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.dummy.QuestionDummy;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.question.entity.Question;
import com.gjgs.gjgs.modules.question.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


class QuestionRepositoryTest extends SetUpLectureTeamBulletinRepository {

    @Autowired QuestionRepository questionRepository;


    @BeforeEach
    void teardown(){
        questionRepository.deleteAll();
    }

    @DisplayName("username과 QuestionId로 question 찾기")
    @Test
    void find_by_member_username_and_id() throws Exception{
        //given
        Member questioner = anotherMembers.get(0);
        Question question = questionRepository.save(QuestionDummy.createWaitQuestion(lecture, questioner));

        flushAndClear();

        //when
        Question question2 = questionRepository.findByMemberUsernameAndId(questioner.getUsername(), question.getId()).get();

        //then
        assertEquals(question.getId(),question2.getId());
    }

    @DisplayName("username과 questionId로 question 존재여부 확인")
    @Test
    void exists_by_member_username_and_id() throws Exception{
        //given
        Member questioner = anotherMembers.get(0);
        Question question = questionRepository.save(QuestionDummy.createWaitQuestion(lecture, questioner));

        flushAndClear();

        //when, then
        assertTrue(questionRepository.existsByMemberUsernameAndId(questioner.getUsername(),question.getId()));
    }
}
