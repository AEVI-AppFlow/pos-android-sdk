package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.util.ApiVersionProvider;
import com.aevi.sdk.pos.flow.model.CardResponse;
import com.aevi.sdk.pos.flow.model.TransactionRequest;

public abstract class BasePaymentCardReadingService extends BaseApiService<TransactionRequest, CardResponse> {

    public BasePaymentCardReadingService() {
        super(TransactionRequest.class, ApiVersionProvider.getApiVersion(ApiProperties.API_PROPERTIES_FILE));
    }
}
