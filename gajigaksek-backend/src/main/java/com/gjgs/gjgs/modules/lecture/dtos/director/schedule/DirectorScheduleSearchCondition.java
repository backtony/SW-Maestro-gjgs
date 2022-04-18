package com.gjgs.gjgs.modules.lecture.dtos.director.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gjgs.gjgs.modules.utils.document.EnumType;
import com.gjgs.gjgs.modules.utils.validators.search.SearchKeyword;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter @Setter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE) @Builder
public class DirectorScheduleSearchCondition implements SearchKeyword {

    private String keyword;

    @NotNull(message = "검색 타입을 입력해주세요. Default = ALL")
    private GetScheduleType searchType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Getter @NoArgsConstructor(access = PRIVATE) @AllArgsConstructor(access = PRIVATE)
    public enum GetScheduleType implements EnumType {
        ALL("전체"),
        RECRUIT("모집 중"),
        HOLD("모집 보류"),
        CANCEL("모집 취소"),
        FULL("인원 마감"),
        CLOSE("모집 확정"),
        END("진행 종료");

        private String description;

        @Override
        public String getName() {
            return this.name();
        }
    }
}
