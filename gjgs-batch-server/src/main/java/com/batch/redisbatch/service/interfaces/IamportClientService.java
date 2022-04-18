package com.batch.redisbatch.service.interfaces;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import java.io.IOException;

public interface IamportClientService {

    IamportResponse<Payment> cancelPayment(String iamportUid) throws IamportResponseException, IOException;
}
