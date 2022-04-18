package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.embedded.Terms;
import com.gjgs.gjgs.modules.lecture.entity.Curriculum;
import com.gjgs.gjgs.modules.lecture.entity.FinishedProduct;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.zone.entity.Zone;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.*;
import static com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus.*;
import static java.time.LocalDate.now;
import static java.time.LocalTime.of;

public class LectureDummy {

    public static Lecture createLecture(int i) {
        return Lecture.builder()
                .title("테스트" + String.valueOf(i))
                .favoriteCount(0)
                .price(Price.builder()
                        .priceOne(50000)
                        .priceTwo(40000)
                        .priceThree(30000)
                        .priceFour(20000)
                        .build())
                .finished(false).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileUrl("test")
                .build();
    }

    public static Lecture createLectureWithIdDirector(int i) {
        return Lecture.builder()
                .id((long) i)
                .director(Member.builder().authority(Authority.ROLE_DIRECTOR)
                        .username("testdirector").id(176L)
                        .build())
                .title("테스트" + String.valueOf(i))
                .favoriteCount(0)
                .price(Price.builder()
                        .priceOne(50000)
                        .priceTwo(40000)
                        .priceThree(30000)
                        .priceFour(20000)
                        .build())
                .finished(false).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileUrl("test")
                .build();
    }

    public static Lecture createLecture(int i, Member director) {
        Lecture lecture = Lecture.builder().id((long) i)
                .title("테스트" + String.valueOf(i))
                .director(director)
                .favoriteCount(0)
                .price(Price.builder()
                        .priceOne(50000)
                        .priceTwo(40000)
                        .priceThree(30000)
                        .priceFour(20000)
                        .build())
                .finished(false).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileName("test")
                .thumbnailImageFileUrl("test")
                .minParticipants(1).maxParticipants(6)
                .build();
        lecture.addSchedule(Schedule.builder().currentParticipants(0)
                .lecture(lecture).id(1L).scheduleStatus(RECRUIT).build());
        lecture.addSchedule(Schedule.builder().currentParticipants(0)
                .lecture(lecture).id(2L).scheduleStatus(END).build());
        return lecture;
    }

    public static Lecture createHaveTwoScheduleThreeCurriculumFourFinishedProduct(int i, Member director) {
        return Lecture.builder()
                .id(30L)
                .title("테스트" + String.valueOf(i))
                .director(director)
                .favoriteCount(0)
                .price(Price.builder()
                        .priceOne(50000)
                        .priceTwo(40000)
                        .priceThree(30000)
                        .priceFour(20000)
                        .build())
                .thumbnailImageFileName("test")
                .thumbnailImageFileUrl("test")
                .finished(true).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .lectureStatus(CREATING)
                .scheduleList(new ArrayList<>(List.of(
                        Schedule.builder().lectureDate(now()).currentParticipants(0).startTime(of(11, 30)).endTime(of(12, 30)).progressMinutes(60).build(),
                        Schedule.builder().lectureDate(now()).currentParticipants(0).startTime(of(12, 30)).endTime(of(13, 30)).progressMinutes(60).build()
                )))
                .curriculumList(new ArrayList<>(List.of(
                        Curriculum.builder().orders(1).title("dummy").detailText("dummy").curriculumImageName("dummy").curriculumImageUrl("dummy").build(),
                        Curriculum.builder().orders(2).title("dummy").detailText("dummy").curriculumImageName("dummy").curriculumImageUrl("dummy").build(),
                        Curriculum.builder().orders(3).title("dummy").detailText("dummy").curriculumImageName("dummy").curriculumImageUrl("dummy").build()
                )))
                .finishedProductList(new ArrayList<>(List.of(
                        FinishedProduct.builder().orders(1).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build(),
                        FinishedProduct.builder().orders(2).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build(),
                        FinishedProduct.builder().orders(3).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build(),
                        FinishedProduct.builder().orders(4).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build()
                )))
                .build();
    }

    public static Lecture createFinishedLecture(int i, Member director) {
        return Lecture.builder()
                .title("테스트" + String.valueOf(i))
                .director(director)
                .favoriteCount(0)
                .price(Price.builder()
                        .priceOne(50000)
                        .priceTwo(40000)
                        .priceThree(30000)
                        .priceFour(20000)
                        .build())
                .finished(true).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileUrl("test")
                .build();
    }

