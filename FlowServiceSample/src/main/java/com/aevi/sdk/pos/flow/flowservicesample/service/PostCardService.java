package com.aevi.sdk.pos.flow.flowservicesample.service;


import com.aevi.sdk.pos.flow.flowservicesample.ui.PostPaymentActivity;
import com.aevi.sdk.pos.flow.model.TransactionSummary;
import com.aevi.sdk.pos.flow.service.BasePostPaymentService;

public class PostCardService extends BasePostPaymentService {

    @Override
    protected void processRequest(String clientMessageId, TransactionSummary transactionSummary) {
        launchActivity(PostPaymentActivity.class, clientMessageId, transactionSummary);
    }

    @Override
    protected void finish(String clientMessageId) {
        finishLaunchedActivity(clientMessageId);
        finishWithNoResponse(clientMessageId);
    }
}
