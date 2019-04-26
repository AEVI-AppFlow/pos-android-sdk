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
import android.support.annotation.NonNull;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;

import static com.aevi.sdk.flow.util.Preconditions.*;

/**
 * Model for the split stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 *
 * If data has been augmented, {@link #sendResponse()} must be called for these changes to be applied. If called with no changes, it has the same
 * effect as calling {@link #skip()}.
 *
 * For cancelling the flow, call {@link #cancelFlow()}.
 *
 * If no changes are required, call {@link #skip()}.
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

    private SplitModel(ClientCommunicator clientCommunicator, SplitRequest splitRequest, InternalData senderInternalData) {
        super(clientCommunicator, senderInternalData);
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
    @NonNull
    public static SplitModel fromActivity(Activity activity) {
        return new SplitModel(activity, SplitRequest.fromJson(getActivityRequestJson(activity)));
    }

    /**
     * Create an instance from a service context.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request            The deserialised SplitRequest
     * @param senderInternalData The internal data of the app that started this stage
     * @return An instance of {@link SplitModel}
     */
    @NonNull
    public static SplitModel fromService(ClientCommunicator clientCommunicator, SplitRequest request, InternalData senderInternalData) {
        return new SplitModel(clientCommunicator, request, senderInternalData);
    }

    /**
     * Get the split request.
     *
     * @return The split request.
     */
    @NonNull
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
     * @throws IllegalArgumentException If amount is negative
     */
    public void setBaseAmountForNextTransaction(long baseAmount) {
        checkNotNegative(baseAmount, "Amount must not be negative");
        amountsModifier.updateBaseAmount(baseAmount);
    }

    /**
     * Set the basket to process for the next transaction.
     *
     * The total amount will be derived from the total basket value. Calling {@link #setBaseAmountForNextTransaction(long)} is not required.
     *
     * @param basket The basket to process for the next transaction
     * @throws IllegalArgumentException If basket is not set up correctly
     */
    public void setBasketForNextTransaction(Basket basket) {
        checkNotNull(basket, "Basket must not be null");
        checkNotEmpty(basket.getBasketItems(), "At least one basket item must be set");
        if (basket.getTotalBasketValue() < 0) {
            throw new IllegalArgumentException("Total basket value must be greater than or equal zero");
        }
        flowResponse.addNewBasket(basket);
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
     * @throws IllegalArgumentException If key or values are not set
     */
    @SafeVarargs
    public final <T> void addRequestData(String key, T... values) {
        checkNotNull(key, "Key must be set");
        checkNotEmpty(values, "At least one value must be provided");
        flowResponse.addAdditionalRequestData(key, values);
    }

    /**
     * Pay off a portion or the full requested amounts.
     *
     * The use cases for this involves the customer paying part or all of the amounts owed via means other than payment cards.
     * Examples are loyalty points, cash, etc.
     *
     * Note that paying off the "additional amounts" is not supported at this time - only the base amount. The paid amounts must only set
     * the base amount value, and it must not exceed the request base amount value. If it exceeds it, or there are additional amounts set,
     * an exception will be thrown.
     *
     * If there is remaining base amounts to pay after this, the remaining amount will be processed by the payment app.
     *
     * If the request contains no additional amounts and this payment equals the requested amounts, the transaction will be considered fulfilled and completed.
     *
     * NOTE! This response only tracks one paid amounts - if this method is called more than once, any previous values will be overwritten.
     * It is up to the client to ensure that a consolidated Amounts object is constructed and provided here.
     *
     * @param amountsPaid   The amounts paid
     * @param paymentMethod The method of payment
     * @throws IllegalArgumentException If either argument is null or the values violate the restrictions mentioned above
     */
    public void setAmountsPaid(Amounts amountsPaid, String paymentMethod) {
        setAmountsPaid(amountsPaid, paymentMethod, null);
    }

    /**
     * Pay off a portion or the full requested amounts.
     *
     * The use cases for this involves the customer paying part or all of the amounts owed via means other than payment cards.
     * Examples are loyalty points, cash, etc.
     *
     * Note that paying off the "additional amounts" is not supported at this time - only the base amount. The paid amounts must only set
     * the base amount value, and it must not exceed the request base amount value. If it exceeds it, or there are additional amounts set,
     * an exception will be thrown.
     *
     * If there is remaining base amounts to pay after this, the remaining amount will be processed by the payment app.
     *
     * If the request contains no additional amounts and this payment equals the requested amounts, the transaction will be considered fulfilled and completed.
     *
     * NOTE! This response only tracks one paid amounts - if this method is called more than once, any previous values will be overwritten.
     * It is up to the client to ensure that a consolidated Amounts object is constructed and provided here.
     *
     * @param amountsPaid       The amounts paid
     * @param paymentMethod     The method of payment
     * @param paymentReferences Payment references associated with the payment
     * @throws IllegalArgumentException If either argument is null or the values violate the restrictions mentioned above
     */
    public void setAmountsPaid(Amounts amountsPaid, String paymentMethod, AdditionalData paymentReferences) {
        checkNotNull(amountsPaid, "Amounts paid must be set");
        checkNotEmpty(paymentMethod, "Payment method must be set");
        if (amountsPaid.getBaseAmountValue() > splitRequest.getRemainingAmounts().getBaseAmountValue()) {
            throw new IllegalArgumentException("Paid base amount value can not exceed the request base amount value");
        }
        if (!amountsPaid.getAdditionalAmounts().isEmpty()) {
            throw new IllegalArgumentException("Paid additional amounts is not supported at the moment - set base amount only");
        }
        if (amountsPaid.getTotalAmountValue() > splitRequest.getRemainingAmounts().getTotalAmountValue()) {
            throw new IllegalArgumentException("Paid amounts can not exceed requested amounts");
        }
        if (!amountsPaid.getCurrency().equals(splitRequest.getRemainingAmounts().getCurrency())) {
            throw new IllegalArgumentException("Paid currency does not match request currency");
        }
        flowResponse.setAmountsPaid(amountsPaid, paymentMethod);
        flowResponse.setPaymentReferences(paymentReferences);
    }

    /**
     * Cancel the flow and send off the response.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void cancelFlow() {
        flowResponse.setCancelTransaction(true);
        sendResponse();
    }

    /**
     * Get the flow response that is created from this model.
     *
     * For internal use.
     *
     * @return The flow response
     */
    @NonNull
    FlowResponse getFlowResponse() {
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

    /**
     * Call to inform FPS that processing is done and no augmentation is required.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void skip() {
        sendEmptyResponse();
    }

    @Override
    @NonNull
    public String getRequestJson() {
        return splitRequest.toJson();
    }
}
