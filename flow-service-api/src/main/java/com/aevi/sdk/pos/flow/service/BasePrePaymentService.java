package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.util.ApiVersionProvider;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.TransactionRequest;

/**
 * Base class for pre-payment (aka pre-transaction) services.
 */
public abstract class BasePrePaymentService extends BaseApiService<TransactionRequest, FlowResponse> {

    private static final String API_PROPERTIES_FILE = "flow-service-api.properties";

    public BasePrePaymentService() {
        super(TransactionRequest.class, ApiVersionProvider.getApiVersion(API_PROPERTIES_FILE));
    }
}
