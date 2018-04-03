package com.aevi.flowservicesample.service;


import com.aevi.flowservicesample.ui.PreFlowActivity;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.service.BasePreFlowService;

public class PreFlowService extends BasePreFlowService {

    @Override
    protected void processRequest(String clientMessageId, Payment request) {
        launchActivity(PreFlowActivity.class, clientMessageId, request);
    }

    @Override
    protected void finish(String clientMessageId) {
        finishLaunchedActivity(clientMessageId);
        finishWithNoResponse(clientMessageId);
    }
}
