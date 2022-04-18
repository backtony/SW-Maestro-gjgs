package com.gjgs.gjgs.modules.member.service.login.interfaces;

import com.gjgs.gjgs.modules.member.dto.login.LoginResponse;
import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.member.dto.login.TokenDto;

public interface LoginService {

    LoginResponse login(String accessToken, String fcmToken);

    LoginResponse webLogin(String accessToken, String authority);

    LoginResponse saveAndLogin(SignUpRequest signUpRequest);

    TokenDto reissue(String refreshToken);

    void logout(String accessToken, String refreshToken);

    // todo test용 추후 삭제
    LoginResponse fakeLogin(String username);
}
