package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.favorite.dto.LectureMemberDto;
import com.gjgs.gjgs.modules.favorite.dto.LectureTeamDto;
import com.gjgs.gjgs.modules.lecture.dtos.LectureDetailResponse;
import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus;

import java.time.LocalDate;
import java.util.Set;

public class LectureDtoDummy {

    public static LectureDetailResponse createLectureDetailResponse() {

        return LectureDetailResponse.builder()
                .lectureId(1L)
                .thumbnailImageUrl("url")
                .lectureTitle("title")
                .zoneId(1L)
                .categoryId(1L)
                .progressTime(120)
                .priceOne(50000)
                .priceTwo(49000)
                .priceThree(48000)
                .priceFour(47000)
                .regularPrice(50000)
                .mainText("text")
                .minParticipants(1)
                .maxParticipants(6)
                .myFavorite(false)
                .directorResponse(
                        LectureDetailResponse.DirectorResponse.builder().directorId(1L).directorProfileText("test").directorProfileImageUrl("url").build()
                )
                .curriculumResponseList(Set.of(
                        LectureDetailResponse.CurriculumResponse.builder()
                                .curriculumId(1L)
                                .order(1)
                                .title("title")
                                .detailText("detail")
                                .curriculumImageUrl("url1")
                                .build()
                ))
                .finishedProductResponseList(Set.of(
                        LectureDetailResponse.FinishedProductResponse.builder()
                                .finishedProductId(1L)
                                .order(1)
                                .finishedProductImageUrl("url1")
                                .text("test")
                                .build()
                ))
                .scheduleResponseList(Set.of(
                        LectureDetailResponse.ScheduleResponse.builder()
                                .scheduleId(1L)
                                .lectureDate(LocalDate.of(2021,8,8))
                                .currentParticipants(0)
                                .startHour(15)
                                .startMinute(30)
                                .endHour(18)
                                .endMinute(30)
                                .progressMinutes(180)
                                .scheduleStatus(ScheduleStatus.HOLD.name())
                                .build()
                ))
                .build();
    }

    public static LectureMemberDto createLectureMemberDto() {
        return LectureMemberDto.builder()
                .lectureMemberId(1L)
                .lectureId(1L)
                .thumbnailImageFileUrl("test")
                .zoneId(1L)
                .title("test")
                .price(Price.builder()
                        .priceOne(50000)
                        .priceTwo(40000)
                        .priceThree(30000)
                        .priceFour(20000)
                        .build())
                .build();
    }

    public static LectureTeamDto createLectureTeamDto() {
        return LectureTeamDto.builder()
                .lectureTeamId(1L)
                .lectureId(1L)
                .thumbnailImageFileUrl("test")
                .zoneId(1L)
                .title("test")
                .price(Price.builder()
                        .priceOne(50000)
                        .priceTwo(40000)
                        .priceThree(30000)
                        .priceFour(20000)
                        .build())
                .build();
    }
}
