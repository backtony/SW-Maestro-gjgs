package com.gjgs.gjgs.dummy.lecture;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLecture;
import com.gjgs.gjgs.modules.lecture.embedded.Price;
import com.gjgs.gjgs.modules.lecture.embedded.Terms;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureJdbcRepository;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureRepository;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gjgs.gjgs.modules.lecture.enums.LectureStatus.*;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class LectureDummy {

    private final LectureRepository lectureRepository;
    private final FinishedProductDummy finishedProductDummy;
    private final CurriculumDummy curriculumDummy;
    private final ScheduleDummy scheduleDummy;
    private final FileUrlDummy fileUrlDummy;
    private final CouponDummy couponDummy;
    private final LectureJdbcRepository lectureJdbcRepository;

    private final List<String> boardThumbnailUrl = new ArrayList<String>(Arrays.asList(
            "https://images.unsplash.com/photo-1563692712050-3e68350add0d?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80",
            "https://images.unsplash.com/photo-1589542425426-2460d8243b58?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=750&q=80",
            "https://images.unsplash.com/photo-1589712186148-03ec318289c0?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80",
            "https://images.unsplash.com/photo-1591602998094-be103b2e37ec?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=762&q=80",
            "https://images.unsplash.com/photo-1499083773823-5000fa2b23e4?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=692&q=80",
            "https://images.unsplash.com/photo-1554635917-1fb8f0f63f7c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"
    ));

    private final List<String> climbingThumbnailUrl = new ArrayList<String>(Arrays.asList(
            "https://images.unsplash.com/photo-1601025678763-e8f5835995db?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80",
            "https://images.unsplash.com/photo-1578886141033-b9f066572135?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=668&q=80",
            "https://images.unsplash.com/photo-1595931285307-ec7ab5b0a1f7?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80",
            "https://images.unsplash.com/photo-1548777921-f10a5d3839d9?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=582&q=80",
            "https://images.unsplash.com/photo-1564769662533-4f00a87b4056?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2614&q=80",
            "https://images.unsplash.com/photo-1507034589631-9433cc6bc453?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2319&q=80"
    ));

    private final String cookingThumbnailUrl = "https://images.unsplash.com/photo-1600289031464-74d374b64991?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=969&q=80";

    private final String vocalThumbnailUrl = "https://images.unsplash.com/flagged/photo-1564434369363-696a2e6d96f9?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80";

    private final String rapThumbnailUrl = "https://images.unsplash.com/flagged/photo-1563205764-79ea509b3e95?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1129&q=80";

    private final String baristaThumbnailUrl = "https://images.unsplash.com/photo-1532713107108-dfb5d8d2fc42?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80";

    public List<Lecture> create12LecturesBoard(Zone kangnam, Zone seocho, Category board, Member director) {
        CreateLecture.IntroRequest intro = finishedProductDummy.createFinishedProductList();
        CreateLecture.CurriculumRequest curriculum = curriculumDummy.createCurriculumList();
        CreateLecture.ScheduleRequest schedule = scheduleDummy.createScheduleList();
        List<FileInfoVo> boardFinishedImage = fileUrlDummy.getBoardFinishedProductFileUrlDummy();
        List<FileInfoVo> boardCurriImage = fileUrlDummy.getBoardCurriculumFileUrlDummy();

        List<Lecture> boardList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Lecture lecture = createLectureBoard(kangnam, board, director, i);
            lecture.putIntro(intro, boardFinishedImage);
            lecture.putCurriculums(curriculum.getCurriculumList(), boardCurriImage);
            lecture.putSchedule(schedule);
            boardList.add(lecture);
            couponDummy.createAcceptLectureCoupon(lecture);
        }

        for (int i = 0; i < 6; i++) {
            Lecture lecture = createLectureBoard(seocho, board, director, i);
            lecture.putIntro(intro, boardFinishedImage);
            lecture.putCurriculums(curriculum.getCurriculumList(), boardCurriImage);
            lecture.putSchedule(schedule);
            boardList.add(lecture);
            couponDummy.createAcceptLectureCoupon(lecture);
        }

        List<Lecture> savedBoardList = lectureRepository.saveAll(boardList);
        insertToJdbc(savedBoardList);
        return savedBoardList;
    }

    public List<Lecture> create12LecturesClimbing(Zone kangnam, Zone seocho, Category climbing, Member director) {
        CreateLecture.IntroRequest intro = finishedProductDummy.createFinishedProductList();
        CreateLecture.CurriculumRequest curriculum = curriculumDummy.createCurriculumList();
        CreateLecture.ScheduleRequest schedule = scheduleDummy.createScheduleList();
        List<FileInfoVo> climbingFinishedImage = fileUrlDummy.getClimbingFinishedProductFileUrlDummy();
        List<FileInfoVo> climbingCurriImage = fileUrlDummy.getClimbingCurriculumFileUrlDummy();

        List<Lecture> climbingList = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            Lecture lecture = createLectureClimbing(kangnam, climbing, director, i);
            lecture.putIntro(intro, climbingFinishedImage);
            lecture.putCurriculums(curriculum.getCurriculumList(), climbingCurriImage);
            lecture.putSchedule(schedule);
            climbingList.add(lecture);
            couponDummy.createAcceptLectureCoupon(lecture);
        }

        for(int i = 0; i < 6; i++) {
            Lecture lecture = createLectureClimbing(seocho, climbing, director, i);
            lecture.putIntro(intro, climbingFinishedImage);
            lecture.putCurriculums(curriculum.getCurriculumList(), climbingCurriImage);
            lecture.putSchedule(schedule);
            climbingList.add(lecture);
            couponDummy.createAcceptLectureCoupon(lecture);
        }

        List<Lecture> savedClimbingList = lectureRepository.saveAll(climbingList);
        insertToJdbc(savedClimbingList);
        return savedClimbingList;
    }

    public Lecture createLectureCookingKFood(Zone kangnam, Category cookingKFood, Member director) {
        CreateLecture.IntroRequest intro = finishedProductDummy.createFinishedProductList();
        CreateLecture.CurriculumRequest curriculum = curriculumDummy.createCurriculumList();
        CreateLecture.ScheduleRequest schedule = scheduleDummy.createScheduleList();
        List<FileInfoVo> cookingFinishedImage = fileUrlDummy.getCookingFinishedProductFileUrlDummy();
        List<FileInfoVo> cookingCurriImage = fileUrlDummy.getCookingCurriculumFileUrlDummy();

        Lecture lecture = createLectureFood(kangnam, cookingKFood, director);
        lecture.putIntro(intro, cookingFinishedImage);
        lecture.putCurriculums(curriculum.getCurriculumList(), cookingCurriImage);
        lecture.putSchedule(schedule);
        Lecture savedCooking = lectureRepository.save(lecture);
        insertToJdbc(List.of(savedCooking));
        couponDummy.createAcceptLectureCoupon(lecture);
        return savedCooking;
    }

    public void createTempLecture(Zone konkukUniv, Category vocal, Member director) {
        Lecture vocalTemp = createVocalTempLecture(konkukUniv, vocal, director);
        lectureRepository.save(vocalTemp);
    }

    public void createClosedLecture(Zone hongikUniv, Category rap, Member director) {
        Lecture rapClosed = createRapClosedLecture(hongikUniv, rap, director);
        lectureRepository.save(rapClosed);
    }

    public void createConfirmLecture(Zone seongsoo, Category barista, Member director) {
        CreateLecture.IntroRequest intro = finishedProductDummy.createFinishedProductList();
        CreateLecture.CurriculumRequest curriculum = curriculumDummy.createCurriculumList();
        CreateLecture.ScheduleRequest schedule = scheduleDummy.createScheduleList();
        List<FileInfoVo> baristaFinishedImage = fileUrlDummy.getBaristaFinishedProductFileUrlDummy();
        List<FileInfoVo> baristaCurriImage = fileUrlDummy.getBaristaCurriculumFileUrlDummy();

        Lecture baristaConfirm = createBaristaConfirmLecture(seongsoo, barista, director);
        baristaConfirm.putIntro(intro, baristaFinishedImage);
        baristaConfirm.putCurriculums(curriculum.getCurriculumList(), baristaCurriImage);
        baristaConfirm.putSchedule(schedule);
        Lecture savedCooking = lectureRepository.save(baristaConfirm);
        insertToJdbc(List.of(savedCooking));
    }

    private Lecture createVocalTempLecture(Zone zone, Category category, Member director) {
        return Lecture.builder().zone(zone).category(category).director(director)
                .title("건대에서 진행하는 보컬 클래스!")
                .fullAddress("서울 광진구 화양동 46-64 지하 1층")
                .thumbnailImageFileUrl(vocalThumbnailUrl)
                .thumbnailImageFileName("test")
                .terms(Terms.of())
                .price(Price.builder().priceOne(3000).priceTwo(3000).priceThree(2800).priceFour(2700).regularPrice(2500).build())
                .terms(Terms.of())
                .finished(true).lectureStatus(CREATING)
                .build();
    }

    private Lecture createRapClosedLecture(Zone hongikUniv, Category rap, Member director) {
        return Lecture.builder().zone(hongikUniv).category(rap).director(director)
                .title("홍대에서 진행하는 랩레슨!")
                .fullAddress("서울 마포구 합정동 426-5 3F")
                .thumbnailImageFileUrl(rapThumbnailUrl)
                .thumbnailImageFileName("test")
                .terms(Terms.of())
                .price(Price.builder().priceOne(3000).priceTwo(3000).priceThree(2800).priceFour(2700).regularPrice(2500).build())
                .terms(Terms.of())
                .finished(true).lectureStatus(ACCEPT)
                .build();
    }

    private Lecture createBaristaConfirmLecture(Zone seongsoo, Category barista, Member director) {
        return Lecture.builder().zone(seongsoo).category(barista).director(director)
                .title("성수에서 진행하는 바리스타 원데이 클래스")
               .fullAddress("서울 성동구 성수동2가 301-66 3층")
                .clickCount(0)
                .thumbnailImageFileUrl(baristaThumbnailUrl)
                .thumbnailImageFileName("test")
                .price(Price.builder().priceOne(3000).priceTwo(3000).priceThree(2800).priceFour(2700).regularPrice(2500).build())
                .mainText("성수동에서 진행하는 바리스타 원데이 클래스! 커피 만들어 먹어봅시다!").finished(true)
                .terms(Terms.of())
                .lectureStatus(CONFIRM).build();
    }

    private Lecture createLectureBoard(Zone zone, Category category, Member director, int i) {
        return Lecture.builder().zone(zone).category(category).director(director)
                .title("강남에서 진행하는 보드 클래스!")
                .fullAddress("서울 강남구 테헤란로7길 21 국립어린이청소년도서관")
                .clickCount(0)
                .thumbnailImageFileUrl(boardThumbnailUrl.get(i))
                .thumbnailImageFileName("test")
                .price(Price.builder().priceOne(3000).priceTwo(3000).priceThree(2800).priceFour(2700).regularPrice(2500).build())
                .mainText("강남에서 진행하는 보드 클래스 입니다! 서초 혹은 강남에서 진행 가능합니다.").finished(false)
                .terms(Terms.of())
                .lectureStatus(ACCEPT).build();
    }

    private Lecture createLectureClimbing(Zone zone, Category category, Member director, int i) {
        return Lecture.builder().zone(zone).category(category).director(director)
                .title("강남에서 진행하는 클라이밍 클래스!")
                .fullAddress("서울 강남구 테헤란로7길 21 국립어린이청소년도서관")
                .clickCount(0)
                .thumbnailImageFileUrl(climbingThumbnailUrl.get(i))
                .thumbnailImageFileName("test")
                .price(Price.builder().priceOne(3000).priceTwo(3000).priceThree(2800).priceFour(2700).regularPrice(2500).build())
                .mainText("강남에서 진행하는 클라이밍 클래스 입니다! 서초 혹은 강남에서 진행 가능합니다.").finished(false)
                .terms(Terms.of())
                .lectureStatus(ACCEPT).build();
    }

    private Lecture createLectureFood(Zone zone, Category category, Member director) {
        return Lecture.builder().zone(zone).category(category).director(director)
                .title("강남에서 진행하는 푸드 클래스!")
                .fullAddress("서울 강남구 테헤란로7길 21 국립어린이청소년도서관")
                .clickCount(0)
                .thumbnailImageFileUrl(cookingThumbnailUrl)
                .thumbnailImageFileName("test")
                .price(Price.builder().priceOne(3000).priceTwo(3000).priceThree(2800).priceFour(2700).regularPrice(2500).build())
                .mainText("강남에서 진행하는 푸드 클래스 입니다! 서초 혹은 강남에서 진행 가능합니다.").finished(false)
                .terms(Terms.of())
                .lectureStatus(ACCEPT).build();
    }

    public void insertToJdbc(List<Lecture> lectureList) {
        lectureList.forEach(lecture -> {
            lectureJdbcRepository.insertFinishedProduct(lecture);
            lectureJdbcRepository.insertCurriculum(lecture);
            lectureJdbcRepository.insertSchedule(lecture);
        });
    }
}
