package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.member.dto.login.TokenDto;
import com.gjgs.gjgs.modules.member.dto.search.MemberSearchResponse;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.enums.Sex;
import com.gjgs.gjgs.modules.notification.dto.MemberFcmDto;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemberDummy {

    public static Member createTestMember() {
        Member member = Member.builder()
                .username("test")
                .authority(Authority.ROLE_USER)
                .nickname("가나다")
                .name("test")
                .phone("01000000000")
                .imageFileUrl("test")
                .profileText("test")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(ZoneDummy.createZone())
                .totalReward(3000)
                .build();


        member.setMemberCategories(Arrays.asList(CategoryDummy.createCategory()));
        return member;
    }

    public static Member createTestMember(Zone zone) {
        Member member = Member.builder()
                .username("test")
                .authority(Authority.ROLE_USER)
                .nickname("가나다")
                .name("test")
                .phone("01000000000")
                .imageFileUrl("test")
                .profileText("test")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .build();


        member.setMemberCategories(Arrays.asList(CategoryDummy.createCategory()));
        return member;
    }

    public static Member createTestUniqueMember(int i) {
        return Member.builder()
                .id((long) i)
                .username("test" + i)
                .authority(Authority.ROLE_USER)
                .nickname("가나다" + i)
                .name("test")
                .phone("0100000000" + i)
                .imageFileUrl("test")
                .profileText("test")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .build();
    }


    public static Member createTestDirectorMember() {
        Member member = Member.builder()
                .id(10L)
                .username("testDirector")
                .authority(Authority.ROLE_DIRECTOR)
                .nickname("director")
                .name("director")
                .phone("01011111111")
                .imageFileUrl("test")
                .profileText("test")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .build();


        member.setMemberCategories(Arrays.asList(CategoryDummy.createCategory()));
        return member;
    }

    public static Member createMemberForTokenGenerate() {
        return Member.builder()
                .id(1L)
                .username("test")
                .authority(Authority.ROLE_USER)
                .nickname("test")
                .phone("01000000000")
                .build();
    }

    public static Member createDirectorMemberForTokenGenerate() {
        return Member.builder()
                .id(1L)
                .username("test")
                .authority(Authority.ROLE_DIRECTOR)
                .nickname("test")
                .phone("01000000000")
                .build();
    }

    public static SignUpRequest createInvalidSignupForm() {
        return SignUpRequest.builder()
                .id(1L)
                .imageFileUrl("testImage")
                .name("가나다라마바사")
                .phone("01000000000123")
                .nickname("t")
                .age(101)
                .sex("ME")
                .zoneId(1L)
                .categoryIdList(new ArrayList<>(Arrays.asList(1L, 2L, 3L)))
                .fcmToken("fcmToken")
                .build();
    }

    public static SignUpRequest createSignupForm() {
        return SignUpRequest.builder()
                .id(1L)
                .imageFileUrl("testImage")
                .name("가나다")
                .phone("01000000000")
                .nickname("test")
                .age(25)
                .sex("M")
                .zoneId(1L)
                .categoryIdList(new ArrayList<>(Arrays.asList(1L, 2L, 3L)))
                .fcmToken("fcmToken")
                .recommendNickname("recommendNickname")
                .build();
    }

    public static SignUpRequest createWrongInitBinderSignupForm() {
        return SignUpRequest.builder()
                .id(1L)
                .imageFileUrl("testImage")
                .name("가나다")
                .phone("01000000000")
                .nickname("가나다")
                .age(25)
                .sex("M")
                .zoneId(1L)
                .categoryIdList(new ArrayList<>(Arrays.asList(1L, 2L, 3L)))
                .fcmToken("fcmToken")
                .recommendNickname("recommendNickname")
                .build();
    }

    public static TokenDto generateToken() {
        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken("TestAccessToken")
                .accessTokenExpiresIn(1000L * 60 * 360)
                .refreshToken("TestRefreshToken")
                .refreshTokenExpiresIn(1000L * 60 * 60 * 24 * 14)
                .build();
    }

    public static Authentication createAuthentication() {
        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails principal = new User("test", "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public static Authentication createDirectorAuthentication() {
        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_DIRECTOR"));
        UserDetails principal = new User("test", "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    public static Authentication createAdminAuthentication() {
        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        UserDetails principal = new User("test", "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    public static List<Member> createLeaders() {
        List<Member> memberList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            memberList.add(
                    Member.builder()
                            .username("test" + i)
                            .nickname("test" + i)
                            .name("test" + i)
                            .phone("0101111222" + i)
                            .imageFileUrl("test")
                            .age(i + 1)
                            .sex(Sex.M)
                            .authority(Authority.ROLE_USER)
                            .build()
            );
        }
        return memberList;
    }

    public static List<Member> createLeaders(List<Zone> zones, List<Category> categories) {
        List<Member> memberList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            memberList.add(
                    createDataJpaTestMember(i, zones.get(i % zones.size()), categories.get(i % categories.size()))
            );
        }
        return memberList;
    }

    // for dataJpaTest
    public static Member createDataJpaTestMember(Zone zone, Category category) {
        Member member = Member.builder()
                .username("mem")
                .authority(Authority.ROLE_USER)
                .nickname("mem")
                .name("mem")
                .phone("01000000000")
                .imageFileUrl("mem")
                .profileText("mem")
                .directorText("mem")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("fcmToken")
                .eventAlarm(true)
                .build();

        member.setMemberCategories(Arrays.asList(category));
        return member;
    }

    public static Member createDataJpaTestMember(int i, Zone zone, Category category) {
        Member member = Member.builder()
                .username("mem"+i)
                .authority(Authority.ROLE_USER)
                .nickname("mem"+i)
                .name("mem"+i)
                .phone(String.valueOf(10000000000L+i))
                .imageFileUrl("mem")
                .profileText("mem")
                .directorText("mem")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("fcmToken")
                .eventAlarm(true)
                .build();

        member.setMemberCategories(Arrays.asList(category));
        return member;
    }

    public static Member createDataJpaTestDirector(Zone zone, Category category) {
        Member member = Member.builder()
                .username("director")
                .authority(Authority.ROLE_DIRECTOR)
                .nickname("director")
                .name("director")
                .phone("01000000001")
                .imageFileUrl("director")
                .profileText("director")
                .directorText("director")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("fcmToken")
                .eventAlarm(true)
                .build();

        member.setMemberCategories(Arrays.asList(category));
        return member;
    }

    public static Member createDataJpaTestDirector(int i, Zone zone, Category category) {
        Member member = Member.builder()
                .username("director" + i)
                .authority(Authority.ROLE_DIRECTOR)
                .nickname("director" + i)
                .name("director" + i)
                .phone("0100000000" + i)
                .imageFileUrl("director")
                .profileText("director")
                .directorText("director")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("fcmToken")
                .eventAlarm(true)
                .build();

        member.setMemberCategories(Arrays.asList(category));
        return member;
    }

    public static List<MemberSearchResponse> createMemberSearchDtoList(int k){
        String nickname = "nickname";
        String phone = "010111122";

        List<MemberSearchResponse> resList = new ArrayList<>();
        for(int i=0;i<k;i++){
            MemberSearchResponse memberSearchResponse = MemberSearchResponse.builder()
                    .createdDate(LocalDateTime.of(2021,10,20,13,30,30))
                    .authority(Authority.ROLE_USER)
                    .nickname(nickname + i)
                    .phone(phone + i)
                    .build();
            resList.add(memberSearchResponse);
        }

        return resList;
    }

    public static MemberFcmDto createMemberFcmDto(Long memberId,String fcmToken){
        return MemberFcmDto.builder()
                .memberId(memberId)
                .fcmToken(fcmToken)
                .build();
    }


}
