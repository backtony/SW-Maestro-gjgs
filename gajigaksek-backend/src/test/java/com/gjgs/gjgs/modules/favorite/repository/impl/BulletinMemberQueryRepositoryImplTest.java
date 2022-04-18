package com.gjgs.gjgs.modules.favorite.repository.impl;

import com.gjgs.gjgs.config.repository.SetUpLectureTeamBulletinRepository;
import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import com.gjgs.gjgs.modules.bulletin.enums.Age;
import com.gjgs.gjgs.modules.dummy.BulletinDummy;
import com.gjgs.gjgs.modules.dummy.LectureDummy;
import com.gjgs.gjgs.modules.dummy.TeamDummy;
import com.gjgs.gjgs.modules.favorite.dto.FavoriteBulletinDto;
import com.gjgs.gjgs.modules.favorite.entity.BulletinMember;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.BulletinMemberQueryRepository;
import com.gjgs.gjgs.modules.favorite.repository.interfaces.BulletinMemberRepository;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.team.entity.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BulletinMemberQueryRepositoryImplTest extends SetUpLectureTeamBulletinRepository {

    @Autowired BulletinMemberQueryRepository bulletinMemberQueryRepository;
    @Autowired BulletinMemberRepository bulletinMemberRepository;

    Member member;
    BulletinMember bulletinMember;

    @BeforeEach
    void setup(){
        member = anotherMembers.get(0);
    }

    @AfterEach
    void tearDown() {
        bulletinMemberRepository.deleteAll();
    }

    @DisplayName("내가 찜한 게시글 반환정보에 맞게 모두 조회하기")
    @Test
    void find_bulletins_by_username() throws Exception {

        //given
        Team team2 = teamRepository.save(TeamDummy.createUniqueTeam(2, leader, zone));
        Lecture lecture2 = lectureRepository.save(LectureDummy.createDataJpaTestLecture(zone, category, director));
        Bulletin bulletin2 = bulletinRepository.save(BulletinDummy.createBulletin(team2, lecture2, "lecture2", Age.FORTY));

        BulletinMember bulletinMember1 = bulletinMemberRepository.save(BulletinMember.of(bulletin, member));
        BulletinMember bulletinMember2 = bulletinMemberRepository.save(BulletinMember.of(bulletin2, member));

        flushAndClear();

        // when
        List<FavoriteBulletinDto> dtos
                = bulletinMemberQueryRepository.findBulletinMemberDtoByUsername(member.getUsername());

        FavoriteBulletinDto dto1 = dtos.get(0);
        FavoriteBulletinDto dto2 = dtos.get(1);

        // then
        assertAll(
                () -> assertEquals(2, dtos.size()),

                () -> assertEquals(bulletin.getId(), dto1.getBulletinId()),
                () -> assertEquals(bulletinMember1.getId(), dto1.getBulletinMemberId()),
                () -> assertEquals(lecture.getThumbnailImageFileUrl(), dto1.getThumbnailImageFileUrl()),
                () -> assertEquals(zone.getId(), dto1.getZoneId()),
                () -> assertEquals(bulletin.getTitle(), dto1.getTitle()),
                () -> assertEquals(bulletin.getAge(), dto1.getAge()),
                () -> assertEquals(bulletin.getTimeType(), dto1.getTimeType()),
                () -> assertEquals(team.getCurrentMemberCount(), dto1.getCurrentPeople()),

                () -> assertEquals(bulletin2.getId(), dto2.getBulletinId()),
                () -> assertEquals(bulletinMember2.getId(), dto2.getBulletinMemberId()),
                () -> assertEquals(lecture2.getThumbnailImageFileUrl(), dto2.getThumbnailImageFileUrl()),
                () -> assertEquals(zone.getId(), dto2.getZoneId()),
                () -> assertEquals(bulletin2.getTitle(), dto2.getTitle()),
                () -> assertEquals(bulletin2.getAge(), dto2.getAge()),
                () -> assertEquals(bulletin2.getTimeType(), dto2.getTimeType()),
                () -> assertEquals(team2.getCurrentMemberCount(), dto2.getCurrentPeople())
        );
    }

    @DisplayName("게시글 id, 게시글 상태, memberId로 BulletinMember 존재 여부 확인")
    @Test
    void exists_by_bulletinId_and_bulletinStatus_and_memberId() throws Exception{
        //given
        bulletinMember = bulletinMemberRepository.save(BulletinMember.of(bulletin, member));
        flushAndClear();

        //when then
        assertTrue(bulletinMemberQueryRepository
                .existsByBulletinIdAndBulletinStatusAndMemberId(bulletin.getId(),true,member.getId()));
    }

    @DisplayName("bulletin Id와 username으로 BulletinMemberId 찾기")
    @Test
    void findId_by_bulletinId_and_username() throws Exception{
        //given
        bulletinMember = bulletinMemberRepository.save(BulletinMember.of(bulletin, member));
        flushAndClear();

        // when then
        assertEquals(bulletinMember.getId(),
                bulletinMemberQueryRepository.findIdByBulletinIdAndUsername(bulletin.getId(), member.getUsername()).get());
    }

    @DisplayName("username으로 좋아요 표시 한 모집글 찾기")
    @Test
    void find_favoriteBulletinIdList_by_username() throws Exception {

        // given
        Member favoriteMember = getMember();
        flushAndClear();

        // when
        List<Long> favoriteBulletinIdList = bulletinMemberQueryRepository.findFavoriteBulletinIdListByUsername(favoriteMember.getUsername());

        // then
        assertEquals(favoriteBulletinIdList.size(), 2);
    }

    private Member getMember() {
        Member favoriteMember = anotherMembers.get(0);
        Team team2 = teamRepository.save(TeamDummy.createTeam(leader, zone));
        Bulletin bulletin2 = bulletinRepository.save(BulletinDummy.createBulletin(team2, lecture));
        bulletinMemberRepository.save(BulletinMember.of(bulletin, favoriteMember));
        bulletinMemberRepository.save(BulletinMember.of(bulletin2, favoriteMember));
        return favoriteMember;
    }

    @DisplayName("teamId로 bulletinId 찾기")
    @Test
    void find_id_by_teamId() throws Exception {

        flushAndClear();

        // when
        Long bulletinId = bulletinRepository.findIdByTeamId(team.getId()).orElseThrow();

        // then
        assertEquals(bulletin.getId(), bulletinId);
    }
}
