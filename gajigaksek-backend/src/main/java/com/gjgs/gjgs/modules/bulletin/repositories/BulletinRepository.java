package com.gjgs.gjgs.modules.bulletin.repositories;

import com.gjgs.gjgs.modules.bulletin.entity.Bulletin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BulletinRepository extends JpaRepository<Bulletin, Long>,
                                                                        BulletinQueryRepository,
                                                                        BulletinSearchQueryRepository {

}
