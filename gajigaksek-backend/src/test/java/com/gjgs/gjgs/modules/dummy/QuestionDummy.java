package com.gjgs.gjgs.modules.dummy;


import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.dto.mypage.MyQuestionDto;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.question.entity.Question;
import com.gjgs.gjgs.modules.question.enums.QuestionStatus;

import java.time.LocalDateTime;

public class QuestionDummy {

    public static Question createWaitQuestion(Lecture lecture, Member member) {
        return Question.builder()
                .lecture(lecture)
                .member(member)
                .mainText("Test Main Text")
                .questionStatus(QuestionStatus.WAIT)
                .build();
    }

    public static Question createCompleteQuestion(Lecture lecture, Member member) {
        return Question.builder()
                .lecture(lecture)
                .member(member)
                .mainText("Test Main Text")
                .replyText("Test Reply Text")
                .questionStatus(QuestionStatus.COMPLETE)
                .build();
    }

    public static Question createOnlyMainTextQuestion() {
        return Question.builder()
                .mainText("Test Main Text")
                .build();
    }

    public static MyQuestionDto createMyQuestionDto() {
        return MyQuestionDto.builder()
                .questionId(1L)
                .classTitle("Title")
                .directorNickname("director")
                .mainText("mainText")
                .replyText("replyText")
                .questionStatus(QuestionStatus.WAIT)
                .questionDate(LocalDateTime.now())
                .build();
    }

}
