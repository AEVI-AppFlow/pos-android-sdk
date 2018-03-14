package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.FlowServiceApi;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.Payment;

/**
 * Base class for pre-flow services.
 */
public abstract class BasePreFlowService extends BaseApiService<Payment, FlowResponse> {

    public BasePreFlowService() {
        super(Payment.class, FlowServiceApi.getApiVersion());
    }
}
