package com.gjgs.gjgs.dummy;

import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.enums.Sex;
import com.gjgs.gjgs.modules.member.repository.interfaces.MemberRepository;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class MemberDummy {

    private final MemberRepository memberRepository;

    public Member createDirector(int i, Zone zone) {
        Member director = Member.builder()
                .username("director" + i)
                .authority(Authority.ROLE_DIRECTOR)
                .nickname("디렉터" + i)
                .name("김민수")
                .phone("11300120000" + i)
                .imageFileUrl("https://images.unsplash.com/photo-1627660692856-bc032e058cc2?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80")
                .profileText("안녕하세요! 문의사항이 있으시면 언제든 채팅 및 문의를 남겨주세요!")
                .directorText("안녕하세요! 문의사항이 있으시면 언제든 채팅 및 문의를 남겨주세요!")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .build();
        Member savedDirector = memberRepository.save(director);
        return savedDirector;
    }

    public Member createLeader(int i, Zone zone) {
        Member leader = Member.builder()
                .username("leader" + i)
                .authority(Authority.ROLE_USER)
                .nickname("고팀장" + i)
                .name("고범석")
                .phone("01100121000"+i)
                .imageFileUrl("https://images.unsplash.com/photo-1627772518466-30d49be6263e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80")
                .profileText("안녕하세요. 고팀장입니다. 잘부탁해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .build();
        Member savedLeader = memberRepository.save(leader);
        return savedLeader;
    }

    public Member createMember1(Zone zone) {
        Member member =  Member.builder()
                .username("member1")
                .authority(Authority.ROLE_USER)
                .nickname("김기와니")
                .name("김기완")
                .phone("01100120001")
                .imageFileUrl("https://images.unsplash.com/photo-1627446775354-9025108586c0?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1300&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member createMember2(Zone zone) {
        Member member = Member.builder()
                .username("member2")
                .authority(Authority.ROLE_USER)
                .nickname("준성짱짱")
                .name("최준성")
                .phone("011001200043")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }


    public Member createMatchingMember1(Zone zone) {
        Member member = Member.builder()
                .username("matching1")
                .authority(Authority.ROLE_USER)
                .nickname("matching1")
                .name("matching1")
                .phone("01050991111")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("")
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member createMatchingMember2(Zone zone) {
        Member member = Member.builder()
                .username("matching2")
                .authority(Authority.ROLE_USER)
                .nickname("matching2")
                .name("matching2")
                .phone("01050991112")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("")
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member createMatchingMember3(Zone zone) {
        Member member = Member.builder()
                .username("matching3")
                .authority(Authority.ROLE_USER)
                .nickname("matching3")
                .name("matching3")
                .phone("01050991113")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("")
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member createMatchingMember4(Zone zone) {
        Member member = Member.builder()
                .username("matching4")
                .authority(Authority.ROLE_USER)
                .nickname("matching4")
                .name("matching4")
                .phone("01050991114")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("")
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member createMatchingMember5(Zone zone) {
        Member member = Member.builder()
                .username("matching5")
                .authority(Authority.ROLE_USER)
                .nickname("matching5")
                .name("matching5")
                .phone("01050991115")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("")
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member createMatchingMember6(Zone zone) {
        Member member = Member.builder()
                .username("matching6")
                .authority(Authority.ROLE_USER)
                .nickname("matching6")
                .name("matching6")
                .phone("01050991116")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("")
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member createRewardMember(Zone zone){
        Member member = Member.builder()
                .username("rewardMember")
                .authority(Authority.ROLE_USER)
                .nickname("rewardMember")
                .name("rewardMember")
                .phone("01050994422")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("")
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member createRewardCouponMember(Zone zone){
        Member member = Member.builder()
                .username("rewardCouponMember")
                .authority(Authority.ROLE_USER)
                .nickname("rewardCouponMember")
                .name("rewardCouponMember")
                .phone("01050994421")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("")
                .build();
        Member savedMember = memberRepository.save(member);
        return savedMember;
    }

    public Member createAdmin(Zone zone) {
        Member member = Member.builder()
                .username("admin")
                .authority(Authority.ROLE_ADMIN)
                .nickname("admin")
                .name("admin")
                .phone("01050994433")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("")
                .build();
       return memberRepository.save(member);

    }

    public Member createNotificationMember(Zone zone) {
        Member member = Member.builder()
                .username("notification")
                .authority(Authority.ROLE_USER)
                .nickname("notification")
                .name("notification")
                .phone("01050991232")
                .imageFileUrl("https://images.unsplash.com/photo-1497184091687-09d30ae055e1?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2250&q=80")
                .profileText("안녕하세요! 같이 취미 즐기실분 환영해요!")
                .directorText("test")
                .age(25)
                .sex(Sex.M)
                .zone(zone)
                .fcmToken("")
                .build();
        return memberRepository.save(member);

    }
}
