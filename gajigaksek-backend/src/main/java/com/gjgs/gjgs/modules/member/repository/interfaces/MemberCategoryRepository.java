package com.gjgs.gjgs.modules.member.repository.interfaces;

import com.gjgs.gjgs.modules.member.entity.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from MemberCategory mc where mc.id in :ids")
    void deleteAllWithBulkById(@Param("ids") List<Long> ids);

    @Modifying(clearAutomatically = true)
    @Query("delete from MemberCategory mc where mc.member.id =:id")
    void deleteAllWithBulkByMemberId(@Param("id") Long id);

    boolean existsByMemberId(Long id);
}
