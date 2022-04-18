package com.gjgs.gjgs.modules.lecture.repositories.lecture;

import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ElasticsearchLectureQueryRepository {

    Page<LectureSearchResponse> getLectures(Pageable pageable, LectureSearchCondition cond);
}
