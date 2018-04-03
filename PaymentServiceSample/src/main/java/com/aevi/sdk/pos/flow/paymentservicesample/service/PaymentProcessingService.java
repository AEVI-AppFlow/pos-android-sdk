package com.aevi.sdk.pos.flow.paymentservicesample.service;


import com.aevi.sdk.pos.flow.paymentservicesample.ui.PaymentResponseBuilderActivity;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.service.BasePaymentProcessingService;

public class PaymentProcessingService extends BasePaymentProcessingService {

    @Override
    protected void processRequest(String clientMessageId, TransactionRequest transactionRequest) {
        launchActivity(PaymentResponseBuilderActivity.class, clientMessageId, transactionRequest);
    }

    @Override
    protected void finish(String clientMessageId) {
        finishLaunchedActivity(clientMessageId);
        finishWithNoResponse(clientMessageId);
    }
}
