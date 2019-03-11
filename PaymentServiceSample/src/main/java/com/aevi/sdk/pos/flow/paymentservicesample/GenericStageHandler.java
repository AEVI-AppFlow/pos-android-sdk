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
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.pos.flow.model.TransactionResponse;
import com.aevi.sdk.pos.flow.paymentservicesample.ui.TokenisationActivity;
import com.aevi.sdk.pos.flow.paymentservicesample.util.InMemoryStore;

import static com.aevi.sdk.flow.constants.FlowTypes.FLOW_TYPE_REVERSAL;
import static com.aevi.sdk.flow.constants.FlowTypes.FLOW_TYPE_TOKENISATION;
import static com.aevi.sdk.pos.flow.paymentservicesample.ui.TransactionProcessingActivity.INTERNAL_ID_KEY;

public class GenericStageHandler {

    public static void handleGenericRequest(Context context, GenericStageModel genericStageModel) {
        Request request = genericStageModel.getRequest();
        switch (request.getRequestType()) {
            case FLOW_TYPE_REVERSAL:
                handleReversal(request, genericStageModel);
                break;
            case FLOW_TYPE_TOKENISATION:
                genericStageModel.processInActivity(context, TokenisationActivity.class);
                break;
            default:
                genericStageModel.sendResponse(new Response(request, false, "Unsupported request: " + request.getRequestType()));
        }
    }

    private static void handleReversal(Request request, GenericStageModel genericStageModel) {
        String transactionId = request.getRequestData().getStringValue(INTERNAL_ID_KEY);
        TransactionResponse lastTransactionResponse = InMemoryStore.getInstance().getLastTransactionResponseGenerated();
        if (lastTransactionResponse != null && transactionId != null && transactionId.equals(lastTransactionResponse.getReferences().getStringValue(INTERNAL_ID_KEY))) {
            genericStageModel.sendResponse(new Response(request, true, "Reversed transaction: " + transactionId));
        } else {
            genericStageModel.sendResponse(new Response(request, false, "Was unable to perform reversal"));
        }
    }
}
