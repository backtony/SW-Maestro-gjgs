package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.lecture.dtos.create.PutLectureResponse;

import java.util.Set;

public class PutLectureResponseDummy {

    // 이미지 이름은 총 5개 있다고 가정
    public static PutLectureResponse create() {
        return PutLectureResponse.builder()
                .lectureId(1L).categoryId(2L).zoneId(2L).title("test").address("test")
                .thumbnailImageFileName("test").thumbnailImageFileUrl("test")
                .minParticipants(1).maxParticipants(10)
                .mainText("test").lectureStatus("test")
                .finishedProductList(Set.of(
                        PutLectureResponse.FinishedProductResponse.builder().finishedProductId(1L).finishedProductImageName("test").build(),
                        PutLectureResponse.FinishedProductResponse.builder().finishedProductId(2L).finishedProductImageName("test").build()
                ))
                .curriculumList(Set.of(
                        PutLectureResponse.CurriculumResponse.builder().curriculumId(1L).curriculumImageName("test").build(),
                        PutLectureResponse.CurriculumResponse.builder().curriculumId(2L).curriculumImageName("test").build()
                ))
                .build();
    }
}
