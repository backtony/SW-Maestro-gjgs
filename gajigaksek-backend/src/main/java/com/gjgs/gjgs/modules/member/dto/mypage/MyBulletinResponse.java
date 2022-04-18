package com.gjgs.gjgs.modules.member.dto.mypage;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyBulletinResponse {

    @Builder.Default
    private List<MyBulletinDto> myBulletinDtoList = new ArrayList<>();

    public static MyBulletinResponse from(List<MyBulletinDto> myBulletinDtoList) {
        return MyBulletinResponse.builder()
                .myBulletinDtoList(myBulletinDtoList)
                .build();
    }

}
