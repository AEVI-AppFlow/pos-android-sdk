package com.aevi.sdk.payment.service.sample.service;


import com.aevi.sdk.payment.service.sample.ui.SelectPaymentCardActivity;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.service.BasePaymentCardReadingService;

public class PaymentCardReadingService extends BasePaymentCardReadingService {

    @Override
    protected void processRequest(String clientMessageId, TransactionRequest transactionRequest) {
        launchActivity(SelectPaymentCardActivity.class, clientMessageId, transactionRequest);
    }

    @Override
    protected void finish(String clientMessageId) {
        finishLaunchedActivity(clientMessageId);
        finishWithNoResponse(clientMessageId);
    }
}
