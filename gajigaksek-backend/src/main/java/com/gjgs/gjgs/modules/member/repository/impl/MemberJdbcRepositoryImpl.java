package com.gjgs.gjgs.modules.member.repository.impl;

import com.gjgs.gjgs.modules.member.repository.interfaces.MemberJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberJdbcRepositoryImpl implements MemberJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void insertMemberCategoryList(Long memberId, List<Long> changeCategoryIdList) {
        jdbcTemplate.batchUpdate("insert into member_category" +
                        "(member_id,category_id,created_date,last_modified_date) values(?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, memberId);
                        ps.setLong(2, changeCategoryIdList.get(i));
                        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                    }

                    @Override
                    public int getBatchSize() {
                        return changeCategoryIdList.size();
                    }
                }
        );

    }
}
