package com.gjgs.gjgs.config.repository;

import com.gjgs.gjgs.config.QuerydslConfig;
import com.gjgs.gjgs.modules.coupon.repositories.CouponQueryRepositoryImpl;
import com.gjgs.gjgs.modules.coupon.repositories.MemberCouponQueryRepositoryImpl;
import com.gjgs.gjgs.modules.favorite.repository.impl.BulletinMemberQueryRepositoryImpl;
import com.gjgs.gjgs.modules.favorite.repository.impl.LectureMemberQueryRepositoryImpl;
import com.gjgs.gjgs.modules.favorite.repository.impl.LectureTeamQueryRepositoryImpl;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureJdbcRepositoryImpl;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureQueryRepositoryImpl;
import com.gjgs.gjgs.modules.lecture.repositories.lecture.LectureSearchQueryRepositoryImpl;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantJdbcRepositoryImpl;
import com.gjgs.gjgs.modules.lecture.repositories.participant.ParticipantQueryRepositoryImpl;
import com.gjgs.gjgs.modules.lecture.repositories.review.ReviewQueryRepositoryImpl;
import com.gjgs.gjgs.modules.lecture.repositories.schedule.ScheduleQueryRepositoryImpl;
import com.gjgs.gjgs.modules.lecture.repositories.temporaryStore.TemporaryStoredLectureQueryRepositoryImpl;
import com.gjgs.gjgs.modules.matching.repository.impl.MatchingQueryRepositoryImpl;
import com.gjgs.gjgs.modules.member.repository.impl.MemberJdbcRepositoryImpl;
import com.gjgs.gjgs.modules.member.repository.impl.MemberQueryRepositoryImpl;
import com.gjgs.gjgs.modules.notice.repository.impl.NoticeQueryRepositoryImpl;
import com.gjgs.gjgs.modules.notification.repository.impl.NotificationJdbcRepositoryImpl;
import com.gjgs.gjgs.modules.notification.repository.impl.NotificationQueryRepositoryImpl;
import com.gjgs.gjgs.modules.payment.repository.OrderJdbcRepositoryImpl;
import com.gjgs.gjgs.modules.payment.repository.OrderQueryRepositoryImpl;
import com.gjgs.gjgs.modules.question.repository.QuestionQueryRepositoryImpl;
import com.gjgs.gjgs.modules.reward.repository.impl.RewardJdbcRepositoryImpl;
import com.gjgs.gjgs.modules.reward.repository.impl.RewardQueryRepositoryImpl;
import com.gjgs.gjgs.modules.team.repositories.implement.MemberTeamQueryRepositoryImpl;
import com.gjgs.gjgs.modules.team.repositories.implement.TeamApplierQueryRepositoryImpl;
import com.gjgs.gjgs.modules.team.repositories.implement.TeamJdbcRepositoryImpl;
import com.gjgs.gjgs.modules.team.repositories.implement.TeamQueryRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

@p6spyDataJpaTest
@Import({QuerydslConfig.class,
        CouponQueryRepositoryImpl.class,
        MemberCouponQueryRepositoryImpl.class,
        BulletinMemberQueryRepositoryImpl.class,
        LectureMemberQueryRepositoryImpl.class,
        LectureTeamQueryRepositoryImpl.class,
        LectureJdbcRepositoryImpl.class,
        LectureQueryRepositoryImpl.class,
        LectureSearchQueryRepositoryImpl.class,
        ParticipantQueryRepositoryImpl.class,
        ReviewQueryRepositoryImpl.class,
        ParticipantJdbcRepositoryImpl.class,
        ScheduleQueryRepositoryImpl.class,
        TemporaryStoredLectureQueryRepositoryImpl.class,
        MatchingQueryRepositoryImpl.class,
        MemberJdbcRepositoryImpl.class,
        MemberQueryRepositoryImpl.class,
        NoticeQueryRepositoryImpl.class,
        NotificationJdbcRepositoryImpl.class,
        NotificationQueryRepositoryImpl.class,
        OrderJdbcRepositoryImpl.class,
        OrderQueryRepositoryImpl.class,
        QuestionQueryRepositoryImpl.class,
        RewardJdbcRepositoryImpl.class,
        RewardQueryRepositoryImpl.class,
        MemberTeamQueryRepositoryImpl.class,
        TeamApplierQueryRepositoryImpl.class,
        TeamJdbcRepositoryImpl.class,
        TeamQueryRepositoryImpl.class
})
public class RepositoryTest {

    @Autowired EntityManager em;
    @Autowired JPAQueryFactory query;

    protected void flushAndClear() {
        em.flush();
        em.clear();
    }
}
