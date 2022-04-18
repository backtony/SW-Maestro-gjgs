package com.gjgs.gjgs.modules.lecture.dtos.create;

import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TemporaryStorageLectureManageResponse {

    private Long lectureId;
    private ResultResponse resultResponse;
    private String description;

    public static TemporaryStorageLectureManageResponse save(Long id) {
        return TemporaryStorageLectureManageResponse.builder()
                .lectureId(id)
                .resultResponse(ResultResponse.SAVE)
                .description(ResultResponse.SAVE.getDescription())
                .build();
    }

    public static TemporaryStorageLectureManageResponse delete() {
        return TemporaryStorageLectureManageResponse.builder()
                .resultResponse(ResultResponse.DELETE)
                .description(ResultResponse.DELETE.getDescription())
                .build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public enum ResultResponse implements EnumType {
        DELETE("임시 저장된 클래스 정보가 삭제되었습니다."),
        SAVE("임시 저장된 클래스가 실제 DB에 저장되었습니다.")
        ;

        private String description;

        ResultResponse(String description) {
            this.description = description;
        }

        @Override
        public String getName() {
            return this.name();
        }
    }
}
