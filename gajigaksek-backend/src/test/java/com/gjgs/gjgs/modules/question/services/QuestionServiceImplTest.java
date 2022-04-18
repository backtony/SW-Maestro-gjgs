package com.gjgs.gjgs.modules.question.services;

import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.QuestionDummy;
import com.gjgs.gjgs.modules.exception.question.QuestionNotFoundException;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.services.authority.LectureDirectorAuthorityCheckable;
import com.gjgs.gjgs.modules.member.dto.mypage.QuestionMainTextModifyRequest;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.question.dto.AnswerRequest;
import com.gjgs.gjgs.modules.question.dto.QuestionRequest;
import com.gjgs.gjgs.modules.question.dto.QuestionResponse;
import com.gjgs.gjgs.modules.question.dto.QuestionResult;
import com.gjgs.gjgs.modules.question.entity.Question;
import com.gjgs.gjgs.modules.question.repository.QuestionRepository;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.gjgs.gjgs.modules.question.enums.QuestionStatus.COMPLETE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock SecurityUtil securityUtil;
    @Mock LectureDirectorAuthorityCheckable lectureAuthorityCheckable;
    @Mock QuestionRepository questionRepository;
    @InjectMocks QuestionServiceImpl questionService;

    @Test
    @DisplayName("문의글 생성하기")
    void create_question_test() throws Exception {

        // given
        Lecture lecture = LectureDummy.createLectureWithIdDirector(1);
        QuestionRequest request = QuestionRequest.builder()
                .lectureId(lecture.getId())
                .questionForm(QuestionMainTextModifyRequest.builder()
                        .mainText("테스트 작성글 입니다.")
                        .build())
                .build();
        when(lectureAuthorityCheckable.findLecture(lecture.getId()))
                .thenReturn(lecture);

        Member questioner = Member.builder()
                .username("question").id(2L)
                .build();
        stubbingSecurityUtil(questioner);

        // when
        QuestionResponse response = questionService.createQuestion(request);

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> verify(lectureAuthorityCheckable).findLecture(lecture.getId()),
                () -> verify(questionRepository).save(any()),
                () -> assertEquals(response.getLectureId(), lecture.getId())
        );
    }


    @Test
    @DisplayName("문의글 없을 시 에러 발생")
    void get_question_not_found_exception_test() throws Exception {

        // given
        doThrow(QuestionNotFoundException.class)
                .when(questionRepository).findWithLectureQuestioner(any());

        // when, then
        assertThrows(QuestionNotFoundException.class,
                () -> questionService.getQuestion(1L),
                "문의글이 없을 경우 예외 발생");
    }

    @Test
    @DisplayName("문의글 답변하기")
    void put_answer_test() throws Exception {

        // given
        Member questioner = Member.builder()
                .username("question").id(2L)
                .build();
        Lecture lecture = LectureDummy.createLectureWithIdDirector(1);
        Question question = QuestionDummy.createWaitQuestion(lecture, questioner);
        AnswerRequest request = AnswerRequest
                .builder()
                .replyText("테스트 답글")
                .build();
        when(lectureAuthorityCheckable.findQuestion(question.getId()))
                .thenReturn(question);

        // when
        QuestionResponse res = questionService.putAnswer(question.getId(), request);

        // then
        assertAll(
                () -> assertEquals(res.getQuestionId(), question.getId()),
                () -> assertEquals(res.getResult(), QuestionResult.REPLY.getDescription()),
                () -> assertEquals(res.getLectureId(), lecture.getId()),
                () -> assertEquals(question.getQuestionStatus(), COMPLETE),
                () -> assertEquals(question.getReplyText(), request.getReplyText())
        );
    }

    private void stubbingSecurityUtil(Member notDirector) {
        when(securityUtil.getCurrentUsername())
                .thenReturn(Optional.of(notDirector.getUsername()));
    }
}