    public static Lecture createLecture(int i, List<Zone> zones, List<Category> categories) {
        return Lecture.builder()
                .title("테스트" + String.valueOf(i))
                .favoriteCount(0)
                .price(Price.builder()
                        .priceOne(50000)
                        .priceTwo(40000)
                        .priceThree(30000)
                        .priceFour(20000)
                        .build())
                .terms(Terms.builder().termsOne(false).termsTwo(false).termsThree(false).termsFour(false).build())
                .finished(false).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileUrl("test")
                .zone(zones.get((i - 1) % zones.size()))
                .category(categories.get((i - 1) % categories.size()))
                .build();
    }

    public static Lecture createLecture(int i, Member director, List<Zone> zones, List<Category> categories) {
        return Lecture.builder()
                .director(director)
                .title("테스트" + String.valueOf(i))
                .favoriteCount(0)
                .price(Price.builder()
                        .priceOne(50000)
                        .priceTwo(40000)
                        .priceThree(30000)
                        .priceFour(20000)
                        .build())
                .terms(Terms.builder().termsOne(false).termsTwo(false).termsThree(false).termsFour(false).build())
                .finished(false).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileUrl("test")
                .zone(zones.get((i - 1) % zones.size()))
                .category(categories.get((i - 1) % categories.size()))
                .build();
    }

    public static Lecture createDataJpaTestLecture(Zone zone, Category category, Member director) {
        return Lecture.builder()
                .zone(zone).category(category).director(director)
                .minParticipants(1).maxParticipants(10)
                .title("test")
                .clickCount(0).thumbnailImageFileUrl("url").thumbnailImageFileName("name")
                .price(Price.builder().priceOne(3000).priceTwo(3000).priceThree(2800).priceFour(2700).regularPrice(2500).build())
                .fullAddress("서울시 광진구")
                .mainText("test").finished(false)
                .terms(Terms.builder().termsOne(false).termsTwo(false).termsThree(false).termsFour(false).build())
                .lectureStatus(CREATING)
                .build();
    }

    public static Lecture createJdbcTestConfirmLecture(Zone zone, Category category, Member director) {
        return Lecture.builder()
                .zone(zone).category(category).director(director)
                .price(Price.builder().regularPrice(10000).priceOne(10000).priceTwo(10000).priceThree(10000).priceFour(10000).build())
                .terms(Terms.of())
                .fullAddress("testtesttest")
                .thumbnailImageFileUrl("test").thumbnailImageFileName("test")
                .title("test")
                .lectureStatus(CONFIRM)
                .minParticipants(1).maxParticipants(10)
                .scheduleList(new ArrayList<>(List.of(
                        Schedule.builder().scheduleStatus(RECRUIT).lectureDate(now()).currentParticipants(0).startTime(of(11, 30)).endTime(of(12, 30)).progressMinutes(60).build(),
                        Schedule.builder().scheduleStatus(RECRUIT).lectureDate(now()).currentParticipants(0).startTime(of(12, 30)).endTime(of(13, 30)).progressMinutes(60).build()
                )))
                .curriculumList(new ArrayList<>(List.of(
                        Curriculum.builder().orders(1).title("dummy").detailText("dummy").curriculumImageName("dummy").curriculumImageUrl("dummy").build(),
                        Curriculum.builder().orders(2).title("dummy").detailText("dummy").curriculumImageName("dummy").curriculumImageUrl("dummy").build(),
                        Curriculum.builder().orders(3).title("dummy").detailText("dummy").curriculumImageName("dummy").curriculumImageUrl("dummy").build()
                )))
                .finishedProductList(new ArrayList<>(List.of(
                        FinishedProduct.builder().orders(1).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build(),
                        FinishedProduct.builder().orders(2).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build(),
                        FinishedProduct.builder().orders(3).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build(),
                        FinishedProduct.builder().orders(4).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build()
                )))
                .build();
    }

    public static Lecture createDataJpaTestClosedLecture(Zone zone, Category category, Member director) {
        return Lecture.builder()
                .zone(zone).category(category).director(director)
                .title("test")
                .clickCount(0).thumbnailImageFileUrl("url").thumbnailImageFileName("name")
                .price(Price.builder().priceOne(3000).priceTwo(3000).priceThree(2800).priceFour(2700).regularPrice(2500).build())
                .fullAddress("서울시 광진구")
                .mainText("test").finished(true)
                .terms(Terms.builder().termsOne(false).termsTwo(false).termsThree(false).termsFour(false).build())
                .lectureStatus(ACCEPT)
                .build();
    }

