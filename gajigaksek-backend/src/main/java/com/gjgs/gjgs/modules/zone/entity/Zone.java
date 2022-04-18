package com.gjgs.gjgs.modules.zone.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Zone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ZONE_ID")
    private Long id;

    @Column(nullable = false)
    private String mainZone;

    private String subZone;

    public static Zone of(Long zoneId) {
        return Zone.builder().id(zoneId).build();
    }
}
