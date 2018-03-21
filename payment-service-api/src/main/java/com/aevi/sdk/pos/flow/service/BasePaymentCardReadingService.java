package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.PaymentServiceApi;
import com.aevi.sdk.pos.flow.model.CardResponse;
import com.aevi.sdk.pos.flow.model.TransactionRequest;

/**
 * Base service to extend for payment services that wish to support the separate flow card reading step.
 */
public abstract class BasePaymentCardReadingService extends BaseApiService<TransactionRequest, CardResponse> {

    public BasePaymentCardReadingService() {
        super(TransactionRequest.class, PaymentServiceApi.getApiVersion());
    }
}
