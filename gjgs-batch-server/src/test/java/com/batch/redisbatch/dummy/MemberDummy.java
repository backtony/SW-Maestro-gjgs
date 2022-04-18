package com.batch.redisbatch.dummy;

import com.batch.redisbatch.domain.Member;
import com.batch.redisbatch.domain.Zone;
import com.batch.redisbatch.enums.Authority;
import com.batch.redisbatch.enums.Sex;

public class MemberDummy {

    public static Member createMember(Zone zone) {
        return Member.builder()
                .username("test")
                .nickname("test")
                .name("test")
                .phone("01012341234")
                .imageFileUrl("test")
                .age(20)
                .sex(Sex.M)
                .zone(zone)
                .authority(Authority.ROLE_USER)
                .build();
    }

    public static Member createMember(Zone zone, int i) {
        return Member.builder()
                .username("test" + String.valueOf(i))
                .nickname("test" + String.valueOf(i))
                .name("test")
                .phone("0101234123" + String.valueOf(i))
                .imageFileUrl("test")
                .age(20)
                .sex(Sex.M)
                .zone(zone)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
