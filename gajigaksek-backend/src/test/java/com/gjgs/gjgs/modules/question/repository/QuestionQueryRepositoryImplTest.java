package com.gjgs.gjgs.modules.question.repository;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.dummy.QuestionDummy;
import com.gjgs.gjgs.modules.lecture.dtos.LectureQuestionsResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.MyQuestionDto;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.question.dto.QuestionDetailResponse;
import com.gjgs.gjgs.modules.question.entity.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuestionQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired QuestionRepository questionRepository;

    @AfterEach
    void teardown(){
        questionRepository.deleteAll();
    }

    @DisplayName("내가 작성한 문의글 가져오기")
    @Test
    void find_my_questions_by_username() throws Exception {

        //given
        Member member = anotherMembers.get(0);
        Question waitQuestion = questionRepository.save(QuestionDummy.createWaitQuestion(lecture, member));
        Question completeQuestion = questionRepository.save(QuestionDummy.createCompleteQuestion(lecture, member));
        flushAndClear();

        //when
        List<MyQuestionDto> myQuestionDtoList
                = questionRepository.findMyQuestionsByUsername(member.getUsername());

        //then
        assertAll(
                () -> assertEquals(2, myQuestionDtoList.size()),
                () -> assertEquals(lecture.getTitle(), myQuestionDtoList.get(0).getClassTitle()),
                () -> assertEquals(lecture.getDirector().getNickname(), myQuestionDtoList.get(0).getDirectorNickname()),
                () -> assertEquals(waitQuestion.getMainText(), myQuestionDtoList.get(0).getMainText()),
                () -> assertNull(myQuestionDtoList.get(0).getReplyText()),
                () -> assertEquals(waitQuestion.getQuestionStatus(), myQuestionDtoList.get(0).getQuestionStatus()),


                () -> assertEquals(lecture.getTitle(), myQuestionDtoList.get(1).getClassTitle()),
                () -> assertEquals(lecture.getDirector().getNickname(), myQuestionDtoList.get(1).getDirectorNickname()),
                () -> assertEquals(waitQuestion.getMainText(), myQuestionDtoList.get(1).getMainText()),
                () -> assertEquals(completeQuestion.getReplyText(), myQuestionDtoList.get(1).getReplyText()),
                () -> assertEquals(completeQuestion.getQuestionStatus(), myQuestionDtoList.get(1).getQuestionStatus())
        );
    }

    @Test
    @DisplayName("문의글과 클래스 한번에 가져오기, 답변 대기중인 문의글")
    void find_with_lecture_wait_test() throws Exception {

        // given
        Member questioner = anotherMembers.get(0);
        Question question = questionRepository.save(QuestionDummy.createWaitQuestion(lecture, questioner));
        flushAndClear();

        // when
        QuestionDetailResponse res = questionRepository.findWithLectureQuestioner(question.getId()).orElseThrow();

        // then
        QuestionDetailResponse.QuestionDetail questionDetail = res.getQuestionDetail();
        QuestionDetailResponse.AnswerDetail answerDetail = res.getAnswerDetail();
        assertAll(
                () -> assertEquals(res.getLectureId(), lecture.getId()),
                () -> assertEquals(res.getQuestionId(), question.getId()),
                () -> assertEquals(res.getQuestionStatus(), "WAIT"),
                () -> assertEquals(questionDetail.getQuestionMainText(), question.getMainText()),
                () -> assertEquals(questionDetail.getQuestionerNickname(), questioner.getNickname()),
                () -> assertEquals(questionDetail.getQuestionerProfileImageUrl(), questioner.getImageFileUrl()),
                () -> assertThat(answerDetail.getReplyText()).isNull(),
                () -> assertThat(answerDetail.getDirectorProfileImageUrl()).isEqualTo(director.getImageFileUrl()),
                () -> assertThat(answerDetail.getDirectorNickname()).isEqualTo(director.getNickname())
        );
    }

    @Test
    @DisplayName("문의글과 클래스 한번에 가져오기, 답변 완료된 문의글")
    void find_with_lecture_complete_test() throws Exception {

        // given
        Member questioner = anotherMembers.get(0);
        Question question = questionRepository.save(QuestionDummy.createCompleteQuestion(lecture, questioner));
        flushAndClear();

        // when
        QuestionDetailResponse res = questionRepository.findWithLectureQuestioner(question.getId()).orElseThrow();

        // then
        QuestionDetailResponse.QuestionDetail questionDetail = res.getQuestionDetail();
        QuestionDetailResponse.AnswerDetail answerDetail = res.getAnswerDetail();
        assertAll(
                () -> assertEquals(res.getLectureId(), lecture.getId()),
                () -> assertEquals(res.getQuestionId(), question.getId()),
                () -> assertEquals(res.getQuestionStatus(), "COMPLETE"),
                () -> assertEquals(questionDetail.getQuestionMainText(), question.getMainText()),
                () -> assertEquals(questionDetail.getQuestionerNickname(), questioner.getNickname()),
                () -> assertEquals(questionDetail.getQuestionerProfileImageUrl(), questioner.getImageFileUrl()),
                () -> assertThat(answerDetail.getReplyText()).isEqualTo(question.getReplyText()),
                () -> assertThat(answerDetail.getDirectorProfileImageUrl()).isEqualTo(director.getImageFileUrl()),
                () -> assertThat(answerDetail.getDirectorNickname()).isEqualTo(director.getNickname())
        );
    }

    @Test
    @DisplayName("문의글과 클래스, 디렉터 정보 한번에 가져오기")
    void find_with_lecture_director_test() throws Exception {

        // given
        Member questioner = anotherMembers.get(0);
        Question question = questionRepository.save(QuestionDummy.createWaitQuestion(lecture, questioner));
        flushAndClear();

        // when
        Question findQuestion = questionRepository.findWithLectureDirector(question.getId()).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(findQuestion.getId(), question.getId()),
                () -> assertEquals(findQuestion.getMainText(), question.getMainText()),
                () -> assertEquals(findQuestion.getLecture().getId(), lecture.getId()),
                () -> assertEquals(findQuestion.getLecture().getDirector().getId(), director.getId())
        );
    }

    @Test
    @DisplayName("클래스의 질문글 가져오기")
    void find_lecture_questions_test() throws Exception {

        // given
        Member questioner = anotherMembers.get(0);
        questionRepository.save(QuestionDummy.createWaitQuestion(lecture, questioner));
        flushAndClear();

        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<LectureQuestionsResponse> response = questionRepository.findLectureQuestions(lecture.getId(), pageRequest);

        // then
        assertAll(
                () -> assertEquals(response.getContent().size(), 1),
                () -> assertEquals(response.getTotalElements(), 1)
        );
    }
}
