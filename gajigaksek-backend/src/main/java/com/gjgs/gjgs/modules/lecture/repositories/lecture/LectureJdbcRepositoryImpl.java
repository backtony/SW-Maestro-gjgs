package com.gjgs.gjgs.modules.lecture.repositories.lecture;

import com.gjgs.gjgs.modules.lecture.entity.Curriculum;
import com.gjgs.gjgs.modules.lecture.entity.FinishedProduct;
import com.gjgs.gjgs.modules.lecture.entity.Lecture;
import com.gjgs.gjgs.modules.lecture.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

@Repository
@RequiredArgsConstructor
public class LectureJdbcRepositoryImpl implements LectureJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void insertFinishedProduct(Lecture lecture) {
        List<FinishedProduct> productList = lecture.getFinishedProductList();
        jdbcTemplate.batchUpdate("insert into finished_product(lecture_id, orders, finished_product_image_name, finished_product_image_url, text, created_date,last_modified_date)" +
                        " values(?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        LocalDateTime now = now();
                        ps.setLong(1, lecture.getId());
                        ps.setInt(2, productList.get(i).getOrders());
                        ps.setString(3, productList.get(i).getFinishedProductImageName());
                        ps.setString(4, productList.get(i).getFinishedProductImageUrl());
                        ps.setString(5, productList.get(i).getText());
                        ps.setTimestamp(6, Timestamp.valueOf(now));
                        ps.setTimestamp(7, Timestamp.valueOf(now));
                    }

                    @Override
                    public int getBatchSize() {
                        return productList.size();
                    }
                });
    }

    @Override
    public void insertCurriculum(Lecture lecture) {
        List<Curriculum> curriculums = lecture.getCurriculumList();
        jdbcTemplate.batchUpdate("insert into curriculum(lecture_id, orders, title, detail_text, curriculum_image_name, curriculum_image_url, created_date,last_modified_date)" +
                " values(?, ?, ?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                LocalDateTime now = now();
                ps.setLong(1, lecture.getId());
                ps.setInt(2, curriculums.get(i).getOrders());
                ps.setString(3, curriculums.get(i).getTitle());
                ps.setString(4, curriculums.get(i).getDetailText());
                ps.setString(5, curriculums.get(i).getCurriculumImageName());
                ps.setString(6, curriculums.get(i).getCurriculumImageUrl());
                ps.setTimestamp(7, Timestamp.valueOf(now));
                ps.setTimestamp(8, Timestamp.valueOf(now));
            }

            @Override
            public int getBatchSize() {return curriculums.size();}
        });
    }

    @Override
    public void insertSchedule(Lecture lecture) {
        List<Schedule> schedules = lecture.getScheduleList();
        jdbcTemplate.batchUpdate("insert into schedule(lecture_id, lecture_date, " +
                "current_participants, " +
                "start_time, end_time, progress_minutes, " +
                "created_date,last_modified_date, schedule_status)" +
                " values(?, ?, ?, ?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                LocalDateTime now = now();
                ps.setLong(1, lecture.getId());
                ps.setDate(2, Date.valueOf(schedules.get(i).getLectureDate()));
                ps.setInt(3, 0);
                ps.setTime(4, Time.valueOf(schedules.get(i).getStartTime()));
                ps.setTime(5, Time.valueOf(schedules.get(i).getEndTime()));
                ps.setInt(6, schedules.get(i).getProgressMinutes());
                ps.setTimestamp(7, Timestamp.valueOf(now));
                ps.setTimestamp(8, Timestamp.valueOf(now));
                ps.setString(9, schedules.get(i).getScheduleStatus().name());
            }

            @Override
            public int getBatchSize() {
                return schedules.size();
            }
        });
    }
}
