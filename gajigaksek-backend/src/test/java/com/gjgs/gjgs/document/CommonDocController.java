package com.gjgs.gjgs.document;

import com.gjgs.gjgs.modules.bulletin.enums.Age;
import com.gjgs.gjgs.modules.bulletin.enums.DayType;
import com.gjgs.gjgs.modules.bulletin.enums.TimeType;
import com.gjgs.gjgs.modules.exception.member.MemberNotAdminException;
import com.gjgs.gjgs.modules.exception.member.MemberNotDirectorException;
import com.gjgs.gjgs.modules.exception.member.MemberNotFoundException;
import com.gjgs.gjgs.modules.lecture.dtos.admin.DecideLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.create.CreateLectureStep;
import com.gjgs.gjgs.modules.lecture.dtos.create.TemporaryStorageLectureManageResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.lecture.GetLectureType;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleResponse;
import com.gjgs.gjgs.modules.lecture.dtos.director.schedule.DirectorScheduleSearchCondition;
import com.gjgs.gjgs.modules.lecture.dtos.search.SearchPriceCondition;
import com.gjgs.gjgs.modules.lecture.enums.LectureStatus;
import com.gjgs.gjgs.modules.lecture.enums.ScheduleStatus;
import com.gjgs.gjgs.modules.matching.enums.Status;
import com.gjgs.gjgs.modules.member.dto.mypage.MyLectureResponse;
import com.gjgs.gjgs.modules.member.enums.Alarm;
import com.gjgs.gjgs.modules.member.enums.Authority;
import com.gjgs.gjgs.modules.member.enums.Sex;
import com.gjgs.gjgs.modules.notice.enums.NoticeType;
import com.gjgs.gjgs.modules.notification.enums.NotificationType;
import com.gjgs.gjgs.modules.notification.enums.PushMessage;
import com.gjgs.gjgs.modules.notification.enums.TargetType;
import com.gjgs.gjgs.modules.payment.dto.PayType;
import com.gjgs.gjgs.modules.payment.enums.OrderStatus;
import com.gjgs.gjgs.modules.question.dto.QuestionResult;
import com.gjgs.gjgs.modules.question.enums.QuestionStatus;
import com.gjgs.gjgs.modules.reward.enums.RewardType;
import com.gjgs.gjgs.modules.utils.document.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class CommonDocController {

    @PostMapping("/error")
    public void errorSample(@RequestBody @Valid SampleRequest dto) {
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SampleRequest {

        @NotEmpty
        private String name;

        @Email
        private String email;
    }


    @GetMapping("/enums")
    public ApiResponseDto<EnumDocs> findEnums() {

        // 문서화 하고 싶은 -> EnumDocs 클래스에 담긴 모든 Enum 값 생성

        Map<String, String> bulletinAge = getDocs(Age.values());
        Map<String, String> dayType = getDocs(DayType.values());
        Map<String, String> timeType = getDocs(TimeType.values());

        Map<String, String> lectureStatus = getDocs(LectureStatus.values());
        Map<String, String> scheduleStatus = getDocs(ScheduleStatus.values());

        Map<String, String> authority = getDocs(Authority.values());
        Map<String, String> sex = getDocs(Sex.values());

        Map<String, String> noticeType = getDocs(NoticeType.values());

        Map<String, String> notificationType = getDocs(NotificationType.values());
        Map<String, String> pushMessage = getDocs(PushMessage.values());
        Map<String, String> targetType = getDocs(TargetType.values());

        Map<String, String> questionStatus = getDocs(QuestionStatus.values());

        Map<String, String> rewardType = getDocs(RewardType.values());

        Map<String, String> teamAge = getDocs(com.gjgs.gjgs.modules.team.enums.Age.values());
        Map<String, String> teamDayType = getDocs(com.gjgs.gjgs.modules.team.enums.DayType.values());
        Map<String, String> teamTimeType = getDocs(com.gjgs.gjgs.modules.team.enums.TimeType.values());

        Map<String, String> decideLectureType = getDocs(DecideLectureType.values());

        Map<String, String> searchPriceCondition = getDocs(SearchPriceCondition.values());

        Map<String, String> orderStatus = getDocs(OrderStatus.values());

        Map<String, String> getLectureType = getDocs(GetLectureType.values());

        Map<String, String> getScheduleType = getDocs(DirectorScheduleSearchCondition.GetScheduleType.values());

        Map<String, String> scheduleResult = getDocs(DirectorScheduleResponse.PostDelete.Result.values());

        Map<String, String> pathAuthority = getDocs(com.gjgs.gjgs.document.enums.Authority.values());

        Map<String, String> createLectureStep = getDocs(CreateLectureStep.values());

        Map<String, String> lectureSaveDeleteResponse = getDocs(TemporaryStorageLectureManageResponse.ResultResponse.values());

        Map<String, String> payType = getDocs(PayType.values());

        Map<String, String> questionResponse = getDocs(QuestionResult.values());

        Map<String, String> currentLectureStatus = getDocs(MyLectureResponse.CurrentLectureStatus.values());

        Map<String, String> matchingStatus = getDocs(Status.values());

        Map<String, String> alarmType = getDocs(Alarm.values());

        // 전부 담아서 반환 -> 테스트에서는 이걸 꺼내 해석하여 조각을 만들면 된다.
        return ApiResponseDto.from(EnumDocs.builder()
                .bulletinAge(bulletinAge)
                .dayType(dayType)
                .timeType(timeType)
                .lectureStatus(lectureStatus)
                .scheduleStatus(scheduleStatus)
                .authority(authority)
                .sex(sex)
                .noticeType(noticeType)
                .notificationType(notificationType)
                .pushMessage(pushMessage)
                .targetType(targetType)
                .questionStatus(questionStatus)
                .rewardType(rewardType)
                .teamAge(teamAge)
                .teamDayType(teamDayType)
                .teamTimeType(teamTimeType)
                .decideLectureType(decideLectureType)
                .searchPriceCondition(searchPriceCondition)
                .orderStatus(orderStatus)
                .getLectureType(getLectureType)
                .getScheduleType(getScheduleType)
                .scheduleResult(scheduleResult)
                .pathAuthority(pathAuthority)
                .createLectureStep(createLectureStep)
                .lectureSaveDeleteResponse(lectureSaveDeleteResponse)
                .payType(payType)
                .questionResponse(questionResponse)
                .currentLectureStatus(currentLectureStatus)
                .matchingStatus(matchingStatus)
                .alarmType(alarmType)
                .build()
        );

    }

    private Map<String, String> getDocs(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
                .collect(Collectors.toMap(EnumType::getName, EnumType::getDescription));
    }

    @GetMapping("/user")
    public void errorUserAuthorization() {
        throw new MemberNotFoundException();
    }

    @GetMapping("/director")
    public void errorDirectorAuthorization() {
        throw new MemberNotDirectorException();
    }

    @GetMapping("/admin")
    public void errorAdminAuthorization() {
        throw new MemberNotAdminException();
    }
}
