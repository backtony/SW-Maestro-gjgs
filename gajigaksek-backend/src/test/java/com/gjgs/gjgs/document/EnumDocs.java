package com.gjgs.gjgs.document;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnumDocs {

    // 문서화하고 싶은 모든 enum값을 명시
    Map<String, String> bulletinAge;
    Map<String, String> dayType;
    Map<String, String> timeType;

    Map<String, String> lectureStatus;
    Map<String, String> scheduleStatus;

    Map<String, String> authority;
    Map<String, String> sex;

    Map<String, String> noticeType;

    Map<String, String> notificationType;
    Map<String, String> pushMessage;
    Map<String, String> targetType;

    Map<String, String> questionStatus;

    Map<String, String> rewardType;

    Map<String, String> teamAge;
    Map<String, String> teamDayType;
    Map<String, String> teamTimeType;

    Map<String, String> decideLectureType;

    Map<String, String> searchPriceCondition;

    Map<String, String> orderStatus;

    Map<String, String> getLectureType;

    Map<String, String> getScheduleType;

    Map<String, String> scheduleResult;

    Map<String, String> pathAuthority;
    Map<String, String> createLectureStep;

    Map<String, String> lectureSaveDeleteResponse;

    Map<String, String> payType;

    Map<String, String> questionResponse;

    Map<String, String> currentLectureStatus;

    Map<String, String> matchingStatus;

    Map<String, String> alarmType;


}
