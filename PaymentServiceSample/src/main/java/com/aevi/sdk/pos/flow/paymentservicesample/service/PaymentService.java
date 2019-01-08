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

import com.aevi.sdk.flow.stage.GenericStageModel;
import com.aevi.sdk.pos.flow.paymentservicesample.GenericStageHandler;
import com.aevi.sdk.pos.flow.paymentservicesample.ui.PaymentCardReadingActivity;
import com.aevi.sdk.pos.flow.paymentservicesample.ui.TransactionProcessingActivity;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;
import com.aevi.sdk.pos.flow.stage.CardReadingModel;
import com.aevi.sdk.pos.flow.stage.TransactionProcessingModel;

import static com.aevi.sdk.flow.model.AuditEntry.AuditSeverity.INFO;

/**
 * This service illustrates an explicit service implementation which gives full control over how and where to process the requests.
 *
 * See the FlowServiceSample for illustrations on how to automatically proxy a request to an activity.
 */
public class PaymentService extends BasePaymentFlowService {

    @Override
    protected void onPaymentCardReading(CardReadingModel model) {
        model.addAuditEntry(INFO, "Hello from PaymentService onPaymentCardReading");
        model.processInActivity(getBaseContext(), PaymentCardReadingActivity.class);
    }

    @Override
    protected void onTransactionProcessing(TransactionProcessingModel model) {
        model.addAuditEntry(INFO, "Hello from PaymentService onTransactionProcessing");
        model.processInActivity(getBaseContext(), TransactionProcessingActivity.class);
    }

    @Override
    protected void onGeneric(GenericStageModel model) {
        model.addAuditEntry(INFO, "Hello from PaymentService onGeneric");
        GenericStageHandler.handleGenericRequest(getBaseContext(), model);
    }
}
