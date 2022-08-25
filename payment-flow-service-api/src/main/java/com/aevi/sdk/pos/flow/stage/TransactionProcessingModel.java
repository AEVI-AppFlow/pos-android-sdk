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
import android.content.Context;

import androidx.annotation.NonNull;

import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.model.TransactionResponse;
import com.aevi.sdk.pos.flow.model.TransactionResponseBuilder;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;

/**
 * Model for the transaction-processing stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 *
 * The outcome of the transaction must be set in the builder before {@link #sendResponse()} is called.
 *
 * Note that skipping this stage is not allowed - a valid response with an outcome must be set.
 */
public class TransactionProcessingModel extends BaseStageModel {

    private final TransactionRequest transactionRequest;
    private final TransactionResponseBuilder transactionResponseBuilder;

    private TransactionProcessingModel(Activity activity, TransactionRequest request) {
        super(activity);
        this.transactionRequest = request;
        this.transactionResponseBuilder = new TransactionResponseBuilder(transactionRequest.getId());
    }

    private TransactionProcessingModel(ClientCommunicator clientCommunicator, TransactionRequest request, InternalData senderInternalData) {
        super(clientCommunicator, senderInternalData);
        this.transactionRequest = request;
        this.transactionResponseBuilder = new TransactionResponseBuilder(transactionRequest.getId());
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link BaseStageModel#processInActivity(Context, Class)},
     * or via the {@link ActivityProxyService}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link TransactionProcessingModel}
     */
    @NonNull
    public static TransactionProcessingModel fromActivity(Activity activity) {
        return new TransactionProcessingModel(activity, TransactionRequest.fromJson(getActivityRequestJson(activity)));
    }

    /**
     * Create an instance from a service context.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request            The deserialised TransactionRequest
     * @param senderInternalData The internal data of the app that started this stage
     * @return An instance of {@link TransactionProcessingModel}
     */
    @NonNull
    public static TransactionProcessingModel fromService(ClientCommunicator clientCommunicator, TransactionRequest request,
                                                         InternalData senderInternalData) {
        return new TransactionProcessingModel(clientCommunicator, request, senderInternalData);
    }

    /**
     * Get the transaction request.
     *
     * @return The transaction request.
     */
    @NonNull
    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * Get the transaction response builder to build the response from.
     *
     * @return The {@link TransactionResponseBuilder}
     */
    @NonNull
    public TransactionResponseBuilder getTransactionResponseBuilder() {
        return transactionResponseBuilder;
    }

    /**
     * Get the transaction response built from the builder.
     *
     * Note that this may throw exception if the mandatory fields have not yet been set via the builder.
     *
     * @return The transaction response
     */
    @NonNull
    public TransactionResponse getTransactionResponse() {
        return transactionResponseBuilder.build();
    }

    /**
     * Send off the response.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void sendResponse() {
        doSendResponse(getTransactionResponse().toJson());
    }

    @Override
    @NonNull
    public String getRequestJson() {
        return transactionRequest.toJson();
    }
}
