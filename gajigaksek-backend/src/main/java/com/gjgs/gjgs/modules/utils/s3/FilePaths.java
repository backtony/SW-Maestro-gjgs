package com.gjgs.gjgs.modules.utils.s3;

import lombok.Getter;

@Getter
public enum FilePaths {

    TEAM_IMAGE_PATH("/image/team"),
    MEMBER_IMAGE_PATH("/image/member"),
    LECTURE_IMAGE_PATH("/image/lecture"),
    REVIEW_IMAGE_PATH("/image/review");

    private final String path;

    FilePaths(String path) {
        this.path = path;
    }


}
