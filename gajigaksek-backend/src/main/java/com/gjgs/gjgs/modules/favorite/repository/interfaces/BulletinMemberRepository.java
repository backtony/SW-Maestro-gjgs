package com.gjgs.gjgs.modules.favorite.repository.interfaces;


import com.gjgs.gjgs.modules.favorite.entity.BulletinMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BulletinMemberRepository extends JpaRepository<BulletinMember, Long> {
}
