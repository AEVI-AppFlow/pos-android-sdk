package com.aevi.sdk.pos.flow.service;

import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.FlowServiceApi;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.TransactionRequest;

/**
 * Base class for post-card reading services.
 */
public abstract class BasePostCardService extends BaseApiService<TransactionRequest, FlowResponse> {

    public BasePostCardService() {
        super(TransactionRequest.class, FlowServiceApi.getApiVersion());
    }
}
