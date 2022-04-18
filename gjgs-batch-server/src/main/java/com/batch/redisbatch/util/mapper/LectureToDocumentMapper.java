package com.batch.redisbatch.util.mapper;

import com.batch.redisbatch.document.LectureDocument;
import com.batch.redisbatch.domain.lecture.Lecture;
import org.springframework.stereotype.Component;

@Component
public class LectureToDocumentMapper {

    public static LectureDocument toDocument(Lecture lecture) {
        return LectureDocument.builder()
                .lectureId(lecture.getId())
                .imageUrl(lecture.getThumbnailImageFileUrl())
                .title(lecture.getTitle())
                .description(lecture.getMainText())
                .finishedProductText(lecture.getFinishedProductTexts())
                .regularPrice((long) lecture.getPrice().getRegularPrice())
                .priceOne((long) lecture.getPrice().getRegularPrice())
                .priceTwo((long) lecture.getPrice().getRegularPrice())
                .priceThree((long) lecture.getPrice().getRegularPrice())
                .priceFour((long) lecture.getPrice().getRegularPrice())
                .reviewCount((long) lecture.getReviewList().size())
                .score(lecture.getScore())
                .clickCount((long) lecture.getClickCount())
                .build();
    }
}
