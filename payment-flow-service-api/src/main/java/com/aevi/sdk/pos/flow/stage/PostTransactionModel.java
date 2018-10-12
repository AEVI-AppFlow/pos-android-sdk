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

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.TransactionSummary;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;

/**
 * Model for the post-transaction stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService#onPreFlow(PreFlowModel)} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 */
public class PostTransactionModel extends BaseStageModel {

    private final TransactionSummary transactionSummary;
    private final FlowResponse flowResponse;

    private PostTransactionModel(Activity activity, BaseApiService service, String clientMessageId, TransactionSummary transactionSummary) {
        super(activity, service, clientMessageId);
        this.transactionSummary = transactionSummary;
        this.flowResponse = new FlowResponse();
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link #processInActivity(Class)}, {@link BaseApiService#launchActivity(Class, String, String)}
     * or via the {@link ActivityProxyService}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link PostTransactionModel}
     */
    public static PostTransactionModel fromActivity(Activity activity) {
        String request = activity.getIntent().getStringExtra(BaseApiService.ACTIVITY_REQUEST_KEY);
        return new PostTransactionModel(activity, null, null, TransactionSummary.fromJson(request));
    }

    /**
     * Create an instance from a service context.
     *
     * @param service         The service instance
     * @param clientMessageId The client message id provided via {@link BaseApiService#processRequest(String, String, String)}
     * @param request         The deserialised Payment provided as a string via {@link BaseApiService#processRequest(String, String, String)}
     * @return An instance of {@link PostTransactionModel}
     */
    public static PostTransactionModel fromService(BaseApiService service, String clientMessageId, TransactionSummary request) {
        return new PostTransactionModel(null, service, clientMessageId, request);
    }

    /**
     * Get the transaction summary.
     *
     * @return The transaction summary
     */
    public TransactionSummary getTransactionSummary() {
        return transactionSummary;
    }

    /**
     * Add payment references to the transaction.
     *
     * The value of this can be any arbitrary data or collection of data.
     *
     * See {@link AdditionalData#addData(String, Object[])} for more info.
     *
     * @param key    The key to use for this data
     * @param values A var-args input of values associated with the key
     * @param <T>    The type of object this data is an array of
     */
    @SafeVarargs
    public final <T> void addReferences(String key, T... values) {
        flowResponse.addAdditionalRequestData(key, values);
    }

    /**
     * Get the flow response that is created from this model.
     *
     * Note that there is rarely any need to interact with this object directly, but there are cases where reading and/or updating data in the
     * response object directly is useful.
     *
     * @return The flow response
     */
    public FlowResponse getFlowResponse() {
        return flowResponse;
    }

    /**
     * Send off the response.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void sendResponse() {
        doSendResponse(flowResponse.toJson());
    }

    @Override
    protected String getRequestJson() {
        return transactionSummary.toJson();
    }
}
