package com.gjgs.gjgs.modules.member.repository;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.coupon.entity.Coupon;
import com.gjgs.gjgs.modules.coupon.repositories.CouponRepository;
import com.gjgs.gjgs.modules.coupon.repositories.MemberCouponRepository;
import com.gjgs.gjgs.modules.dummy.BulletinDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.LectureMemberRepository;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.matching.dto.MemberFcmIncludeNicknameDto;
import com.gjgs.gjgs.modules.member.dto.mypage.AlarmStatusResponse;
import com.gjgs.gjgs.modules.member.dto.mypage.MyBulletinDto;
import com.gjgs.gjgs.modules.member.dto.mypage.TotalRewardDto;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchCondition;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.entity.MemberCategory;
import com.gjgs.gjgs.modules.member.entity.MemberCoupon;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberCategoryRepository;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberQueryRepository;
import com.gjgs.gjgs.modules.notification.dto.MemberFcmDto;
import com.gjgs.gjgs.modules.notification.enums.TargetType;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

class MemberQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired MemberQueryRepository memberQueryRepository;
    @Autowired LectureMemberRepository lectureMemberRepository;
    @Autowired MemberCategoryRepository memberCategoryRepository;
    @Autowired CouponRepository couponRepository;
    @Autowired MemberCouponRepository memberCouponRepository;


    @AfterEach
    void teardown(){
        couponRepository.deleteAll();
        memberCategoryRepository.deleteAll();
        lectureMemberRepository.deleteAll();
    }


    @DisplayName("자신의 게시글 가져오기 / 리더의 경우")
    @Test
    void find_my_bulletins_by_username() throws Exception {

        //given
        // 지역,회원, 클래스, 팀, 게시글 생성
        Lecture lecture2 = lectureRepository.save(LectureDummy.createDataJpaTestLecture(zone, category, director));
        Lecture lecture3 = lectureRepository.save(LectureDummy.createDataJpaTestLecture(zone, category, director));

        Team team2 = teamRepository.save(TeamDummy.createUniqueTeam(2, leader, zone));
        Team team3 = teamRepository.save(TeamDummy.createUniqueTeam(3, director, zone));

        Bulletin bulletin2 = bulletinRepository.save(BulletinDummy.createBulletin(team2, lecture2));
        Bulletin savedBulletin3 = bulletinRepository.save(BulletinDummy.createBulletin(team3, lecture2));
        flushAndClear();

        // when
        List<MyBulletinDto> myBulletinDtoList = memberQueryRepository.findMyBulletinsByUsername(leader.getUsername());

        // then
        assertAll(
                () -> assertEquals(2, myBulletinDtoList.size()),
                () -> assertEquals(bulletin.getId(), myBulletinDtoList.get(0).getBulletinId()),
                () -> assertEquals(lecture.getThumbnailImageFileUrl(), myBulletinDtoList.get(0).getThumbnailImageFileUrl()),
                () -> assertEquals(lecture.getZone().getId(), myBulletinDtoList.get(0).getZoneId()),
                () -> assertEquals(bulletin.getTitle(), myBulletinDtoList.get(0).getTitle()),
                () -> assertEquals(bulletin.getAge(), myBulletinDtoList.get(0).getAge()),
                () -> assertEquals(bulletin.getTimeType(), myBulletinDtoList.get(0).getTimeType()),
                () -> assertEquals(team.getCurrentMemberCount(), myBulletinDtoList.get(0).getCurrentPeople()),
                () -> assertEquals(team.getMaxPeople(), myBulletinDtoList.get(0).getMaxPeople()),
                () -> assertEquals(bulletin.isStatus(), myBulletinDtoList.get(0).isStatus()),

                () -> assertEquals(bulletin2.getId(), myBulletinDtoList.get(1).getBulletinId()),
                () -> assertEquals(lecture2.getThumbnailImageFileUrl(), myBulletinDtoList.get(1).getThumbnailImageFileUrl()),
                () -> assertEquals(lecture2.getZone().getId(), myBulletinDtoList.get(1).getZoneId()),
                () -> assertEquals(bulletin2.getTitle(), myBulletinDtoList.get(1).getTitle()),
                () -> assertEquals(bulletin2.getAge(), myBulletinDtoList.get(1).getAge()),
                () -> assertEquals(bulletin2.getTimeType(), myBulletinDtoList.get(1).getTimeType()),
                () -> assertEquals(team2.getCurrentMemberCount(), myBulletinDtoList.get(1).getCurrentPeople()),
                () -> assertEquals(team2.getMaxPeople(), myBulletinDtoList.get(1).getMaxPeople()),
                () -> assertEquals(bulletin2.isStatus(), myBulletinDtoList.get(1).isStatus())
        );
    }

    @DisplayName("member 찾을 때 zone과 favoriteCategory의 category까지 땡겨오기")
    @Test
    void find_with_zone_and_favoriteCategory_and_category_by_id() throws Exception {

        //given
        Member member1 = anotherMembers.get(0);
        MemberCategory memberCategory = MemberCategory.of(member1, category);
        MemberCategory savedMemberCategory = memberCategoryRepository.save(memberCategory);
        flushAndClear();

        //when
        Member m1 = memberQueryRepository.findWithZoneAndFavoriteCategoryAndCategoryById(member1.getId()).get();

        //then
        assertAll(
                () -> assertEquals(zone.getId(), m1.getZone().getId()),
                () -> assertEquals(zone.getMainZone(), m1.getZone().getMainZone()),
                () -> assertEquals(zone.getSubZone(), m1.getZone().getSubZone()),

                () -> assertEquals(savedMemberCategory.getId(), m1.getMemberCategories().get(0).getId()),
                () -> assertEquals(1, m1.getMemberCategories().size()),

                () -> assertEquals(category.getId(), m1.getMemberCategories().get(0).getCategory().getId()),
                () -> assertEquals(category.getMainCategory(), m1.getMemberCategories().get(0).getCategory().getMainCategory()),
                () -> assertEquals(category.getSubCategory(), m1.getMemberCategories().get(0).getCategory().getSubCategory()),

                () -> assertEquals(member1.getUsername(), m1.getMemberCategories().get(0).getMember().getUsername())
        );
    }

    @DisplayName("회원이 보유하고 있는 쿠폰 정보 가져오기")
    @Test
    void find_with_coupon_by_username() throws Exception {

        // given
        Member member1 = anotherMembers.get(0);
        Coupon coupon = couponRepository.save(createCoupon());
        memberCouponRepository.save(MemberCoupon.of(member1, coupon.getDiscountPrice(), coupon.getSerialNumber()));
        flushAndClear();

        // when
        Member findMember = memberQueryRepository.findWithCouponByUsername(member1.getUsername()).orElseThrow();

        // then
        assertEquals(findMember.getCoupons().size(), 1);
    }

    private Coupon createCoupon() {
        return Coupon.builder()
                .title("test").receivePeople(0).issueDate(now()).closeDate(now().plusDays(30))
                .remainCount(10).chargeCount(10).serialNumber(UUID.randomUUID().toString()).discountPrice(1000).available(true)
                .build();
    }

    @DisplayName("condition과 paging에 따른 멤버 검색")
    @Test
    void find_paging_member_by_condition() throws Exception{
        //given

        PageRequest pageRequest = PageRequest.of(0, 20, Sort.Direction.DESC, "createdDate");
        MemberSearchCondition condition = createMemberSearchCondition();
        flushAndClear();

        //when
        Page<MemberSearchResponse> dtos = memberQueryRepository.findPagingMemberByCondition(pageRequest, condition);

        //then
        assertAll(
                () -> assertEquals(0,dtos.getPageable().getOffset()),
                () -> assertEquals(20,dtos.getPageable().getPageSize()),
                () -> assertEquals(pageRequest.getSort(),dtos.getPageable().getSort()),
                () -> assertEquals(4,dtos.getTotalElements()),
                () -> assertEquals(1,dtos.getTotalPages()),
                () -> assertEquals(Authority.ROLE_USER,dtos.getContent().get(0).getAuthority()),
                () -> assertEquals(leader.getNickname(),dtos.getContent().get(0).getNickname()),
                () -> assertNotNull(dtos.getContent().get(0).getCreatedDate()),
                () -> assertEquals(leader.getPhone(),dtos.getContent().get(0).getPhone())
        );
    }

    private MemberSearchCondition createMemberSearchCondition() {
        return MemberSearchCondition.builder()
                .nickname("mem")
                .authority(Authority.ROLE_USER)
                .createdDateStart(LocalDate.now().minusDays(1))
                .createdDateEnd(LocalDate.now())
                .build();
    }

    @DisplayName("username으로 멤버의 id와 fcmtoken 추출하여 memberFcmDto로 받기")
    @Test
    void find_memberId_and_fcmToken_by_username() throws Exception{

        //given
        Member member1 = anotherMembers.get(0);
        flushAndClear();

        //when
        MemberFcmIncludeNicknameDto dto = memberQueryRepository.findMemberFcmDtoByUsername(member1.getUsername()).get();

        //then
        assertEquals(member1.getId(),dto.getMemberId());
        assertEquals(member1.getFcmToken(),dto.getFcmToken());
        assertEquals(member1.getUsername(),dto.getNickname());
    }


    @DisplayName("username으로 totalRewardDto 찾기")
    @Test
    void find_totalRewardDto_by_username() throws Exception{
        //given
        Member member1 = anotherMembers.get(0);
        flushAndClear();

        //when
        TotalRewardDto totalRewardDto = memberQueryRepository.findTotalRewardDtoByUsername(member1.getUsername()).get();

        //then
        assertEquals(member1.getId(),totalRewardDto.getMemberId());
        assertEquals(member1.getTotalReward(),totalRewardDto.getTotalReward());
    }

    @DisplayName("SELECT 타켓 타입과 memberIdList로 memberFcmDto 찾기")
    @Test
    void find_memberFcmDto_by_targetType_and_memberIdList_SELECT() throws Exception{
        //given
        Member m1 = anotherMembers.get(0);
        Member m2 = anotherMembers.get(1);
        Member m3 = anotherMembers.get(2);
        flushAndClear();

        //when
        List<MemberFcmDto> dto = memberQueryRepository
                .findMemberFcmDtoByTargetTypeAndMemberIdListAndMemberEventAlarm(TargetType.SELECT, List.of(m1.getId(), m2.getId(), m3.getId()),true);

        //then
        assertEquals(3,dto.size());
        assertEquals(m1.getId(),dto.get(0).getMemberId());

    }

    @DisplayName("ALL 타켓 타입과 memberIdList로 memberFcmDto 찾기")
    @Test
    void find_memberFcmDto_by_targetType_and_memberIdList_ALL() throws Exception{
        //given
        Member m1 = anotherMembers.get(0);
        Member m2 = anotherMembers.get(1);
        Member m3 = anotherMembers.get(2);
        flushAndClear();

        //when
        List<MemberFcmDto> dto = memberQueryRepository
                .findMemberFcmDtoByTargetTypeAndMemberIdListAndMemberEventAlarm(TargetType.ALL, List.of(m1.getId(), m2.getId(), m3.getId()),true);
        //then
        assertEquals(5,dto.size());

    }


    @DisplayName("현재 사용자의 알림 수신 여부 확인")
    @Test
    void find_alarm_status_by_username() throws Exception{
        //given
        Member m1 = anotherMembers.get(0);
        flushAndClear();

        //when
        AlarmStatusResponse alarmStatusResponse = memberQueryRepository.findAlarmStatusByUsername(m1.getUsername());

        //then
        assertTrue(alarmStatusResponse.isEventAlarm());
    }
}
