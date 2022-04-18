package com.gjgs.gjgs.modules.matching.repository.interfaces;


import com.gjgs.gjgs.modules.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from Matching m where m.member.id in :memberIdList")
    void deleteAllWithBulkById(@Param("memberIdList") List<Long> memberIdList);

    @Modifying(clearAutomatically = true)
    long deleteMatchingByMemberId(Long memberId);


}
