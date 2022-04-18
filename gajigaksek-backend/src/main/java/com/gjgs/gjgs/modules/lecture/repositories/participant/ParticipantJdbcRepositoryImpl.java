package com.gjgs.gjgs.modules.lecture.repositories.participant;

import com.gjgs.gjgs.modules.lecture.entity.Participant;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
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
public class ParticipantJdbcRepositoryImpl implements ParticipantJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void insertParticipants(Schedule schedule) {
        List<Participant> participantList = schedule.getParticipantList();
        jdbcTemplate.batchUpdate("insert into participant(member_id, schedule_id, created_date,last_modified_date) values (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        LocalDateTime now = now();
                        ps.setLong(1, participantList.get(i).getMember().getId());
                        ps.setLong(2, schedule.getId());
                        ps.setTimestamp(3, Timestamp.valueOf(now));
                        ps.setTimestamp(4, Timestamp.valueOf(now));
                    }

                    @Override
                    public int getBatchSize() {
                        return participantList.size();
                    }
                });
    }
}
