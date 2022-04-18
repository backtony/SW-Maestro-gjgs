package com.gjgs.gjgs.modules.dummy;

import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Review;
import com.gjgs.gjgs.modules.member.entity.Member;

public class ReviewDummy {

    public static Review createReview(Member reviewer, Lecture lecture, int score) {
        return Review.builder()
                .member(reviewer)
                .lecture(lecture)
                .text("test")
                .score(score)
                .build();
    }
}
