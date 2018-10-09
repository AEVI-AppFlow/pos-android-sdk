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
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.model.TransactionResponse;
import com.aevi.sdk.pos.flow.model.TransactionResponseBuilder;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;

/**
 * Model for the transaction-processing stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService#onPreFlow(PreFlowModel)} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 */
public class TransactionProcessingModel extends BaseStageModel {

    private final TransactionRequest transactionRequest;
    private final TransactionResponseBuilder transactionResponseBuilder;

    private TransactionProcessingModel(Activity activity, BaseApiService service, String clientMessageId, TransactionRequest request) {
        super(activity, service, clientMessageId);
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
     * @return An instance of {@link TransactionProcessingModel}
     */
    public static TransactionProcessingModel fromActivity(Activity activity) {
        String request = activity.getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY);
        return new TransactionProcessingModel(activity, null, null, TransactionRequest.fromJson(request));
    }

    /**
     * Create an instance from a service context.
     *
     * @param service         The service instance
     * @param clientMessageId The client message id provided via {@link BaseApiService#processRequest(String, String, String)}
     * @param request         The deserialised Payment provided as a string via {@link BaseApiService#processRequest(String, String, String)}
     * @return An instance of {@link TransactionProcessingModel}
     */
    public static TransactionProcessingModel fromService(BaseApiService service, String clientMessageId, TransactionRequest request) {
        return new TransactionProcessingModel(null, service, clientMessageId, request);
    }

    /**
     * Get the transaction request.
     *
     * @return The transaction request.
     */
    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * Get the transaction response builder to build the response from.
     *
     * @return The {@link TransactionResponseBuilder}
     */
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
    protected String getRequestJson() {
        return transactionRequest.toJson();
    }
}
