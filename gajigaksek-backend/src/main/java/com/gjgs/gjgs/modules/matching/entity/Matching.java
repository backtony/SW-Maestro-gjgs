package com.gjgs.gjgs.modules.matching.entity;


import com.gjgs.gjgs.modules.category.entity.Category;
import com.gjgs.gjgs.modules.matching.dto.MatchingRequest;
import com.gjgs.gjgs.modules.member.entity.Member;
import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class Matching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MATCHING_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZONE_ID", nullable = false)
    private Zone zone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String dayType;

    @Column(nullable = false)
    private String timeType;

    @Column(nullable = false)
    private int preferMemberCount;

    public static Matching of(MatchingRequest matchingRequest, Member member){
        return Matching.builder()
                .zone(Zone.of(matchingRequest.getZoneId()))
                .member(member)
                .category(Category.from(matchingRequest.getCategoryId()))
                .dayType(matchingRequest.getDayType())
                .timeType(matchingRequest.getTimeType())
                .preferMemberCount(matchingRequest.getPreferMemberCount())
                .build();
    }

}
