package com.gjgs.gjgs.modules.category.repositories;

import com.gjgs.gjgs.infra.config.redis.CacheKey;
import com.gjgs.gjgs.modules.category.entity.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    @Query("select count(c) from Category c where c.id in :ids")
    Long countCategoryByIdList(@Param("ids") List<Long> ids);

    @Cacheable(value = CacheKey.CATEGORY,key = "#p0",unless = "#result == null")
    Optional<Category> findById(Long id);
}
