package com.gjgs.gjgs.modules.lecture.aop;

import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.question.entity.Question;
import com.gjgs.gjgs.modules.utils.security.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckDirectorAspectTest {

    @Mock SecurityUtil securityUtil;
    @InjectMocks CheckDirectorAspect aspect;

    @Test
    @DisplayName("디렉터인지 확인하기")
    void check_director_test() throws Exception {

        // given
        Member director = Member.from("test");
        stubbingGetUsername(director);
        Lecture lecture = Lecture.builder()
                .director(director)
                .build();
        Member questioner = Member.from("test2");
        Question question = Question.builder()
                .member(questioner)
                .lecture(lecture)
                .build();
        stubbingGetUsername(questioner);

        // when
        aspect.checkQuestionLectureDirector(question);

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> assertDoesNotThrow(() -> aspect.checkQuestionLectureDirector(question))
        );
    }

    @Test
    @DisplayName("디렉터가 아닌지 확인하기")
    void check_not_director_test() throws Exception {

        // given
        Member director = Member.from("test");
        Member notDirector = Member.from("notdirector");
        stubbingGetUsername(notDirector);
        Lecture lecture = Lecture.builder()
                .director(director)
                .build();

        // when
        aspect.checkNotDirector(lecture);

        // then
        assertAll(
                () -> verify(securityUtil).getCurrentUsername(),
                () -> assertDoesNotThrow(() -> aspect.checkNotDirector(lecture))
        );
    }

    private void stubbingGetUsername(Member director) {
        when(securityUtil.getCurrentUsername()).thenReturn(Optional.of(director.getUsername()));
    }
}
