package com.gjgs.gjgs.modules.lecture.repositories.review;

import com.gjgs.gjgs.modules.lecture.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
