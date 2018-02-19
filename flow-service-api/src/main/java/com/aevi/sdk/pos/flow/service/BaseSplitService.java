package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.util.ApiVersionProvider;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.SplitRequest;

/**
 * Base class for split services.
 */
public abstract class BaseSplitService extends BaseApiService<SplitRequest, FlowResponse> {

    private static final String API_PROPERTIES_FILE = "flow-service-api.properties";

    public BaseSplitService() {
        super(SplitRequest.class, ApiVersionProvider.getApiVersion(API_PROPERTIES_FILE));
    }
}
