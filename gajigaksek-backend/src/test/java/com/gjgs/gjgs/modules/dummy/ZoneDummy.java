package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.zone.entity.Zone;

public class ZoneDummy {

    public static Zone createZone() {
        return Zone.builder()
                //.id(1L)
                .mainZone("서울")
                .subZone("강남")
                .build();

    }

    public static Zone createZone(Long id) {
        return Zone.builder()
                .id(id)
                .mainZone("서울")
                .subZone("강남")
                .build();

    }
}
