package com.aevi.sdk.pos.flow.sample.ui;


import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.pos.flow.model.*;

public interface ModelDisplay {

    void showTitle(boolean show);

    void showPayment(Payment payment);

    void showRequest(Request request);

    void showTransactionRequest(TransactionRequest transactionRequest);

    void showSplitRequest(SplitRequest splitRequest);

    void showTransactionSummary(TransactionSummary transactionSummary);

    void showCardResponse(CardResponse cardResponse);

    void showTransactionResponse(TransactionResponse transactionResponse);

    void showPaymentResponse(PaymentResponse paymentResponse);

    void showResponse(Response response);

    void showFlowResponse(FlowResponse flowResponse);

    void showCustomData(AdditionalData additionalData);
}
