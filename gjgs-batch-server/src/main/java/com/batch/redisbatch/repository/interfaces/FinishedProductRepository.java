package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.lecture.FinishedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinishedProductRepository extends JpaRepository<FinishedProduct, Long> {
}
