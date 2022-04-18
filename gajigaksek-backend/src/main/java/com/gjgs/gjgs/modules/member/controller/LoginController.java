package com.gjgs.gjgs.modules.member.controller;

import com.gjgs.gjgs.modules.exception.validate.ValidatedException;
import com.gjgs.gjgs.modules.member.dto.login.FcmTokenRequest;
import com.gjgs.gjgs.modules.member.dto.login.LoginResponse;
import com.gjgs.gjgs.modules.member.dto.login.SignUpRequest;
import com.gjgs.gjgs.modules.member.dto.login.TokenDto;
import com.gjgs.gjgs.modules.member.service.login.interfaces.LoginService;
import com.gjgs.gjgs.modules.member.validator.SignUpRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SignUpRequestValidator signUpRequestValidator;

    @InitBinder("signUpRequest")
    public void initBinderSignupForm(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpRequestValidator);
    }


    @PostMapping("/login/{provider}")
    public ResponseEntity<LoginResponse> loginByProvider(@PathVariable String provider,
                                                         @RequestHeader(value = "KakaoAccessToken") String accessToken,
                                                         @RequestBody @Validated FcmTokenRequest fcmTokenRequest,
                                                         BindingResult errors) {
        if (errors.hasErrors()) {
            throw new ValidatedException(errors);
        }

        return ResponseEntity.ok(loginService.login(accessToken, fcmTokenRequest.getFcmToken()));
    }


    @PostMapping("/web/{authority}/login")
    public ResponseEntity<LoginResponse> webLogin(@RequestHeader(value = "KakaoAccessToken") String accessToken,
                                                  @PathVariable String authority) {
        return ResponseEntity.ok(loginService.webLogin(accessToken,authority));
    }


    @PostMapping("/sign-up")
    public ResponseEntity<LoginResponse> firstLogin(@RequestBody @Validated SignUpRequest signUpRequest,
                                                    BindingResult errors) {
        if (errors.hasErrors()) {
            throw new ValidatedException(errors);
        }

        return ResponseEntity.ok(loginService.saveAndLogin(signUpRequest));
    }


    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestHeader(value = "Authorization") String refreshToken) {
        return ResponseEntity.ok(loginService.reissue(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization") String accessToken,
                                       @RequestHeader(value = "RefreshToken") String refreshToken) {

        loginService.logout(accessToken, refreshToken);
        return ResponseEntity.ok().build();
    }


    // todo test 전용 임시
    @PostMapping("/fake/login/{username}")
    public ResponseEntity<LoginResponse> fakeLogin(@PathVariable String username) {
        return ResponseEntity.ok(loginService.fakeLogin(username));
    }
}
