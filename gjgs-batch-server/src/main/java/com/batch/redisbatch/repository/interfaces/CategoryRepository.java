package com.batch.redisbatch.repository.interfaces;

import com.batch.redisbatch.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
