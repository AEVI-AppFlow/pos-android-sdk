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

package com.aevi.sdk.pos.flow.paymentservicesample;

import android.content.Context;
import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.pos.flow.model.TransactionResponse;
import com.aevi.sdk.pos.flow.paymentservicesample.ui.TokenisationActivity;
import com.aevi.sdk.pos.flow.paymentservicesample.util.InMemoryStore;

import static com.aevi.sdk.flow.constants.FlowServiceEventTypes.RESUME_USER_INTERFACE;
import static com.aevi.sdk.flow.constants.FlowTypes.FLOW_TYPE_REVERSAL;
import static com.aevi.sdk.flow.constants.FlowTypes.FLOW_TYPE_TOKENISATION;

public class GenericStageHandler {

    public static void handleGenericRequest(Context context, GenericStageModel genericStageModel) {
        Request request = genericStageModel.getRequest();
        switch (request.getRequestType()) {
            case FLOW_TYPE_REVERSAL:
                handleReversal(request, genericStageModel);
                break;
            case FLOW_TYPE_TOKENISATION:
                if (request.shouldProcessInBackground()) {
                    genericStageModel.sendResponse(new Response(request, false, "Can not perform tokenisation in the background"));
                    return;
                }
                genericStageModel.processInActivity(context, TokenisationActivity.class);
                genericStageModel.getEvents().subscribe(flowEvent -> {
                    switch (flowEvent.getType()) {
                        case RESUME_USER_INTERFACE:
                            genericStageModel.processInActivity(context, TokenisationActivity.class);
                            break;
                    }
                });
                break;
            default:
                genericStageModel.sendResponse(new Response(request, false, "Unsupported request: " + request.getRequestType()));
        }
    }

    private static void handleReversal(Request request, GenericStageModel genericStageModel) {
        String transactionId = request.getRequestData().getStringValue(AdditionalDataKeys.DATA_KEY_TRANSACTION_ID);
        TransactionResponse lastTransactionResponse = InMemoryStore.getInstance().getLastTransactionResponseGenerated();
        if (lastTransactionResponse != null && lastTransactionResponse.getId().equals(transactionId)) {
            genericStageModel.sendResponse(new Response(request, true, "Reversed transaction: " + transactionId));
        } else {
            genericStageModel.sendResponse(new Response(request, false, "Was unable to perform reversal"));
        }
    }
}
