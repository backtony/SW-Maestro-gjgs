package com.gjgs.gjgs.modules.member.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gjgs.gjgs.modules.exception.member.InvalidKaKaoAccessTokenException;
import com.gjgs.gjgs.modules.member.dto.login.KakaoProfile;
import com.gjgs.gjgs.modules.member.service.login.impl.KakaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KakaoServiceTest {

    @Mock RestTemplate restTemplate;
    @Mock ObjectMapper objectMapper;

    @InjectMocks KakaoService kakaoService;

    @DisplayName("카카오 통신 실패")
    @Test
    void fail_to_kakao_communication() throws Exception {
        //given
        String kakaoUrl = "test";
        ReflectionTestUtils.setField(kakaoService, "kakaoProfileUrl", kakaoUrl);
        doReturn(ResponseEntity.badRequest().build()).when(restTemplate).postForEntity(eq(kakaoUrl), any(), eq(String.class));

        //when then
        assertThrows(InvalidKaKaoAccessTokenException.class, () -> kakaoService.getKakaoProfile("AccessToken"));


    }

    @DisplayName("통신 성공")
    @Test
    void success_kakao_connection() throws Exception {
        //given
        String kakaoUrl = "test";
        KakaoProfile kakaoProfile = createKakaoProfile();

        ReflectionTestUtils.setField(kakaoService, "kakaoProfileUrl", kakaoUrl);
        when(restTemplate.postForEntity(eq(kakaoUrl), any(), eq(String.class))).thenReturn(ResponseEntity.ok().build());
        when(objectMapper.readValue((String)any(),eq(KakaoProfile.class))).thenReturn(kakaoProfile);

        //when
        KakaoProfile profile = kakaoService.getKakaoProfile("Bearer accessToken");

        //then
        assertEquals(kakaoProfile, profile);
    }

    private KakaoProfile createKakaoProfile() {
        return KakaoProfile.builder()
                .id(1L)
                .properties(KakaoProfile.Properties.builder()
                        .nickname("test")
                        .profile_image("test")
                        .thumbnail_image("test")
                        .build())
                .build();
    }

}


