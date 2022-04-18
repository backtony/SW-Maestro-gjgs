package com.gjgs.gjgs.modules.favorite.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureMemberDtoResponse {

    private List<LectureMemberDto> lectureMemberDtoList;

    public static LectureMemberDtoResponse from(List<LectureMemberDto> lectureMemberDtoList) {
        return LectureMemberDtoResponse.builder()
                .lectureMemberDtoList(lectureMemberDtoList)
                .build();
    }
}
