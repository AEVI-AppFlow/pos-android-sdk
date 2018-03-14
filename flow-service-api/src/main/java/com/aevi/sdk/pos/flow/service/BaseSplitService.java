package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.FlowServiceApi;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.SplitRequest;

/**
 * Base class for split services.
 */
public abstract class BaseSplitService extends BaseApiService<SplitRequest, FlowResponse> {

    public BaseSplitService() {
        super(SplitRequest.class, FlowServiceApi.getApiVersion());
    }
}
