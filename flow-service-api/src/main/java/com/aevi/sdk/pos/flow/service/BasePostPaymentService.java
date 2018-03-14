package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.FlowServiceApi;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.TransactionSummary;

/**
 * Base class for post-payment (aka post-transaction) services.
 */
public abstract class BasePostPaymentService extends BaseApiService<TransactionSummary, FlowResponse> {

    public BasePostPaymentService() {
        super(TransactionSummary.class, FlowServiceApi.getApiVersion());
    }
}
