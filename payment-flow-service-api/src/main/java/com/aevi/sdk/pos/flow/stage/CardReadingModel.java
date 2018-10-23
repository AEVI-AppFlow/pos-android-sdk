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


package com.aevi.sdk.pos.flow.stage;

import android.app.Activity;

import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.model.Card;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.model.TransactionResponseBuilder;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;

import static com.aevi.sdk.flow.service.ActivityHelper.ACTIVITY_REQUEST_KEY;

/**
 * Model for the card-reading stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService#onPreFlow(PreFlowModel)} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 */
public class CardReadingModel extends BaseStageModel {

    private final TransactionRequest transactionRequest;
    private final TransactionResponseBuilder transactionResponseBuilder;

    private CardReadingModel(Activity activity, TransactionRequest request) {
        super(activity);
        this.transactionRequest = request;
        this.transactionResponseBuilder = new TransactionResponseBuilder(transactionRequest.getId());
    }

    private CardReadingModel(ClientCommunicator clientCommunicator, TransactionRequest request) {
        super(clientCommunicator);
        this.transactionRequest = request;
        this.transactionResponseBuilder = new TransactionResponseBuilder(transactionRequest.getId());
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link #processInActivity(Class)}, {@link BaseApiService#launchActivity(Class, String, String)}
     * or via the {@link ActivityProxyService}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link CardReadingModel}
     */
    public static CardReadingModel fromActivity(Activity activity) {
        String request = activity.getIntent().getStringExtra(ACTIVITY_REQUEST_KEY);
        return new CardReadingModel(activity, TransactionRequest.fromJson(request));
    }

    /**
     * Create an instance from a service context.
     *
     * @param service         The service instance
     * @param clientMessageId The client message id provided via {@link BaseApiService#processRequest(String, String, String)}
     * @param request         The deserialised Payment provided as a string via {@link BaseApiService#processRequest(String, String, String)}
     * @return An instance of {@link CardReadingModel}
     */
    public static CardReadingModel fromService(ClientCommunicator clientCommunicator, TransactionRequest request) {
        return new CardReadingModel(clientCommunicator, request);
    }

    /**
     * Get the transaction request.
     *
     * @return The transaction request
     */
    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * Approve the card reading with a valid card instance.
     *
     * @param card The card details
     */
    public void approveWithCard(Card card) {
        transactionResponseBuilder.approve();
        transactionResponseBuilder.withCard(card);
    }

    /**
     * Call to skip the card reading stage.
     */
    public void skipCardReading() {
        transactionResponseBuilder.approve();
    }

    /**
     * Decline the transaction due to invalid card, cancellation, etc.
     *
     * @param message The decline message
     */
    public void declineTransaction(String message) {
        declineTransaction(message, null);
    }

    /**
     * Decline the transaction due to invalid card, cancellation, etc.
     *
     * @param message      The decline message
     * @param responseCode The response code
     */
    public void declineTransaction(String message, String responseCode) {
        transactionResponseBuilder.decline(message);
        transactionResponseBuilder.withResponseCode(responseCode);
    }

    /**
     * Send off the response.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void sendResponse() {
        doSendResponse(transactionResponseBuilder.build().toJson());
    }

    @Override
    public String getRequestJson() {
        return transactionRequest.toJson();
    }
}
