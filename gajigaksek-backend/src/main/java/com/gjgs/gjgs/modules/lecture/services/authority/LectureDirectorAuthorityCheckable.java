package com.gjgs.gjgs.modules.lecture.services.authority;

import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.question.entity.Question;
import com.gjgs.gjgs.modules.utils.validators.authority.AuthorityCheckable;

public interface LectureDirectorAuthorityCheckable extends AuthorityCheckable<Lecture> {

    Lecture findLecture(Long lectureId);

    Question findQuestion(Long questionId);
}
