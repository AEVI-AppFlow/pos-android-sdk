package com.aevi.sdk.pos.flow.paymentservicesample.service;


import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.constants.FinancialRequestTypes;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseRequestService;
import com.aevi.sdk.pos.flow.paymentservicesample.ui.SelectTokenActivity;
import com.aevi.sdk.pos.flow.paymentservicesample.util.InMemoryStore;
import com.aevi.sdk.pos.flow.model.TransactionResponse;

public class FinancialRequestService extends BaseRequestService {

    @Override
    protected void processRequest(String clientMessageId, Request request) {

        switch (request.getRequestType()) {
            case FinancialRequestTypes.REVERSAL:
                handleReversal(clientMessageId, request);
                break;
            case FinancialRequestTypes.TOKENISATION:
                launchActivity(SelectTokenActivity.class, clientMessageId, request);
                break;
            case FinancialRequestTypes.RESPONSE_REDELIVERY:
                handleResponseRedelivery(clientMessageId, request);
                break;
            default:
                finishWithResponse(clientMessageId, new Response(request, false, "Unsupported request: " + request.getRequestType()));
        }

    }

    private void handleReversal(String clientMessageId, Request request) {
        String transactionId = request.getRequestData().getStringValue(AdditionalDataKeys.DATA_KEY_TRANSACTION_ID);
        TransactionResponse lastTransactionResponse = InMemoryStore.getInstance().getLastTransactionResponseGenerated();
        if (transactionId != null && lastTransactionResponse != null && lastTransactionResponse.getId().equals(transactionId)) {
            finishWithResponse(clientMessageId, new Response(request, true, "Reversed transaction: " + transactionId));
        } else {
            finishWithResponse(clientMessageId, new Response(request, false, "Was unable to perform reversal"));
        }
    }

    private void handleResponseRedelivery(String clientMessageId, Request request) {
        String transactionId = request.getRequestData().getStringValue(AdditionalDataKeys.DATA_KEY_TRANSACTION_ID);
        TransactionResponse lastTransactionResponse = InMemoryStore.getInstance().getLastTransactionResponseGenerated();
        if (transactionId != null && lastTransactionResponse != null && lastTransactionResponse.getId().equals(transactionId)) {
            Response response = new Response(request, true, "Response redelivery successful");
            request.addAdditionalData(AdditionalDataKeys.DATA_KEY_TRANSACTION_RESPONSE, lastTransactionResponse);
            finishWithResponse(clientMessageId, response);
        } else {
            finishWithResponse(clientMessageId, new Response(request, false, "Was unable to redeliver response"));
        }
    }

    @Override
    protected void finish(String clientMessageId) {
        finishLaunchedActivity(clientMessageId);
        finishWithNoResponse(clientMessageId);
    }
}
