package com.gjgs.gjgs.modules.exception;

public enum ErrorCode {

    MISSING_FILE("FILE-400", "사진 파일이 존재하지 않습니다."),
    BINDING_EXCEPTION("FORM-400", "적절하지 않은 요청 값입니다."),
    MISSING_BODY("FORM-400","Body에 요청값이 존재하지 않습니다."),
    KEYWORD_IS_NOT_ALLOWED("SEARCH-400", "최소 1글자 이상의 검색어를 입력해주세요."),

    MISSING_REQUEST_HEADER("HEADER-400", "특정 헤더값이 들어오지 않았습니다."),
    NO_AUTHORIZATION_TOKEN("TOKEN-401", "헤더에 Authorization 토큰이 없습니다."),
    NO_KAKAO_ACCESS_TOKEN("KAKAO-401", "헤더에 KakaoAccessToken 토큰이 없습니다."),
    NO_REFRESH_TOKEN("TOKEN-401", "헤더에 RefreshToken 토큰이 없습니다."),
    ACCESS_DENIED("MEMBER-403","접근 권한이 없습니다."),

    INTERNAL_SERVER_ERROR_CODE("SERVER-405", "서버측 오류가 발생했습니다.")
    ;


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

}
