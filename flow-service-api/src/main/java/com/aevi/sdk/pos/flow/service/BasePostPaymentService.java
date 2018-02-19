package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.util.ApiVersionProvider;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.TransactionSummary;

/**
 * Base class for post-payment (aka post-transaction) services.
 */
public abstract class BasePostPaymentService extends BaseApiService<TransactionSummary, FlowResponse> {

    private static final String API_PROPERTIES_FILE = "flow-service-api.properties";

    public BasePostPaymentService() {
        super(TransactionSummary.class, ApiVersionProvider.getApiVersion(API_PROPERTIES_FILE));
    }
}
