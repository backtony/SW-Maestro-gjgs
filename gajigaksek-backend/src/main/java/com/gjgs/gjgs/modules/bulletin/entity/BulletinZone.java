package com.gjgs.gjgs.modules.bulletin.entity;

import com.gjgs.gjgs.modules.utils.base.BaseEntity;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
public class BulletinZone extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BULLETIN_ZONE_ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ZONE_ID", nullable = false)
    private Zone zone;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "BULLETIN_ID", nullable = false)
    private Bulletin bulletin;
}
