package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.util.ApiVersionProvider;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.Request;

/**
 * Base class for pre-flow services.
 */
public abstract class BasePreFlowService extends BaseApiService<Request, FlowResponse> {

    private static final String API_PROPERTIES_FILE = "flow-service-api.properties";

    public BasePreFlowService() {
        super(Request.class, ApiVersionProvider.getApiVersion(API_PROPERTIES_FILE));
    }
}
