package com.gjgs.gjgs.modules.team.repositories.implement;

import com.gjgs.gjgs.modules.team.repositories.interfaces.TeamJdbcRepository;
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
public class TeamJdbcRepositoryImpl implements TeamJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void insertTeamCategoryList(Long teamId, List<Long> categoryIdList) {
        jdbcTemplate.batchUpdate("insert into  team_category(team_id, category_id, created_date,last_modified_date) values(?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        LocalDateTime now = LocalDateTime.now();
                        ps.setLong(1, teamId);
                        ps.setLong(2, categoryIdList.get(i));
                        ps.setTimestamp(3, Timestamp.valueOf(now));
                        ps.setTimestamp(4, Timestamp.valueOf(now));
                    }

                    @Override
                    public int getBatchSize() {
                        return categoryIdList.size();
                    }
                });
    }

    @Override
    public void insertMemberTeamList(Long teamId, List<Long> memberIdList) {
        jdbcTemplate.batchUpdate("insert into member_team(member_id,team_id,created_date,last_modified_date) " +
                        "values(?,?,?,?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, memberIdList.get(i));
                        ps.setLong(2, teamId);
                        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                        ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                    }

                    @Override
                    public int getBatchSize() {
                        return memberIdList.size();
                    }
                });
    }

}
