/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aevi.sdk.pos.flow.paymentservicesample.service;


import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.service.BaseRequestService;
import com.aevi.sdk.pos.flow.model.TransactionResponse;
import com.aevi.sdk.pos.flow.paymentservicesample.ui.SelectTokenActivity;
import com.aevi.sdk.pos.flow.paymentservicesample.util.InMemoryStore;

import static com.aevi.sdk.flow.constants.FlowTypes.*;

public class FinancialRequestService extends BaseRequestService {

    @Override
    protected void processRequest(String clientMessageId, Request request) {

        switch (request.getRequestType()) {
            case FLOW_TYPE_REVERSAL:
                handleReversal(clientMessageId, request);
                break;
            case FLOW_TYPE_TOKENISATION:
                launchActivity(SelectTokenActivity.class, clientMessageId, request);
                break;
            case FLOW_TYPE_RESPONSE_REDELIVERY:
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
