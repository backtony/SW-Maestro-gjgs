package com.batch.redisbatch.domain;

import com.batch.redisbatch.enums.Authority;
import com.batch.redisbatch.enums.Sex;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private Sex sex; // MAN, WOMAN

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(30) default 'ROLE_USER'")
    private Authority authority;

    private String directorText;

    private String fcmToken;

    private int totalReward;

    @Column(nullable = false, columnDefinition = "bit(1) default 1")
    private boolean eventAlarm;

    public void addReward(int amount) {
        this.totalReward = totalReward + amount;
    }
}
