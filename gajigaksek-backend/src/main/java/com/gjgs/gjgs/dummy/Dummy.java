package com.gjgs.gjgs.dummy;

import com.gjgs.gjgs.dummy.lecture.CouponDummy;
import com.gjgs.gjgs.dummy.lecture.ElasticsearchLectureDummy;
import com.gjgs.gjgs.dummy.lecture.LectureDummy;
import com.gjgs.gjgs.dummy.lecture.OrderDummy;
import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.category.repositories.CategoryRepository;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.Team;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
@Profile("dev")
public class Dummy implements CommandLineRunner {

    private final EntityManager em;
    private final MemberDummy memberDummy;
    private final LectureDummy lectureDummy;
    private final TeamDummy teamDummy;
    private final BulletinDummy bulletinDummy;
    private final ZoneRepository zoneRepository;
    private final CategoryRepository categoryRepository;
    private final NoticeDummy noticeDummy;
    private final MatchingDummy matchingDummy;
    private final RewardDummy rewardDummy;
    private final OrderDummy orderDummy;
    private final CouponDummy couponDummy;
    private final NotificationDummy notificationDummy;
    private final ElasticsearchLectureDummy elasticsearchLectureDummy;

    private void flushAndClear() {
        em.flush();
        em.clear();
    }

    @Override
    public void run(String... args) throws Exception {

        List<Category> categories = categoryRepository.findAll();
        List<Zone> zones = zoneRepository.findAll();

        Member director = memberDummy.createDirector(1, zones.get(1));
        Member leader = memberDummy.createLeader(1, zones.get(2));
        Member member1 = memberDummy.createMember1(zones.get(3));
        Member member2 = memberDummy.createMember2(zones.get(4));

        Category board = getBoard(categories);
        Category climbing = getClimbing(categories);
        Category cookingKFood = getCookingKFood(categories);

        Zone kangnam = getKangnam(zones);
        Zone seocho = getSeocho(zones);

        List<Lecture> boardList = lectureDummy.create12LecturesBoard(kangnam, seocho, board, director);
        List<Lecture> climbingList = lectureDummy.create12LecturesClimbing(kangnam, seocho, climbing, director);
        Lecture cookingLecture = lectureDummy.createLectureCookingKFood(kangnam, cookingKFood, director);

        Zone konkukUniv = getKonkukUniv(zones);
        Category vocal = getVocal(categories);
        lectureDummy.createTempLecture(konkukUniv, vocal, director);

        Zone hongikUniv = getHongikUniv(zones);
        Category rap = getRap(categories);
        lectureDummy.createClosedLecture(hongikUniv, rap, director);

        Zone seongsoo = getSeongsoo(zones);
        Category barista = getBarista(categories);
        lectureDummy.createConfirmLecture(seongsoo, barista, director);

        // 쿠폰 리워드 멤버
        Member rewardCouponMember = memberDummy.createRewardCouponMember(kangnam);
        couponDummy.createCoupon(rewardCouponMember);
        rewardDummy.createRecommendTypeReward(rewardCouponMember);

        List<Team> allTeams = teamDummy.create25teams(leader, member1, rewardCouponMember, zones, board);
        List<Team> boardTeams = allTeams.subList(0, 12);
        List<Team> climbingTeams = allTeams.subList(12, 24);
        Team cookingTeam = allTeams.get(24);

        bulletinDummy.createBoardBulletins(boardTeams, boardList);
        bulletinDummy.createClimbingBulletins(climbingTeams, climbingList);
        bulletinDummy.createCookingBulletin(cookingTeam, cookingLecture);
        flushAndClear();

        // matching
        Member m1 = memberDummy.createMatchingMember1(kangnam);
        Member m2 = memberDummy.createMatchingMember2(kangnam);
        Member m3 = memberDummy.createMatchingMember3(kangnam);
        Member m4 = memberDummy.createMatchingMember4(kangnam);
        Member m5 = memberDummy.createMatchingMember5(kangnam);
        Member m6 = memberDummy.createMatchingMember6(kangnam);

        matchingDummy.createMatching(m1,kangnam,board,"MON","MORNING",2);
        matchingDummy.createMatching(m2,kangnam,board,"MON","MORNING",3);
        matchingDummy.createMatching(m3,kangnam,board,"MON","MORNING",3);
        matchingDummy.createMatching(m4,kangnam,board,"MON","MORNING",4);
        matchingDummy.createMatching(m5,kangnam,board,"MON","MORNING",4);
        matchingDummy.createMatching(m6,kangnam,board,"MON","MORNING",4);

        // 공지사항
        for(int i=0;i<30;i++){
            noticeDummy.createNoticeForAll(i);
            noticeDummy.createNoticeForDirector(i);
        }

        // 리워드
        Member rewardMember = memberDummy.createRewardMember(zones.get(0));
        for(int i=0;i<30;i++){
            rewardDummy.createRecommendTypeReward(rewardMember);
        }

        // admin 계정
        memberDummy.createAdmin(zones.get(0));

        // 결제 신청 (팀)
        List<Member> teamMembers = List.of(leader, member1, rewardCouponMember);
        orderDummy.createScheduleOrder(cookingTeam, teamMembers, cookingLecture);

        // 모든 쿠폰 발급
        flushAndClear();
        couponDummy.giveCoupon(leader);

        // 알림
        Member notificationMember = memberDummy.createNotificationMember(zones.get(0));
        for(int i=0;i<30;i++){
            notificationDummy.createNotification(notificationMember,i);
        }
        flushAndClear();

        // 저장된 클래스들 Elasticsearch로 옮기기
        elasticsearchLectureDummy.setUpLecture();

        log.info("=======================Insert Dummy Data end.....");
    }

    private Zone getSeocho(List<Zone> zones) {
        return zones.stream().filter(zone -> zone.getId().equals(3L)).findFirst().get();
    }

    private Zone getKangnam(List<Zone> zones) {
        return zones.stream().filter(zone -> zone.getId().equals(2L)).findFirst().get();
    }

    private Zone getKonkukUniv(List<Zone> zones) {
        return zones.stream().filter(zone -> zone.getId().equals(14L)).findFirst().get();
    }

    private Zone getHongikUniv(List<Zone> zones) {
        return zones.stream().filter(zone -> zone.getId().equals(4L)).findFirst().get();
    }

    private Zone getSeongsoo(List<Zone> zones) {
        return zones.stream().filter(zone -> zone.getId().equals(16L)).findFirst().get();
    }

    private Category getCookingKFood(List<Category> categories) {
        return categories.stream().filter(category -> category.getId().equals(12L)).findFirst().get();
    }

    private Category getClimbing(List<Category> categories) {
        return categories.stream().filter(category -> category.getId().equals(3L)).findFirst().get();
    }

    private Category getBoard(List<Category> categories) {
        return categories.stream().filter(category -> category.getId().equals(2L)).findFirst().get();
    }

    private Category getVocal(List<Category> categories) {
        return categories.stream().filter(category -> category.getId().equals(64L)).findFirst().get();
    }

    private Category getRap(List<Category> categories) {
        return categories.stream().filter(category -> category.getId().equals(71L)).findFirst().get();
    }

    private Category getBarista(List<Category> categories) {
        return categories.stream().filter(category -> category.getId().equals(18L)).findFirst().get();
    }
}