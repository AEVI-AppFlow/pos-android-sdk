package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.model.NoOpModel;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.FlowServiceApi;
import com.aevi.sdk.pos.flow.model.PaymentResponse;

/**
 * Base class for post-flow services.
 */
public abstract class BasePostFlowService extends BaseApiService<PaymentResponse, NoOpModel> {

    public BasePostFlowService() {
        super(PaymentResponse.class, FlowServiceApi.getApiVersion());
    }
}
