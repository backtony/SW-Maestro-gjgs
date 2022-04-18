package com.gjgs.gjgs.modules.payment.controller;

import com.gjgs.gjgs.document.utils.RestDocsTestSupport;
import com.gjgs.gjgs.modules.exception.payment.CanNotCancelOrderException;
import com.gjgs.gjgs.modules.exception.payment.InvalidPayTypeException;
import com.gjgs.gjgs.modules.exception.payment.InvalidPriceException;
import com.gjgs.gjgs.modules.exception.schedule.ScheduleTimeOverException;
import com.gjgs.gjgs.modules.payment.dto.OrderIdDto;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.gjgs.gjgs.modules.payment.dto.PaymentVerifyRequest;
import com.gjgs.gjgs.modules.payment.dto.TeamMemberPaymentResponse;
import com.siot.IamportRestClient.exception.IamportResponseException;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import retrofit2.HttpException;
import retrofit2.Response;

import java.io.IOException;

import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.DocUrl.PAY_TYPE;
import static com.gjgs.gjgs.document.utils.DocumentLinkGenerator.generateLinkCode;
import static com.gjgs.gjgs.document.utils.RestDocsConfig.field;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest extends RestDocsTestSupport {

    private final String SCHEDULE_URL = "/api/v1/payment/{payType}/{scheduleId}";
    private final String ORDER_URL = "/api/v1/payment/{payType}/{orderId}";


    @BeforeEach
    void setUpMockUser() {
        securityUserMockSetting();
    }

    @Test
    @DisplayName("공통 에러 1. 결제 타입이 없을 경우")
    void common_exception_not_found_pay_type() throws Exception {

        // given
        when(paymentServiceFactory.getProcess(any()))
                .thenThrow(new InvalidPayTypeException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(SCHEDULE_URL, "TEAM", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(PaymentRequest.builder().lectureId(1L).build()))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("공통 에러 2. 마감 시간을 지키지 않고 신청할 경우")
    void common_exception_not_valid_close_time() throws Exception {

        // given
        when(paymentServiceFactory.getProcess(any()))
                .thenReturn(paymentTeamMemberProcess);
        when(paymentTeamMemberProcess.payProcess(any(), any()))
                .thenThrow(new ScheduleTimeOverException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(SCHEDULE_URL, "TEAM", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(PaymentRequest.builder().lectureId(1L).build()))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("공통 에러 3. 아임포트 관련 에러 발생")
    void common_exception_iamport() throws Exception {

        // given
        when(orderService.verifyAndCompletePayment(any()))
                .thenThrow(new IamportResponseException("아임포트 관련 에러, 결제 내역을 조회할 수 없습니다.",
                        new HttpException(Response.error(400, ResponseBody.create(null, "")))));

        // when, then

        mockMvc.perform(RestDocumentationRequestBuilders.patch(SCHEDULE_URL, "TEAM", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(PaymentVerifyRequest.builder().orderId(1L).iamportUid("test").build()))
                .contentType(APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("공통 에러 4. 할인된 금액과 맞지 않은 경우")
    void common_exception_invalid_payment() throws Exception {

        // given
        String json = createJson(createPaymentRequest());
        stubbingPersonalPayment();
        when(paymentPersonalProcess.payProcess(any(), any()))
                .thenThrow(new InvalidPriceException());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(SCHEDULE_URL, "PERSONAL", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(json)
                .contentType(APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("팀 신청시 결제 내역 조회하기")
    void get_team_members_payment() throws Exception {

        // given
        when(orderService.getTeamMemberPayment(any()))
                .thenReturn(createTeamMemberPaymentResponse());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get(SCHEDULE_URL, "TEAM", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("payType").description(generateLinkCode(PAY_TYPE)),
                                parameterWithName("scheduleId").description("조회할 스케줄 ID")
                        ),
                        responseFields(
                                fieldWithPath("orderId").type(NUMBER).description("주문 ID"),
                                fieldWithPath("price").type(NUMBER).description("주문 가격"),
                                fieldWithPath("scheduleId").type(NUMBER).description("신청한 스케줄 ID"),
                                fieldWithPath("startTime").type(STRING).description("시작 시간"),
                                fieldWithPath("endTime").type(STRING).description("종료 시간"),
                                fieldWithPath("lectureThumbnailUrl").type(STRING).description("클래스 썸네일 URL"),
                                fieldWithPath("lectureTitle").type(STRING).description("클래스 제목"),
                                fieldWithPath("teamId").type(NUMBER).description("팀 ID"),
                                fieldWithPath("teamName").type(STRING).description("팀 이름"),
                                fieldWithPath("haveReward").type(NUMBER).description("내가 갖고있는 리워드")
                        )
                ));
    }

    private TeamMemberPaymentResponse createTeamMemberPaymentResponse() {
        return TeamMemberPaymentResponse.builder()
                .orderId(1L)
                .price(3000)
                .scheduleId(1L)
                .startTime(now())
                .endTime(now().plusHours(2L))
                .lectureThumbnailUrl("test")
                .lectureTitle("test")
                .teamId(1L)
                .teamName("test")
                .haveReward(1000)
                .build();
    }

    @Test
    @DisplayName("클래스를 신청한 멤버의 결제하기")
    void pay_team_member() throws Exception {

        // given
        String json = createJson(createPaymentRequest());
        stubbingPersonalPayment();
        when(paymentPersonalProcess.payProcess(any(), any()))
                .thenReturn(OrderIdDto.ofComplete(1L));

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(SCHEDULE_URL, "PERSONAL", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(json)
                .contentType(APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("payType").description(generateLinkCode(PAY_TYPE)),
                                parameterWithName("scheduleId").description("신청한 스케줄 ID")
                        ),
                        requestFields(
                                fieldWithPath("lectureId").type(NUMBER).description("신청한 클래스 ID"),
                                fieldWithPath("memberCouponId").type(NUMBER).description("선택한 쿠폰 ID"),
                                fieldWithPath("rewardAmount").type(NUMBER).description("사용할 리워드 총 금액"),
                                fieldWithPath("totalPrice").type(NUMBER).description("할인이 적용된 가격").attributes(field("constraints", "금액이 맞지 않을 시 예외 발생"))
                        ),
                        responseFields(
                                fieldWithPath("orderId").description("결제 완료된 주문 ID"),
                                fieldWithPath("description").description("응답에 대한 상세 설명")
                        )
                ));
    }

    @Test
    @DisplayName("클래스를 신청한 멤버의 결제하기 / 신청한 클래스 ID를 입력하지 않은 경우 예외 발생")
    void pay_team_member_should_input_lecture_id() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post(SCHEDULE_URL, "PERSONAL", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(PaymentRequest.builder().build()))
                .contentType(APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    @DisplayName("결제 검증하기")
    void verify_payment() throws Exception {

        // given
        String json = createJson(createPaymentVerifyRequest());
        stubbingVerifyPayment();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(SCHEDULE_URL, "PERSONAL", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(json)
                .contentType(APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("payType").description(generateLinkCode(PAY_TYPE)),
                                parameterWithName("scheduleId").description("신청한 스케줄 ID")
                        ),
                        requestFields(
                                fieldWithPath("orderId").type(NUMBER).description("검증할 주문 ID"),
                                fieldWithPath("iamportUid").type(STRING).description("아임포트에서 결제 완료 후 받은 UID")
                        ),
                        responseFields(
                                fieldWithPath("orderId").description("결제 완료된 주문 ID"),
                                fieldWithPath("description").description("응답에 대한 상세 설명")
                        )
                ));
    }

    @Test
    @DisplayName("결제 검증하기 / 검증이 되지 않아 자동 취소되었을 때")
    void verify_payment_auto_cancel() throws Exception {

        // given
        String json = createJson(createPaymentVerifyRequest());
        stubbingVerifyPaymentAutoCancel();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(SCHEDULE_URL, "PERSONAL", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(json)
                .contentType(APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("payType").description(generateLinkCode(PAY_TYPE)),
                                parameterWithName("scheduleId").description("신청한 스케줄 ID")
                        ),
                        requestFields(
                                fieldWithPath("orderId").type(NUMBER).description("검증할 주문 ID"),
                                fieldWithPath("iamportUid").type(STRING).description("아임포트에서 결제 완료 후 받은 UID")
                        ),
                        responseFields(
                                fieldWithPath("orderId").description("결제 취소된 주문 ID"),
                                fieldWithPath("description").description("응답에 대한 상세 설명")
                        )
                ));
    }

    private void stubbingVerifyPaymentAutoCancel() throws IamportResponseException, IOException {
        when(orderService.verifyAndCompletePayment(any()))
                .thenReturn(OrderIdDto.ofCancel(1L));
    }

    @Test
    @DisplayName("결제 검증하기 / 주문 ID와 아임포트 UID가 없을 경우 예외 발생")
    void verify_payment_should_input_order_id_and_iamport_uid() throws Exception {

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.patch(SCHEDULE_URL, "PERSONAL", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .content(createJson(PaymentVerifyRequest.builder().build()))
                .contentType(APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    private void stubbingVerifyPayment() throws IamportResponseException, IOException {
        when(orderService.verifyAndCompletePayment(any())).thenReturn(OrderIdDto.ofComplete(1L));
    }

    @Test
    @DisplayName("결제 취소하기")
    void cancel_payment() throws Exception {

        // given
        stubbingTeamPayment();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(ORDER_URL, "TEAM", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("JWT access token")
                        ),
                        pathParameters(
                                parameterWithName("orderId").description("주문 ID"),
                                parameterWithName("payType").description(generateLinkCode(PAY_TYPE))
                        )
                ));
    }

    @Test
    @DisplayName("결제 취소 / 클래스 시작 3일 이내에 취소시 예외 발생")
    void cancel_payment_should_not_cancel_under_three_days() throws Exception {

        // given
        stubbingTeamPayment();
        stubbingCancelExceptionPayment();

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete(ORDER_URL, "TEAM", "1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer access-token")
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    private void stubbingCancelExceptionPayment() throws IamportResponseException, IOException {
        doThrow(new CanNotCancelOrderException())
                .when(paymentTeamMemberProcess).cancelProcess(any());
    }


    private OrderIdDto createOrderIdDto() {
        return OrderIdDto.ofComplete(1L);
    }

    private PaymentVerifyRequest createPaymentVerifyRequest() {
        return PaymentVerifyRequest.builder()
                .iamportUid("imp_12341234")
                .orderId(1L)
                .build();
    }

    private void stubbingPersonalPayment() {
        when(paymentServiceFactory.getProcess(any())).thenReturn(paymentPersonalProcess);
    }

    private void stubbingTeamPayment() {
        when(paymentServiceFactory.getProcess(any())).thenReturn(paymentTeamMemberProcess);
    }

    private PaymentRequest createPaymentRequest() {
        return PaymentRequest.builder()
                .lectureId(1L)
                .memberCouponId(1L)
                .rewardAmount(1000)
                .totalPrice(2000)
                .build();
    }
}
