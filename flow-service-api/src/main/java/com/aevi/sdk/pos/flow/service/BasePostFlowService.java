package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.model.NoOpModel;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.util.ApiVersionProvider;
import com.aevi.sdk.pos.flow.model.PaymentResponse;

/**
 * Base class for post-flow services.
 */
public abstract class BasePostFlowService extends BaseApiService<PaymentResponse, NoOpModel> {

    private static final String API_PROPERTIES_FILE = "flow-service-api.properties";

    public BasePostFlowService() {
        super(PaymentResponse.class, ApiVersionProvider.getApiVersion(API_PROPERTIES_FILE));
    }
}
