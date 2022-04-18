package com.gjgs.gjgs.modules.member.dto.mypage;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyQuestionResponse {

    @Builder.Default
    private List<MyQuestionDto> myQuestionDtoList = new ArrayList<>();

    public static MyQuestionResponse from(List<MyQuestionDto> myQuestionDtoList) {
        return MyQuestionResponse.builder()
                .myQuestionDtoList(myQuestionDtoList)
                .build();
    }
}
