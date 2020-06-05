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

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.pos.flow.model.events.ProgressMessage;
import com.aevi.sdk.pos.flow.paymentservicesample.GenericStageHandler;
import com.aevi.sdk.pos.flow.paymentservicesample.R;
import com.aevi.sdk.pos.flow.paymentservicesample.ui.PaymentCardReadingActivity;
import com.aevi.sdk.pos.flow.paymentservicesample.ui.TransactionProcessingActivity;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;
import com.aevi.sdk.pos.flow.stage.CardReadingModel;
import com.aevi.sdk.pos.flow.stage.TransactionProcessingModel;

import static com.aevi.sdk.flow.constants.FlowServiceEventDataKeys.REJECTED_REASON;
import static com.aevi.sdk.flow.constants.FlowServiceEventTypes.*;
import static com.aevi.sdk.flow.constants.events.FlowEventTypes.EVENT_PROGRESS_MESSAGE;
import static com.aevi.sdk.flow.model.AuditEntry.AuditSeverity.INFO;

/**
 * This service illustrates an explicit service implementation which gives full control over how and where to process the requests.
 *
 * See the FlowServiceSample for illustrations on how to automatically proxy a request to an activity.
 */
public class PaymentService extends BasePaymentFlowService {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    protected void onPaymentCardReading(CardReadingModel model) {
        model.addAuditEntry(INFO, "Hello from PaymentService onPaymentCardReading");
        model.processInActivity(getBaseContext(), PaymentCardReadingActivity.class);
        sendClientMessage(model, getString(R.string.message_payment_card_reading));
        subscribeToFlowServiceEvents(model, PaymentCardReadingActivity.class);
    }

    @Override
    protected void onTransactionProcessing(TransactionProcessingModel model) {
        model.addAuditEntry(INFO, "Hello from PaymentService onTransactionProcessing");
        model.processInActivity(getBaseContext(), TransactionProcessingActivity.class);
        sendClientMessage(model, getString(R.string.message_processing_transaction));
        subscribeToFlowServiceEvents(model, TransactionProcessingActivity.class);
    }

    @Override
    protected void onGeneric(GenericStageModel model) {
        model.addAuditEntry(INFO, "Hello from PaymentService onGeneric");
        GenericStageHandler.handleGenericRequest(getBaseContext(), model);
    }

    protected void subscribeToFlowServiceEvents(BaseStageModel model, Class<? extends Activity> activityToRestart) {
        model.getEvents().subscribe(event -> {
            switch (event.getType()) {
                case RESUME_USER_INTERFACE:
                    // In this sample, we simply restart the activity as it contains no state
                    model.processInActivity(getBaseContext(), activityToRestart);
                    break;
                case RESPONSE_REJECTED:
                    String rejectReason = event.getData().getStringValue(REJECTED_REASON);
                    Log.w("PaymentServiceSample", "Response rejected: " + rejectReason);
                    break;
                default:
                    Log.i("PaymentServiceSample", "Received flow service event: " + event.getType());
                    break;
            }
        });
    }

    private void sendClientMessage(BaseStageModel model, String message) {
        FlowEvent flowEvent = new FlowEvent(EVENT_PROGRESS_MESSAGE, new ProgressMessage(message));
        model.sendEvent(flowEvent);
    }

}
