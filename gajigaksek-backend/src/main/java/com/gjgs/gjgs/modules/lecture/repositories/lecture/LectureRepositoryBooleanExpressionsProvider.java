package com.gjgs.gjgs.modules.lecture.repositories.lecture;


import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.GetLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleSearchCondition.GetScheduleType;
import com.gjgs.gjgs.modules.lecture.entity.QLecture;
import com.gjgs.gjgs.modules.lecture.entity.QSchedule;
import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus;
import com.gjgs.gjgs.modules.member.entity.QMember;
import com.gjgs.gjgs.modules.question.entity.QQuestion;
import com.gjgs.gjgs.modules.question.enums.QuestionStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

import static com.gjgs.gjgs.modules.lecture.entity.QLecture.lecture;

public abstract class LectureRepositoryBooleanExpressionsProvider {

    protected BooleanExpression isCreating() {
        return lecture.lectureStatus.eq(LectureStatus.CREATING);
    }

    protected BooleanExpression directorUsernameEq(String username, QMember director) {
        return director.username.eq(username);
    }

    protected BooleanExpression lectureStatusFinishedExpression(QLecture lecture, GetLectureType type) {
        switch(type) {
            case TEMP:
                return lecture.lectureStatus.eq(LectureStatus.CREATING);
            case PROGRESS:
                return lecture.finished.eq(false).and(lecture.lectureStatus.eq(LectureStatus.ACCEPT));
            case CLOSED:
                return lecture.finished.eq(true).and(lecture.lectureStatus.eq(LectureStatus.ACCEPT));
            case CONFIRM:
                return lecture.lectureStatus.eq(LectureStatus.CONFIRM);
            case REJECT:
                return lecture.lectureStatus.eq(LectureStatus.REJECT);
            default:
                return null;
        }
    }

    protected BooleanExpression isAccepted(QLecture lecture) {
        return lecture.lectureStatus.eq(LectureStatus.ACCEPT);
    }

    protected BooleanExpression containsKeyword(QLecture lecture, String keyword) {
        return !StringUtils.hasText(keyword)
                ? lecture.title.contains("")
                : lecture.title.contains(keyword);
    }

    protected BooleanExpression scheduleStatusType(QSchedule schedule, GetScheduleType type) {
        switch(type) {
            case RECRUIT:
                return schedule.scheduleStatus.eq(ScheduleStatus.RECRUIT);
            case HOLD:
                return schedule.scheduleStatus.eq(ScheduleStatus.HOLD);
            case CANCEL:
                return schedule.scheduleStatus.eq(ScheduleStatus.CANCEL);
            case FULL:
                return schedule.scheduleStatus.eq(ScheduleStatus.FULL);
            case CLOSE:
                return schedule.scheduleStatus.eq(ScheduleStatus.CLOSE);
            case END:
                return schedule.scheduleStatus.eq(ScheduleStatus.END);
            default:
                return null;
        }
    }

    protected BooleanExpression dateGoeLoe(QSchedule schedule, LocalDate startDate, LocalDate endDate) {
        if (onlyStartDateIsNull(startDate, endDate)) {
            return schedule.lectureDate.loe(endDate);
        } else if (onlyEndDateIsNull(startDate, endDate)) {
            return schedule.lectureDate.goe(startDate);
        } else if (isNotNullStartDateEndDate(startDate, endDate)) {
            return schedule.lectureDate.goe(startDate).and(schedule.lectureDate.loe(endDate));
        }
        return null;
    }

    protected BooleanExpression answerStatusEq(QQuestion question, QuestionStatus questionStatus) {
        return question.questionStatus.eq(questionStatus);
    }

    protected BooleanExpression lectureIdEq(QLecture lecture, Long lectureId) {
        if (lectureId == null) {
            return null;
        }
        return lecture.id.eq(lectureId);
    }

    private boolean onlyStartDateIsNull(LocalDate startDate, LocalDate endDate) {
        return startDate == null && endDate != null;
    }

    private boolean onlyEndDateIsNull(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate == null;
    }

    private boolean isNotNullStartDateEndDate(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null;
    }
}
