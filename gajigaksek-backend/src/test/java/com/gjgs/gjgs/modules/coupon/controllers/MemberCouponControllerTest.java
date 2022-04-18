package com.gjgs.gjgs.modules.coupon.controllers;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.coupon.dto.EnableMemberCouponResponse;
import com.gjgs.gjgs.modules.exception.coupon.InvalidCouponException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MemberCouponControllerTest extends RestDocsTestSupport {

    private final String COUPON_API = "/api/v1/lectures/{lectureId}/coupon";

    @BeforeEach
    void setUpMockUser() {
        securityUserMockSetting();
    }

    @Test
    @DisplayName("쿠폰 발급 받기")
    void get_member_coupon() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(COUPON_API, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("쿠폰을 발급 받을 클래스 ID")
                        )
                ));
    }

    @Test
    @DisplayName("쿠폰 발행하기 / 쿠폰 관련 예외가 발생했을 때")
    void get_member_coupon_should_not_coupon_exception() throws Exception {

        // given
        doThrow(new InvalidCouponException()).when(memberCouponService).giveMemberCoupon(any());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(COUPON_API, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("결제 시 내가 사용할 수 있는 쿠폰 가져오기")
    void get_enable_member_coupon() throws Exception {

        // given
        when(memberCouponService.getMemberCoupon(any())).thenReturn(EnableMemberCouponResponse.builder()
                .enableCouponList(Set.of(
                        EnableMemberCouponResponse.EnableCoupon.builder()
                                .memberCouponId(1L).couponTitle("테스트쿠폰").discountPrice(1000)
                                .build()
                ))
                .build());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(COUPON_API, 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enableCouponList[0].memberCouponId", is(1)))
                .andExpect(jsonPath("$.enableCouponList[0].couponTitle", is("테스트쿠폰")))
                .andExpect(jsonPath("$.enableCouponList[0].discountPrice", is(1000)))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("lectureId").description("결제할 때 사용 가능한 쿠폰의 클래스 ID")
                        ),
                        responseFields(
                                fieldWithPath("enableCouponList[0].memberCouponId").type(NUMBER).description("멤버가 발급받은 쿠폰 아이디"),
                                fieldWithPath("enableCouponList[0].couponTitle").type(STRING).description("쿠폰 제목"),
                                fieldWithPath("enableCouponList[0].discountPrice").type(NUMBER).description("할인 가격")
                        )
                ));
    }
}
