package com.aevi.flowservicesample.service;


import com.aevi.flowservicesample.ui.PostFlowActivity;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.service.BasePostFlowService;

public class PostFlowService extends BasePostFlowService {

    @Override
    protected void processRequest(String clientMessageId, PaymentResponse response) {
        launchActivity(PostFlowActivity.class, clientMessageId, response);
    }

    @Override
    protected void finish(String clientMessageId) {
        finishLaunchedActivity(clientMessageId);
        finishWithNoResponse(clientMessageId);
    }
}
