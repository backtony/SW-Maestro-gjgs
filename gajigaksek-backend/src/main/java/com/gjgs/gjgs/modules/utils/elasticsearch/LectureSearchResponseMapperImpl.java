package com.gjgs.gjgs.modules.utils.elasticsearch;

import com.gjgs.gjgs.modules.lecture.document.LectureDocument;
import com.gjgs.gjgs.modules.lecture.dtos.search.LectureSearchResponse;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class LectureSearchResponseMapperImpl implements ResponseMapper<LectureDocument, LectureSearchResponse> {

    @Override
    public List<LectureSearchResponse> toResponse(List<LectureDocument> source) {
        return source.stream().map(lectureDocument -> LectureSearchResponse.builder()
                .lectureId(lectureDocument.getLectureId())
                .imageUrl(lectureDocument.getImageUrl())
                .title(lectureDocument.getTitle())
                .zoneId(lectureDocument.getZoneId())
                .priceOne(lectureDocument.getPriceOne().intValue())
                .priceTwo(lectureDocument.getPriceTwo().intValue())
                .priceThree(lectureDocument.getPriceThree().intValue())
                .priceFour(lectureDocument.getPriceFour().intValue())
                .build())
                .collect(toList());
    }
}
