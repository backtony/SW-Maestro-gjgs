package com.gjgs.gjgs.modules.question.entity;


import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.question.dto.AnswerRequest;
import com.gjgs.gjgs.modules.question.dto.QuestionRequest;
import com.gjgs.gjgs.modules.question.enums.QuestionStatus;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

import static com.gjgs.gjgs.modules.question.enums.QuestionStatus.COMPLETE;
import static com.gjgs.gjgs.modules.question.enums.QuestionStatus.WAIT;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LECTURE_ID", nullable = false)
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String mainText;

    private String replyText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus questionStatus;

    public static Question of(Lecture lecture, Member questioner, QuestionRequest request) {
        return Question.builder()
                .lecture(lecture)
                .mainText(request.getQuestionForm().getMainText())
                .member(questioner)
                .questionStatus(QuestionStatus.WAIT)
                .build();
    }

    public void changeMainText(String mainText) {
        this.mainText = mainText;
    }

    public void putAnswer(AnswerRequest request) {
        if (questionStatus == WAIT) {
            questionStatus = COMPLETE;
        }
        replyText = request.getReplyText();
    }

    public void checkDirector(Member currentMember) {
        lecture.checkNotDirector(lecture.getDirector(), currentMember);
    }
}
