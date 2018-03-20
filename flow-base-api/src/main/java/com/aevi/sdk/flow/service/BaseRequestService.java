package com.aevi.sdk.flow.service;


import com.aevi.sdk.flow.FlowApi;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;

/**
 * Base service for handling generic requests defined by a request type and associated bespoke data.
 *
 * See documentation for examples and reference types and data.
 */
public abstract class BaseRequestService extends BaseApiService<Request, Response> {

    public BaseRequestService() {
        super(Request.class, FlowApi.getApiVersion());
    }
}
