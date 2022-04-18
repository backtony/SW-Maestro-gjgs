package com.gjgs.gjgs.modules.payment.service.iamport;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import java.io.IOException;

public interface IamportClientService {

    IamportResponse<Payment> verifyPayment(String iamportUid) throws IamportResponseException, IOException;

    IamportResponse<Payment> cancelPayment(String iamportUid) throws IamportResponseException, IOException;
}
