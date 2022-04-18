package com.gjgs.gjgs.modules.lecture.services.authority;

import com.gjgs.gjgs.modules.exception.lecture.LectureNotFoundException;
import com.gjgs.gjgs.modules.exception.question.QuestionNotFoundException;
import com.gjgs.gjgs.modules.lecture.aop.CheckDirector;
import com.gjgs.gjgs.modules.lecture.aop.CheckNotDirector;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepository;
import com.gjgs.gjgs.modules.question.entity.Question;
import com.gjgs.gjgs.modules.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureDirectorAuthorityCheckableImpl implements LectureDirectorAuthorityCheckable {

    private final LectureQueryRepository lectureQueryRepository;
    private final QuestionRepository questionRepository;

    @CheckNotDirector
    @Override
    public Lecture findLecture(Long lectureId) {
        return lectureQueryRepository.findWithDirectorById(lectureId).orElseThrow(() -> new LectureNotFoundException());
    }

    @CheckDirector
    @Override
    public Question findQuestion(Long questionId) {
        return questionRepository.findWithLectureDirector(questionId)
                .orElseThrow(() -> new QuestionNotFoundException());
    }
}
