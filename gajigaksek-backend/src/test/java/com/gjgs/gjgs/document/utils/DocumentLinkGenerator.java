package com.gjgs.gjgs.document.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface DocumentLinkGenerator {

    static String generateLinkCode(DocUrl docUrl) {
        return String.format("link:common/%s.html[%s %s,role=\"popup\"]", docUrl.pageId, docUrl.text, "코드");
    }

    static String generateText(DocUrl docUrl) {
        return String.format("%s %s", docUrl.text, "코드명");
    }

    // 필요한 url 쭉 명시
    @RequiredArgsConstructor
    enum DocUrl {
        BULLETIN_AGE("bulletin-age", "나이"),
        DAY_TYPE("day-type","요일"),
        TIME_TYPE("time-type", "시간"),
        LECTURE_CONFIRM_DECIDE_TYPE("lecture-decide-type", "검수 결정"),
        GET_LECTURE_TYPE("get-lecture-type", "찾을 클래스 상태"),
        GET_SCHEDULE_TYPE("get-schedule-type", "찾을 스케줄 상태"),
        QUESTION_STATUS("question-status", "질문글의 상태"),
        SCHEDULE_RESULT("schedule-result", "스케줄 추가/삭제 결과"),
        SEARCH_PRICE_CONDITION("search-price-condition", "가격 검색"),
        SEX("sex","성별"),
        PATH_AUTHORITY("path-authority","로그인 타입"),
        CREATE_LECTURE_STEP("create-lecture-step", "클래스를 임시 저장한 순서"),
        LECTURE_SAVE_DELETE_RESPONSE("lecture-save-delete-response", "클래스를 최종 저장 혹은 삭제할 때 응답"),
        PAY_TYPE("pay-type", "팀 혹은 개인 신청"),
        QUESTION_RESPONSE("question-response", "질문글에 대한 응답"),
        NOTICE_TYPE("notice-type", "공지사항 유형"),
        TARGET_TYPE("target-type", "알림을 보낼 타입"),
        SEX_TYPE("sex", "성별 타입"),
        ORDER_STATUS("order-status", "주문 상태"),
        CURRENT_LECTURE_STATUS("current-lecture-status", "현재 클래스 진행 상태"),
        NOTIFICATION_TYPE("notification-type", "푸시 알림"),
        MEMBER_AUTHORITY("member-authority","회원 권한"),
        REWARD_TYPE("reward-type","리워드 타입"),
        MATCHING_STATUS("matching-status","매칭 상태"),
        ALARM_TYPE("alarm-type","알림 타입")
        ;

        private final String pageId;
        @Getter
        private final String text;
    }
}
