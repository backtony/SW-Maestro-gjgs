package com.gjgs.gjgs.modules.reward.repository.impl;

import com.gjgs.gjgs.modules.reward.entity.Reward;
import com.gjgs.gjgs.modules.reward.repository.interfaces.RewardJdbcRepository;
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
public class RewardJdbcRepositoryImpl implements RewardJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void insertRewardList(List<Reward> rewardList) {
        jdbcTemplate.batchUpdate("insert into reward" +
                        "(member_id,amount,text,reward_type,created_date,last_modified_date) values(?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, rewardList.get(i).getMember().getId());
                        ps.setInt(2, rewardList.get(i).getAmount());
                        ps.setString(3, rewardList.get(i).getText());
                        ps.setString(4, rewardList.get(i).getRewardType().name());
                        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                    }

                    @Override
                    public int getBatchSize() {
                        return rewardList.size();
                    }
                }
        );

    }
}
