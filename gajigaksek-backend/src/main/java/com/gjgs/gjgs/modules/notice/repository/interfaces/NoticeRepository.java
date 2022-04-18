package com.gjgs.gjgs.modules.notice.repository.interfaces;

import com.gjgs.gjgs.modules.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
