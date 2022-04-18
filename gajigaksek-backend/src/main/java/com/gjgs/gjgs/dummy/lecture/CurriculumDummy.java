package com.gjgs.gjgs.dummy.lecture;

import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep.CURRICULUM;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class CurriculumDummy {

    public CreateLecture.CurriculumRequest createCurriculumList() {
        return CreateLecture.CurriculumRequest.builder()
                .createLectureStep(CURRICULUM)
                .curriculumList(List.of(
                        CreateLecture.CurriculumDto.builder().order(2).title("2번째 커리큘럼").detailText("2번째 커리큘럼입니다.").build(),
                        CreateLecture.CurriculumDto.builder().order(3).title("3번째 커리큘럼").detailText("3번째 커리큘럼입니다.").build(),
                        CreateLecture.CurriculumDto.builder().order(1).title("1번째 커리큘럼").detailText("1번째 커리큘럼입니다.").build()
                ))
                .build();
    }
}
