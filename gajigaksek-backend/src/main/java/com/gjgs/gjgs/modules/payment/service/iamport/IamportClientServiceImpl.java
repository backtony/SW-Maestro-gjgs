package com.gjgs.gjgs.modules.payment.service.iamport;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class IamportClientServiceImpl implements IamportClientService {

    private final IamportClient iamportClient;

    @Override
    public IamportResponse<Payment> verifyPayment(String iamportUid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(iamportUid);
    }

    @Override
    public IamportResponse<Payment> cancelPayment(String iamportUid) throws IamportResponseException, IOException {
        return iamportClient.cancelPaymentByImpUid(new CancelData(iamportUid, true));
    }
}
