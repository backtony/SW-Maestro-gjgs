package com.gjgs.gjgs.modules.member.entity;

import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.exception.coupon.CouponNotFoundException;
import com.gjgs.gjgs.modules.exception.reward.RewardNotEnoughException;
import com.gjgs.gjgs.modules.matching.dto.MemberFcmIncludeNicknameDto;
import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.member.enums.Alarm;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.enums.Sex;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.PERSIST;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZONE_ID", nullable = false)
    private Zone zone;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<MemberCategory> memberCategories = new ArrayList<>();


    @OneToMany(mappedBy = "member", cascade = PERSIST)
    @Builder.Default
    private List<MemberCoupon> coupons = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String username; // 카카오id

    @Column(nullable = false, unique = true)
    private String nickname; // 닉네임

    @Column(nullable = false) // 실명
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    private String profileText;

    @Lob
    @Column(nullable = false)
    private String imageFileUrl;

    @Lob
    private String imageFileName;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(30) default 'ROLE_USER'")
    private Authority authority;

    private String directorText;

    private String fcmToken;

    private int totalReward;

    @Column(nullable = false, columnDefinition = "bit(1) default 1")
    private boolean eventAlarm;

    public static Member from(Long memberId) {
        return Member.builder()
                .id(memberId)
                .build();
    }

    public static Member from(String username) {
        return Member.builder().username(username).build();
    }

    public static Member from(SignUpRequest signUpRequest) {
        return Member.builder()
                .name(signUpRequest.getName())
                .phone(signUpRequest.getPhone())
                .nickname(signUpRequest.getNickname())
                .age(signUpRequest.getAge())
                .sex(Sex.valueOf(signUpRequest.getSex()))
                .zone(Zone.of(signUpRequest.getZoneId()))
                .imageFileUrl(signUpRequest.getImageFileUrl())
                .username(String.valueOf(signUpRequest.getId()))
                .authority(Authority.ROLE_USER)
                .fcmToken(signUpRequest.getFcmToken())
                .eventAlarm(true)
                .build();
    }

    public static Member from(MemberFcmIncludeNicknameDto memberFcmIncludeNicknameDto) {
        return Member.builder()
                .id(memberFcmIncludeNicknameDto.getMemberId())
                .fcmToken(memberFcmIncludeNicknameDto.getFcmToken())
                .nickname(memberFcmIncludeNicknameDto.getNickname())
                .build();
    }

    public static Member of(Long memberId, String fcmToken, String nickname) {
        return Member.builder()
                .id(memberId)
                .fcmToken(fcmToken)
                .nickname(nickname)
                .build();
    }


    public void setMemberCategories(List<Category> categoryList) {
        this.memberCategories = categoryList.stream()
                .map(category ->
                        MemberCategory.builder()
                                .category(category)
                                .member(this)
                                .build()
                ).collect(Collectors.toList());
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeProfileText(String profileText) {
        this.profileText = profileText;
    }

    public void changePhone(String phone) {
        this.phone = phone;
    }

    public void changeDirectorText(String directorText) {
        this.directorText = directorText;
    }

    public void changeZone(Zone zone) {
        this.zone = zone;
    }

    public void changeFileInfo(String fileName, String fileUrl) {
        this.imageFileName = fileName;
        this.imageFileUrl = fileUrl;
    }

    public void changeFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void clearFcmToken() {
        this.fcmToken = "";
    }

    public void changeAuthorityToDirector() {
        this.authority = Authority.ROLE_DIRECTOR;
    }

    public void addReward(int amount) {
        this.totalReward = totalReward + amount;
    }

    public void addCoupon(MemberCoupon publishedCoupon) {
        this.getCoupons().add(publishedCoupon);
    }

    public MemberCoupon getMemberCoupon(Long memberCouponId) {
        return this.getCoupons().stream()
                .filter(memberCoupon -> memberCoupon.getId().equals(memberCouponId))
                .findFirst().orElseThrow(() -> new CouponNotFoundException());
    }

    public void useReward(int useReward) {
        if (this.getTotalReward() - useReward < 0) {
            throw new RewardNotEnoughException();
        }
        this.totalReward = totalReward - useReward;
    }

    public void changeAlarm(Alarm alarm, boolean isActive) {
        if (alarm.equals(Alarm.EVENT)) {
            eventAlarm = isActive;
        }
    }
}
