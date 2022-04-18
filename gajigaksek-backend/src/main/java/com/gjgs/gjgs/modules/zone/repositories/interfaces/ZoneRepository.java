package com.gjgs.gjgs.modules.zone.repositories.interfaces;

import com.gjgs.gjgs.infra.config.redis.CacheKey;
import com.gjgs.gjgs.modules.zone.entity.Zone;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ZoneRepository extends JpaRepository<Zone, Long> {

    @Query("select z from Zone z where z.id in :ids")
    List<Zone> findByListIds(@Param("ids") List<Long> ids);

    @Cacheable(value = CacheKey.ZONE,key = "#p0",unless = "#result == null")
    Optional<Zone> findById(Long id);
}
