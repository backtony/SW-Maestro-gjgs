package com.gjgs.gjgs.modules.payment.repository;

import com.gjgs.gjgs.modules.payment.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

@Repository
@RequiredArgsConstructor
public class OrderJdbcRepositoryImpl implements OrderJdbcRepository{

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void insertOrders(List<Order> orderList) {
        jdbcTemplate.batchUpdate(
                "insert into orders(member_id, schedule_id, order_status, original_price, discount_price, final_price, team_id, created_date, last_modified_date) " +
                        "values(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        LocalDateTime now = now();
                        ps.setLong(1,orderList.get(i).getMember().getId());
                        ps.setLong(2,orderList.get(i).getSchedule().getId());
                        ps.setString(3,orderList.get(i).getOrderStatus().name());
                        ps.setInt(4,orderList.get(i).getOriginalPrice());
                        ps.setInt(5, 0);
                        ps.setInt(6, 0);
                        ps.setLong(7, orderList.get(i).getTeam().getId());
                        ps.setTimestamp(8, Timestamp.valueOf(now));
                        ps.setTimestamp(9, Timestamp.valueOf(now));
                    }

                    @Override
                    public int getBatchSize() {
                        return orderList.size();
                    }
                }
        );
    }
}
