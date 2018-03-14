package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.FlowServiceApi;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.TransactionRequest;

/**
 * Base class for pre-payment (aka pre-transaction) services.
 */
public abstract class BasePrePaymentService extends BaseApiService<TransactionRequest, FlowResponse> {

    public BasePrePaymentService() {
        super(TransactionRequest.class, FlowServiceApi.getApiVersion());
    }
}