    public static Lecture createDataJpaTestLectureWith5Schedule(int i, Zone zone, Category category, Member director) {
        LocalDate now = now();
        return Lecture.builder()
                .director(director).zone(zone).category(category)
                .thumbnailImageFileName("test")
                .thumbnailImageFileUrl("test")
                .title("test" + i).favoriteCount(0)
                .price(Price.builder().regularPrice(i * 10000).priceOne(i * 10000).priceTwo(i * 10000).priceThree(i * 10000).priceFour(i * 10000).build())
                .terms(Terms.of())
                .lectureStatus(ACCEPT)
                .finished(false)
                .mainText("소개글" + i)
                .clickCount(0)
                .fullAddress("test")
                .minParticipants(1)
                .maxParticipants(10)
                .scheduleList(List.of(
                        Schedule.builder().scheduleStatus(RECRUIT).lectureDate(now).currentParticipants(3).startTime(of(18, 0)).endTime(of(20, 00)).progressMinutes(120).build(),
                        Schedule.builder().scheduleStatus(HOLD).lectureDate(now.plusDays(1)).currentParticipants(2).startTime(of(18, 0)).endTime(of(20, 00)).progressMinutes(120).build(),
                        Schedule.builder().scheduleStatus(CLOSE).lectureDate(now.plusDays(2)).currentParticipants(1).startTime(of(18, 0)).endTime(of(20, 00)).progressMinutes(120).build(),
                        Schedule.builder().scheduleStatus(HOLD).lectureDate(now.minusDays(1)).currentParticipants(4).startTime(of(18, 0)).endTime(of(20, 00)).progressMinutes(120).build(),
                        Schedule.builder().scheduleStatus(END).lectureDate(now.minusDays(2)).currentParticipants(5).startTime(of(18, 0)).endTime(of(20, 00)).progressMinutes(120).build()
                )).build();
    }

    public static Lecture createLectureWithZoneCategory(Member director, Zone zone, Category category) {
        return Lecture.builder()
                .zone(zone).category(category).director(director)
                .title("test")
                .clickCount(0).thumbnailImageFileUrl("url").thumbnailImageFileName("name")
                .price(Price.builder().priceOne(0).priceTwo(0).priceThree(0).priceFour(0).regularPrice(0).build())
                .fullAddress("서울시 광진구")
                .mainText("test").finished(true)
                .terms(Terms.builder().termsOne(false).termsTwo(false).termsThree(false).termsFour(false).build())
                .lectureStatus(CREATING)
                .build();
    }

    public static Lecture createDataJpaTestProgressLecture(Zone zone, Category category, Member director) {
        return Lecture.builder()
                .zone(zone).category(category).director(director)
                .price(Price.builder().regularPrice(10000).priceOne(10000).priceTwo(10000).priceThree(10000).priceFour(10000).build())
                .terms(Terms.of())
                .fullAddress("testtesttest")
                .thumbnailImageFileUrl("test").thumbnailImageFileName("test")
                .title("test")
                .lectureStatus(ACCEPT)
                .scheduleList(List.of(
                        Schedule.builder().scheduleStatus(RECRUIT).lectureDate(now()).currentParticipants(0).startTime(of(11, 30)).endTime(of(12, 30)).progressMinutes(60).build(),
                        Schedule.builder().scheduleStatus(RECRUIT).lectureDate(now()).currentParticipants(0).startTime(of(12, 30)).endTime(of(13, 30)).progressMinutes(60).build()
                ))
                .curriculumList(List.of(
                        Curriculum.builder().orders(1).title("dummy").detailText("dummy").curriculumImageName("dummy").curriculumImageUrl("dummy").build(),
                        Curriculum.builder().orders(2).title("dummy").detailText("dummy").curriculumImageName("dummy").curriculumImageUrl("dummy").build(),
                        Curriculum.builder().orders(3).title("dummy").detailText("dummy").curriculumImageName("dummy").curriculumImageUrl("dummy").build()
                ))
                .finishedProductList(List.of(
                        FinishedProduct.builder().orders(1).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build(),
                        FinishedProduct.builder().orders(2).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build(),
                        FinishedProduct.builder().orders(3).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build(),
                        FinishedProduct.builder().orders(4).text("dummy").finishedProductImageName("dummy").finishedProductImageUrl("dummy").build()
                ))
                .build();
    }

    public static Lecture createLectureWithSixSchedule(int i, Member director) {
        return Lecture.builder().id((long) i)
                .title("테스트" + String.valueOf(i))
                .director(director)
                .favoriteCount(0)
                .price(Price.builder()
                        .priceOne(50000)
                        .priceTwo(40000)
                        .priceThree(30000)
                        .priceFour(20000)
                        .build())
                .finished(false).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileName("test")
                .thumbnailImageFileUrl("test")
                .scheduleList(new ArrayList<>(List.of(
                        Schedule.builder().id(2L).currentParticipants(0).scheduleStatus(RECRUIT).build(),
                        Schedule.builder().id(3L).currentParticipants(0).scheduleStatus(HOLD).build(),
                        Schedule.builder().id(4L).currentParticipants(0).scheduleStatus(CANCEL).build(),
                        Schedule.builder().id(5L).currentParticipants(0).scheduleStatus(CLOSE).build(),
                        Schedule.builder().id(6L).currentParticipants(0).scheduleStatus(END).build(),
                        Schedule.builder().id(7L).currentParticipants(2).scheduleStatus(RECRUIT).build()
                )))
                .build();
    }

