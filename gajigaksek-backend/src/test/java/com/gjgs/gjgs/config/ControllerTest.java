package com.gjgs.gjgs.config;

import com.gjgs.gjgs.modules.bulletin.controller.BulletinController;
import com.gjgs.gjgs.modules.bulletin.services.BulletinService;
import com.gjgs.gjgs.modules.bulletin.validators.AgeValidator;
import com.gjgs.gjgs.modules.category.repositories.CategoryRepository;
import com.gjgs.gjgs.modules.category.validators.CategoryValidator;
import com.gjgs.gjgs.modules.coupon.controllers.MemberCouponController;
import com.gjgs.gjgs.modules.coupon.services.DirectorCouponService;
import com.gjgs.gjgs.modules.coupon.services.MemberCouponService;
import com.gjgs.gjgs.modules.favorite.controller.FavoriteController;
import com.gjgs.gjgs.modules.favorite.service.interfaces.FavoriteService;
import com.gjgs.gjgs.modules.lecture.controllers.*;
import com.gjgs.gjgs.modules.lecture.services.LectureService;
import com.gjgs.gjgs.modules.lecture.services.admin.AdminLectureService;
import com.gjgs.gjgs.modules.lecture.services.apply.ApplyScheduleTeamService;
import com.gjgs.gjgs.modules.lecture.services.director.lecture.DirectorLectureService;
import com.gjgs.gjgs.modules.lecture.services.director.schedule.DirectorScheduleService;
import com.gjgs.gjgs.modules.lecture.services.review.ReviewService;
import com.gjgs.gjgs.modules.lecture.services.temporaryStore.manage.TemporaryStorageLectureManageService;
import com.gjgs.gjgs.modules.lecture.services.temporaryStore.put.*;
import com.gjgs.gjgs.modules.lecture.validators.minute.MinuteValidator;
import com.gjgs.gjgs.modules.matching.controller.MatchingController;
import com.gjgs.gjgs.modules.matching.service.interfaces.MatchingService;
import com.gjgs.gjgs.modules.matching.validator.MatchingRequestValidator;
import com.gjgs.gjgs.modules.member.controller.*;
import com.gjgs.gjgs.modules.member.service.login.interfaces.LoginService;
import com.gjgs.gjgs.modules.member.service.mypage.interfaces.DirectorEditService;
import com.gjgs.gjgs.modules.member.service.mypage.interfaces.MyInfoEditService;
import com.gjgs.gjgs.modules.member.service.mypage.interfaces.MyPageService;
import com.gjgs.gjgs.modules.member.service.search.interfaces.MemberSearchService;
import com.gjgs.gjgs.modules.member.validator.*;
import com.gjgs.gjgs.modules.notice.controller.NoticeController;
import com.gjgs.gjgs.modules.notice.service.interfaces.NoticeService;
import com.gjgs.gjgs.modules.notification.controller.NotificationController;
import com.gjgs.gjgs.modules.notification.service.interfaces.NotificationService;
import com.gjgs.gjgs.modules.payment.controller.PaymentController;
import com.gjgs.gjgs.modules.payment.service.order.OrderService;
import com.gjgs.gjgs.modules.payment.service.pay.PaymentPersonalProcessImpl;
import com.gjgs.gjgs.modules.payment.service.pay.PaymentServiceFactory;
import com.gjgs.gjgs.modules.payment.service.pay.PaymentTeamMemberProcessImpl;
import com.gjgs.gjgs.modules.question.controller.QuestionController;
import com.gjgs.gjgs.modules.question.services.QuestionService;
import com.gjgs.gjgs.modules.team.controllers.TeamController;
import com.gjgs.gjgs.modules.team.controllers.TeamManageController;
import com.gjgs.gjgs.modules.team.services.crud.TeamCrudService;
import com.gjgs.gjgs.modules.team.services.manage.TeamManageService;
import com.gjgs.gjgs.modules.utils.validators.dayTimeAge.CreateRequestTimeDayAgeValidator;
import com.gjgs.gjgs.modules.utils.validators.dayTimeAge.DayTypeValidator;
import com.gjgs.gjgs.modules.utils.validators.dayTimeAge.TimeTypeValidator;
import com.gjgs.gjgs.modules.utils.validators.search.SearchValidator;
import com.gjgs.gjgs.modules.zone.repositories.interfaces.ZoneRepository;
import com.gjgs.gjgs.modules.zone.validators.ZoneValidator;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest({
        BulletinController.class,
        SearchValidator.class,
        TimeTypeValidator.class,
        DayTypeValidator.class,
        AgeValidator.class,
        MemberCouponController.class,
        FavoriteController.class,
        AdminLectureController.class,
        ApplyTeamScheduleController.class,
        DirectorLectureController.class,
        LectureQueryController.class,
        ReviewController.class,
        TemporaryStorageLectureController.class,
        MinuteValidator.class,
        LoginController.class,
        SignUpRequestValidator.class,
        MemberSearchController.class,
        MyInfoEditController.class,
        NicknameModifyRequestValidator.class,
        CategoryModifyRequestValidator.class,
        ZoneModifyRequestValidator.class,
        MyPageController.class,
        PhoneModifyRequestValidator.class,
        NoticeController.class,
        NotificationController.class,
        PaymentController.class,
        QuestionController.class,
        TeamController.class,
        ZoneValidator.class,
        CreateRequestTimeDayAgeValidator.class,
        CategoryValidator.class,
        TeamManageController.class,
        MatchingController.class,
        MatchingRequestValidator.class,
        DirectorEditController.class
})
public abstract class ControllerTest {

