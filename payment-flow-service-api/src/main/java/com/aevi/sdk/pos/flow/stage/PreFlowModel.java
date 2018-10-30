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
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;

import static com.aevi.sdk.flow.service.ActivityHelper.ACTIVITY_REQUEST_KEY;

/**
 * Model for the pre-flow stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService#onPreFlow(PreFlowModel)} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 *
 * If no data has been augmented, calling {@link #sendResponse()} will send back an empty response informing FPS that no changes were made.
 */
public class PreFlowModel extends BaseStageModel {

    private final Payment payment;
    private final AmountsModifier amountsModifier;
    private final FlowResponse flowResponse;

    private PreFlowModel(Activity activity, Payment payment) {
        super(activity);
        this.payment = payment;
        this.amountsModifier = new AmountsModifier(payment.getAmounts());
        this.flowResponse = new FlowResponse();
    }

    private PreFlowModel(ClientCommunicator clientCommunicator, Payment payment) {
        super(clientCommunicator);
        this.payment = payment;
        this.amountsModifier = new AmountsModifier(payment.getAmounts());
        this.flowResponse = new FlowResponse();
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link BaseStageModel#processInActivity(Context, Class)},
     * or via the {@link ActivityProxyService}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link PreFlowModel}
     */
    public static PreFlowModel fromActivity(Activity activity) {
        String request = activity.getIntent().getStringExtra(ACTIVITY_REQUEST_KEY);
        return new PreFlowModel(activity, Payment.fromJson(request));
    }

    /**
     * Create an instance from a service context.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request            The deserialised Payment provided as a string via {@link BaseApiService#processRequest(ClientCommunicator, String, String)}
     * @return An instance of {@link PreFlowModel}
     */
    public static PreFlowModel fromService(ClientCommunicator clientCommunicator, Payment request) {
        return new PreFlowModel(clientCommunicator, request);
    }

    /**
     * Get the payment object.
     *
     * @return The payment object
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * Enable or disable split for this payment.
     *
     * @param enabled True to enable split, false otherwise
     */
    public void setSplitEnabled(boolean enabled) {
        flowResponse.setEnableSplit(enabled);
    }

    /**
     * Cancel the flow.
     */
    public void cancelFlow() {
        flowResponse.setCancelTransaction(true);
    }

    /**
     * Update the base payment amount.
     *
     * Note that this should be used with care, as the base amount typically reflects the value of the purchase.
     *
     * If you are looking to add a fee, charity contribution, tax/vat, etc, please use {@link #setAdditionalAmount(String, long)}.
     *
     * Must be 0 or greater.
     *
     * @param baseAmountValue The new base amount
     */
    public void updateBaseAmount(long baseAmountValue) {
        amountsModifier.updateBaseAmount(baseAmountValue);
    }

    /**
     * Set or update an additional payment amount with an amount value.
     *
     * This is typically used to add a fee/charge, charity contribution, tax/vat, etc
     *
     * Must be 0 or greater.
     *
     * @param identifier The amount identifier
     * @param amount     The amount value
     */
    public void setAdditionalAmount(String identifier, long amount) {
        amountsModifier.setAdditionalAmount(identifier, amount);
    }

    /**
     * Set or update an additional amount as a fraction of the base amount.
     *
     * This is useful for cases where a fee, charity contribution, etc is calculated as a fraction or percentage of the base amount.
     *
     * @param identifier The string identifier for the amount
     * @param fraction   The fraction of the base amount, ranging from 0.0 to 1.0f (0% to 100%)
     */
    public void setAdditionalAmountAsBaseFraction(String identifier, float fraction) {
        amountsModifier.setAdditionalAmountAsBaseFraction(identifier, fraction);
    }

    /**
     * Add a new basket to the transaction.
     *
     * An application can either add a new basket via this method, or add more items to an existing basket via {@link #addItemsToExistingBasket(String, BasketItem...)}.
     *
     * This method is the recommended option to add "extras" to a purchase, whereas the other method is recommended to provide discounts, etc.
     *
     * The transaction base amount will automatically be updated to take the value of this basket into account.
     *
     * @param basket The basket to add
     */
    public void addNewBasket(Basket basket) {
        flowResponse.addBasket(basket);
        amountsModifier.offsetBaseAmount(basket.getTotalBasketValue());
    }

    /**
     * Add items to an existing basket.
     *
     * The main use case for this is to allow applications to apply discounts to existing baskets.
     *
     * For adding "extras" to a transaction, please use {@link #addNewBasket(Basket)}.
     *
     * The transaction base amount will automatically be updated to take the value of these changes into account.
     *
     * @param basketId    The id of the basket to add the items to
     * @param basketItems The basket items to add
     */
    public void addItemsToExistingBasket(String basketId, BasketItem... basketItems) {
        flowResponse.updateBasket(basketId, basketItems);
        amountsModifier.offsetBaseAmount(flowResponse.getBasket().getTotalBasketValue());
    }

    /**
     * Add a customer or update existing customer details.
     *
     * @param customer The customer details
     */
    public void addOrUpdateCustomerDetails(Customer customer) {
        flowResponse.addOrUpdateCustomer(customer);
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
        return payment.toJson();
    }
}
