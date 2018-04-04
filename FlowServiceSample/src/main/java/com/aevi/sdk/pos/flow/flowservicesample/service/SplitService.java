package com.aevi.sdk.pos.flow.flowservicesample.service;


import com.aevi.sdk.pos.flow.flowservicesample.ui.SplitActivity;
import com.aevi.sdk.pos.flow.model.SplitRequest;
import com.aevi.sdk.pos.flow.service.BaseSplitService;

public class SplitService extends BaseSplitService {

    @Override
    protected void processRequest(String clientMessageId, SplitRequest splitRequest) {
        launchActivity(SplitActivity.class, clientMessageId, splitRequest);
    }

    @Override
    protected void finish(String clientMessageId) {
        finishLaunchedActivity(clientMessageId);
        finishWithNoResponse(clientMessageId);
    }
}
