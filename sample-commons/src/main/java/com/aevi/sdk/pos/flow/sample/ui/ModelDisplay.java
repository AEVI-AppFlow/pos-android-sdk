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

    void showCard(Card card);

    void showTransactionResponse(TransactionResponse transactionResponse);

    void showPaymentResponse(PaymentResponse paymentResponse);

    void showResponse(Response response);

    void showFlowResponse(FlowResponse flowResponse);

    void showCustomData(AdditionalData additionalData);
}
