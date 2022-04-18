package com.gjgs.gjgs.modules.utils.vo;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class FileInfoVo {

    private String fileName;
    private String fileUrl;

    public static FileInfoVo of(String fileName, String fileUrl) {
        return FileInfoVo.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .build();
    }

    public static FileInfoVo empty() {
        return FileInfoVo.builder().fileName("").fileUrl("").build();
    }
}