    public static Lecture createDataJpaTestAcceptLecture(Zone zone, Category category, Member director) {
        return Lecture.builder()
                .zone(zone).category(category)
                .title("testLecture")
                .director(director)
                .favoriteCount(0)
                .price(Price.builder().priceOne(50000).priceTwo(40000).priceThree(30000).priceFour(20000).build())
                .finished(false).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileName("test")
                .thumbnailImageFileUrl("test")
                .lectureStatus(ACCEPT)
                .terms(Terms.of())
                .scheduleList(List.of(
                        Schedule.builder().lectureDate(now()).currentParticipants(0).startTime(of(12, 0)).endTime(of(14, 0)).progressMinutes(60).scheduleStatus(RECRUIT).build(),
                        Schedule.builder().lectureDate(now().plusDays(1)).currentParticipants(0).startTime(of(12, 0)).endTime(of(14, 0)).progressMinutes(60).scheduleStatus(RECRUIT).build(),
                        Schedule.builder().lectureDate(now().plusDays(2)).currentParticipants(0).startTime(of(12, 0)).endTime(of(14, 0)).progressMinutes(60).scheduleStatus(RECRUIT).build(),
                        Schedule.builder().lectureDate(now().plusDays(3)).currentParticipants(0).startTime(of(12, 0)).endTime(of(14, 0)).progressMinutes(60).scheduleStatus(RECRUIT).build()
                ))
                .finishedProductList(List.of(
                        FinishedProduct.builder().orders(1).text("test").finishedProductImageName("test").finishedProductImageUrl("test").build(),
                        FinishedProduct.builder().orders(2).text("test").finishedProductImageName("test").finishedProductImageUrl("test").build(),
                        FinishedProduct.builder().orders(3).text("test").finishedProductImageName("test").finishedProductImageUrl("test").build(),
                        FinishedProduct.builder().orders(4).text("test").finishedProductImageName("test").finishedProductImageUrl("test").build()
                ))
                .curriculumList(List.of(
                        Curriculum.builder().orders(1).title("test").detailText("detail").curriculumImageName("test").curriculumImageUrl("test").build(),
                        Curriculum.builder().orders(2).title("test").detailText("detail").curriculumImageName("test").curriculumImageUrl("test").build(),
                        Curriculum.builder().orders(3).title("test").detailText("detail").curriculumImageName("test").curriculumImageUrl("test").build(),
                        Curriculum.builder().orders(4).title("test").detailText("detail").curriculumImageName("test").curriculumImageUrl("test").build()
                ))
                .build();
    }

    public static Lecture createLectureWithCoupon(Member director) {
        return Lecture.builder()
                .id(1L)
                .title("testLecture")
                .director(director)
                .favoriteCount(0)
                .price(Price.builder().priceOne(50000).priceTwo(40000).priceThree(30000).priceFour(20000).build())
                .finished(false).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileName("test")
                .thumbnailImageFileUrl("test")
                .lectureStatus(ACCEPT)
                .terms(Terms.of()).build();
    }

    public static Lecture createLectureWithCloseCoupon(Member director) {
        return Lecture.builder()
                .id(1L)
                .title("testLecture")
                .director(director)
                .favoriteCount(0)
                .price(Price.builder().priceOne(50000).priceTwo(40000).priceThree(30000).priceFour(20000).build())
                .finished(false).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileName("test")
                .thumbnailImageFileUrl("test")
                .lectureStatus(ACCEPT)
                .terms(Terms.of()).build();
    }

    public static Lecture createDataJpaTestRejectLecture(Zone zone, Category category, Member director) {
        return Lecture.builder()
                .id(1L)
                .zone(zone)
                .category(category)
                .title("testLecture")
                .director(director)
                .favoriteCount(0)
                .price(Price.builder().priceOne(50000).priceTwo(40000).priceThree(30000).priceFour(20000).build())
                .finished(false).mainText("테스트").maxParticipants(8).minParticipants(2)
                .progressTime(60).clickCount(0).fullAddress("테스트")
                .thumbnailImageFileName("test")
                .thumbnailImageFileUrl("test")
                .lectureStatus(REJECT)
                .terms(Terms.of()).build();
    }
}
