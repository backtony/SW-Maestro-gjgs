package com.gjgs.gjgs.modules.member.repository.interfaces;

import com.gjgs.gjgs.modules.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByPhone(String phone);

    boolean existsByUsername(String username);

    boolean existsByNickname(String username);

    boolean existsByPhone(String username);

    @Query("select m.id from Member m where m.username =:username")
    Optional<Long> findIdByUsername(@Param("username") String username);

    @EntityGraph(attributePaths = "memberCategories")
    Optional<Member> findWithMemberCategoryByUsername(String username);
}
