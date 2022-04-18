package com.batch.redisbatch.dummy;

import com.batch.redisbatch.domain.lecture.Lecture;
import com.batch.redisbatch.domain.lecture.Price;
import com.batch.redisbatch.domain.lecture.Terms;

import static com.batch.redisbatch.domain.lecture.LectureStatus.ACCEPT;

public class LectureDummy {

    public static Lecture createLecture() {
        return Lecture.builder()
                .thumbnailImageFileUrl("test")
                .title("test")
                .price(Price.builder().regularPrice(1000).priceFour(1000).priceThree(1000).priceTwo(1000).priceOne(1000).build())
                .terms(Terms.builder().termsOne(true).termsTwo(true).termsThree(true).termsFour(true).build())
                .fullAddress("test")
                .lectureStatus(ACCEPT)
                .build();
    }
}
