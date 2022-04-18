package com.gjgs.gjgs.modules.notification.repository.impl;

import com.gjgs.gjgs.modules.notification.entity.Notification;
import com.gjgs.gjgs.modules.notification.repository.interfaces.NotificationJdbcRepository;
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
public class NotificationJdbcRepositoryImpl implements NotificationJdbcRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void insertNotification(List<Notification> notificationList) {

        if (notificationList.get(0).getTeamId() != null){
            jdbcTemplate.batchUpdate("insert into notification" +
                            "(member_id,title,message,checked,notification_type,team_id,uuid,created_date,last_modified_date) " +
                            "values(?,?,?,?,?,?,?,?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, notificationList.get(i).getMember().getId());
                            ps.setString(2, notificationList.get(i).getTitle());
                            ps.setString(3, notificationList.get(i).getMessage());
                            ps.setBoolean(4, notificationList.get(i).isChecked());
                            ps.setString(5, notificationList.get(i).getNotificationType().name());
                            ps.setLong(6, notificationList.get(i).getTeamId());
                            ps.setString(7, notificationList.get(i).getUuid());
                            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
                        }

                        @Override
                        public int getBatchSize() {
                            return notificationList.size();
                        }
                    }
            );
        }
        else {
            jdbcTemplate.batchUpdate("insert into notification" +
                            "(member_id,title,message,checked,notification_type,uuid,created_date,last_modified_date) " +
                            "values(?,?,?,?,?,?,?,?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, notificationList.get(i).getMember().getId());
                            ps.setString(2, notificationList.get(i).getTitle());
                            ps.setString(3, notificationList.get(i).getMessage());
                            ps.setBoolean(4, notificationList.get(i).isChecked());
                            ps.setString(5, notificationList.get(i).getNotificationType().name());
                            ps.setString(6, notificationList.get(i).getUuid());
                            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                        }

                        @Override
                        public int getBatchSize() {
                            return notificationList.size();
                        }
                    }
            );
        }

    }
}
