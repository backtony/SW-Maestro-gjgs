package com.gjgs.gjgs.dummy.lecture;

import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class FinishedProductDummy {

    public CreateLecture.IntroRequest createFinishedProductList() {
        return CreateLecture.IntroRequest.builder().createLectureStep(CreateLectureStep.INTRO)
                .mainText("소개글 입니다.")
                .finishedProductInfoList(List.of(
                        CreateLecture.FinishedProductInfoDto.builder().order(2).text("2번째 완성작").build(),
                        CreateLecture.FinishedProductInfoDto.builder().order(3).text("3번째 완성작").build(),
                        CreateLecture.FinishedProductInfoDto.builder().order(1).text("1번째 완성작").build()
                ))
                .build();
    }
}
