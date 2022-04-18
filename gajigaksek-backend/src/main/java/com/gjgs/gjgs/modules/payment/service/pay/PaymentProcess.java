package com.gjgs.gjgs.modules.payment.service.pay;

import com.gjgs.gjgs.modules.payment.dto.OrderIdDto;
import com.gjgs.gjgs.modules.payment.dto.PayType;
import com.gjgs.gjgs.modules.payment.dto.PaymentRequest;
import com.siot.IamportRestClient.exception.IamportResponseException;

import java.io.IOException;

public interface PaymentProcess {

    PayType getType();

    OrderIdDto payProcess(Long scheduleId, PaymentRequest paymentRequest);

    void cancelProcess(Long orderId) throws IamportResponseException, IOException;
}
