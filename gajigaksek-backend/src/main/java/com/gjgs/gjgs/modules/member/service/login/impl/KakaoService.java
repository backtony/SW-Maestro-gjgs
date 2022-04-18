package com.gjgs.gjgs.modules.member.service.login.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gjgs.gjgs.modules.exception.member.InvalidKaKaoAccessTokenException;
import com.gjgs.gjgs.modules.exception.member.KakaoConnectionException;
import com.gjgs.gjgs.modules.member.dto.login.KakaoProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.social.kakao.profile}")
    private String kakaoProfileUrl;

    public KakaoProfile getKakaoProfile(String accessToken) {
        ResponseEntity<String> response = requestToKakao(accessToken);

        if (response.getStatusCode() != HttpStatus.OK){
            throw new InvalidKaKaoAccessTokenException();
        }

        try {
            return objectMapper.readValue(response.getBody(),KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new KakaoConnectionException();
        }
    }

    private ResponseEntity<String> requestToKakao(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(kakaoProfileUrl, request, String.class);
        return response;
    }
}
