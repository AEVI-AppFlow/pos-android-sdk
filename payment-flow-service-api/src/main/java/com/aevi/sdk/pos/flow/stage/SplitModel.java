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

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;

import static com.aevi.sdk.flow.service.ActivityHelper.ACTIVITY_REQUEST_KEY;

/**
 * Model for the split stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService#onPreFlow(PreFlowModel)} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 *
 * If no data has been augmented, calling {@link #sendResponse()} will send back an empty response informing FPS that no changes were made.
 */
public class SplitModel extends BaseStageModel {

    private final SplitRequest splitRequest;
    private final AmountsModifier amountsModifier;
    private final FlowResponse flowResponse;

    private SplitModel(Activity activity, SplitRequest splitRequest) {
        super(activity);
        this.splitRequest = splitRequest;
        this.amountsModifier = new AmountsModifier(splitRequest.getRemainingAmounts());
        this.flowResponse = new FlowResponse();
    }

    private SplitModel(ClientCommunicator clientCommunicator, SplitRequest splitRequest) {
        super(clientCommunicator);
        this.splitRequest = splitRequest;
        this.amountsModifier = new AmountsModifier(splitRequest.getRemainingAmounts());
        this.flowResponse = new FlowResponse();
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link BaseStageModel#processInActivity(Context, Class)},
     * or via the {@link ActivityProxyService}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link SplitModel}
     */
    public static SplitModel fromActivity(Activity activity) {
        String request = activity.getIntent().getStringExtra(ACTIVITY_REQUEST_KEY);
        return new SplitModel(activity, SplitRequest.fromJson(request));
    }

    /**
     * Create an instance from a service context.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request            The deserialised Payment provided as a string via {@link BaseApiService#processRequest(ClientCommunicator, String, String)}
     * @return An instance of {@link SplitModel}
     */
    public static SplitModel fromService(ClientCommunicator clientCommunicator, SplitRequest request) {
        return new SplitModel(clientCommunicator, request);
    }

    /**
     * Get the split request.
     *
     * @return The split request.
     */
    public SplitRequest getSplitRequest() {
        return splitRequest;
    }

    /**
     * Check whether the last transaction failed, meaning that the last split was not successful.
     *
     * This scenario needs to be handled appropriately with information to the merchant and option to re-try the split.
     *
     * @return True if last transaction failed, false otherwise
     */
    public boolean lastTransactionFailed() {
        return splitRequest.hasPreviousTransactions() && !splitRequest.getLastTransaction().hasProcessedRequestedAmounts() &&
                splitRequest.getLastTransaction().hasDeclinedResponses();
    }

    /**
     * Set the base amount to process for the next split transaction.
     *
     * If the payment contains a basket, using {@link #setBasketForNextTransaction(Basket)} is more suitable.
     *
     * @param baseAmount The base amount to process for the next transaction
     */
    public void setBaseAmountForNextTransaction(long baseAmount) {
        amountsModifier.updateBaseAmount(baseAmount);
    }

    /**
     * Set the basket to process for the next transaction.
     *
     * The total amount will be derived from the total basket value.
     *
     * @param basket The basket to process for the next transaction
     */
    public void setBasketForNextTransaction(Basket basket) {
        flowResponse.addBasket(basket);
        amountsModifier.updateBaseAmount(basket.getTotalBasketValue());
    }

    /**
     * Add request key/value pair data to the request.
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
    public final <T> void addRequestData(String key, T... values) {
        flowResponse.addAdditionalRequestData(key, values);
    }

    /**
     * Pay off a portion or the full requested amounts.
     *
     * The use cases for this involves the customer paying part or all of the amounts owed via means other than payment cards.
     * Examples are loyalty points, cash, etc.
     *
     * If this amount is less than the overall amount for the transaction, the remaining amount will be processed by the payment app.
     *
     * If this amount equals the overall (original or updated) amounts, the transaction will be considered fulfilled and completed.
     *
     * NOTE! This response only tracks one paid amounts - if this method is called more than once, any previous values will be overwritten.
     * It is up to the client to ensure that a consolidated Amounts object is constructed and provided here.
     *
     * @param amountsPaid   The amounts paid
     * @param paymentMethod The method of payment
     */
    public void setAmountsPaid(Amounts amountsPaid, String paymentMethod) {
        flowResponse.setAmountsPaid(amountsPaid, paymentMethod);
    }


    /**
     * Pay off a portion or the full requested amounts.
     *
     * The use cases for this involves the customer paying part or all of the amounts owed via means other than payment cards.
     * Examples are loyalty points, cash, etc.
     *
     * If this amount is less than the overall amount for the transaction, the remaining amount will be processed by the payment app.
     *
     * If this amount equals the overall (original or updated) amounts, the transaction will be considered fulfilled and completed.
     *
     * NOTE! This response only tracks one paid amounts - if this method is called more than once, any previous values will be overwritten.
     * It is up to the client to ensure that a consolidated Amounts object is constructed and provided here.
     *
     * @param amountsPaid       The amounts paid
     * @param paymentMethod     The method of payment
     * @param paymentReferences Payment references associated with the payment
     */
    public void setAmountsPaid(Amounts amountsPaid, String paymentMethod, AdditionalData paymentReferences) {
        flowResponse.setAmountsPaid(amountsPaid, paymentMethod);
        flowResponse.setPaymentReferences(paymentReferences);
    }

    /**
     * Cancel the flow.
     */
    public void cancelFlow() {
        flowResponse.setCancelTransaction(true);
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
        if (amountsModifier.hasModifications()) {
            flowResponse.updateRequestAmounts(amountsModifier.build());
        }
        return flowResponse;
    }

    /**
     * Send off the response.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void sendResponse() {
        doSendResponse(getFlowResponse().toJson());
    }

    @Override
    public String getRequestJson() {
        return splitRequest.toJson();
    }
}
