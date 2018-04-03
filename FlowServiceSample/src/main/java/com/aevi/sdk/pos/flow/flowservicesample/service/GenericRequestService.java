package com.aevi.sdk.pos.flow.flowservicesample.service;

import com.aevi.sdk.pos.flow.flowservicesample.ui.LoyaltyBalanceActivity;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseRequestService;

public class GenericRequestService extends BaseRequestService {

    public static final String SHOW_LOYALTY_POINTS_REQUEST = "showLoyaltyPointsBalance";

    @Override
    protected void processRequest(String clientMessageId, Request request) {
        if (request.getRequestType().equals(SHOW_LOYALTY_POINTS_REQUEST)) {
            launchActivity(LoyaltyBalanceActivity.class, clientMessageId, request);
        } else {
            finishWithResponse(clientMessageId, new Response(request, false, "Unsupported request type"));
        }
    }

    @Override
    protected void finish(String clientMessageId) {

    }
}