    @MockBean
    protected DirectorEditService directorEditService;

    @MockBean
    protected MatchingService matchingService;

    @MockBean
    protected ZoneRepository zoneRepository;

    @MockBean
    protected TeamManageService teamManageService;

    @MockBean
    protected CategoryRepository categoryRepository;

    @MockBean
    protected TeamCrudService teamService;

    @MockBean
    protected QuestionService questionService;

    @MockBean
    protected PaymentServiceFactory paymentServiceFactory;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected PaymentPersonalProcessImpl paymentPersonalProcess;

    @MockBean
    protected PaymentTeamMemberProcessImpl paymentTeamMemberProcess;

    @MockBean
    protected NotificationService notificationService;

    @MockBean
    protected NoticeService noticeService;

    @MockBean
    protected MyPageService myPageService;

    @MockBean
    protected MyInfoEditService myInfoEditService;

    @MockBean
    protected MemberSearchService memberSearchService;

    @MockBean
    protected LoginService loginService;

    @MockBean
    protected PutLectureServiceFactory factory;

    @MockBean
    protected PutCurriculumServiceImpl putCurriculumService;

    @MockBean
    protected PutIntroServiceImpl putIntroService;

    @MockBean
    protected PutFirstServiceImpl putFirstService;

    @MockBean
    protected PutScheduleServiceImpl putScheduleService;

    @MockBean
    protected PutPriceCouponServiceImpl putPriceCouponService;

    @MockBean
    protected TemporaryStorageLectureManageService temporaryStorageLectureManageService;

    @MockBean
    protected ReviewService reviewService;

    @MockBean
    protected BulletinService bulletinService;

    @MockBean
    protected MemberCouponService memberCouponService;

    @MockBean
    protected FavoriteService favoriteService;

    @MockBean
    protected AdminLectureService adminLectureService;

    @MockBean
    protected ApplyScheduleTeamService applyTeamService;

    @MockBean
    protected DirectorLectureService directorLectureService;

    @MockBean
    protected DirectorScheduleService directorScheduleService;

    @MockBean
    protected DirectorCouponService directorCouponService;

    @MockBean
    protected LectureService lectureService;


}
