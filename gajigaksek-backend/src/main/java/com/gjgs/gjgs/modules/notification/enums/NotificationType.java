package com.gjgs.gjgs.modules.notification.enums;

import com.gjgs.gjgs.modules.utils.document.EnumType;

public enum NotificationType implements EnumType {

    MATCHING_COMPLETE("매칭 완료 알림")
    ,ADMIN_CUSTOM("관리자 커스텀 알림")
    ,TEAM_APPLY("팀 신청 알림")
    ;

    private String description;

    NotificationType(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
