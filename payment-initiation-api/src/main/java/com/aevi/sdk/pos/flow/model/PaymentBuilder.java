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

package com.aevi.sdk.pos.flow.model;


import android.support.annotation.NonNull;

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.flow.model.Token;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.model.config.PaymentSettings;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

/**
 * Builder to construct {@link Payment} objects.
 */
public class PaymentBuilder {

    public static final String AEVI_APPFLOW = "AEVI AppFlow";

    private String flowType;
    private String flowName;
    private Amounts amounts;
    private Basket basket;
    private String paymentMethod;
    private Customer customer;
    private boolean splitEnabled;
    private Token cardToken;
    private AdditionalData additionalData = new AdditionalData();
    private String source = AEVI_APPFLOW;
    private String deviceId;

    public PaymentBuilder() {
    }

    public PaymentBuilder(Payment paymentToCopyFrom) {
        flowType = paymentToCopyFrom.getFlowType();
        flowName = paymentToCopyFrom.getFlowName();
        amounts = paymentToCopyFrom.getAmounts();
        basket = paymentToCopyFrom.getBasket();
        paymentMethod = paymentToCopyFrom.getPaymentMethod();
        customer = paymentToCopyFrom.getCustomer();
        splitEnabled = paymentToCopyFrom.isSplitEnabled();
        cardToken = paymentToCopyFrom.getCardToken();
        additionalData = paymentToCopyFrom.getAdditionalData();
        source = paymentToCopyFrom.getSource();
        deviceId = paymentToCopyFrom.getDeviceId();
    }

    /**
     * Set what flow to use based on flow type.
     *
     * As AppFlow supports multiple flows per type, assigning a flow based on type only may lead to a runtime selection dialog for the merchant.
     *
     * Ideally, for any production scenarios, use {@link #withPaymentFlow(String, String)} to specify the name of the flow as well to ensure
     * a good experience for the merchant. See docs for more clarification on this.
     *
     * The flow will determine what stages the payment goes through and what applications get called.
     *
     * See {@link PaymentSettings} for retrieving flow information.
     *
     * @param flowType The flow type
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withPaymentFlow(String flowType) {
        this.flowType = flowType;
        return this;
    }

    /**
     * Set what flow to use based on flow type and name.
     *
     * This ensures that the intended flow is used in the case of multiple flows for the provided type.
     *
     * The flow will determine what stages the payment goes through and what applications get called.
     *
     * See {@link PaymentSettings} for retrieving flow information.
     *
     * @param flowType The flow type
     * @param flowName The name of the flow to use
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withPaymentFlow(String flowType, String flowName) {
        this.flowType = flowType;
        this.flowName = flowName;
        return this;
    }

    /**
     * Set the amounts.
     *
     * This parameter is mandatory.
     *
     * @param amounts The amounts.
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withAmounts(Amounts amounts) {
        this.amounts = amounts;
        return this;
    }

    /**
     * Add a basket for this payment.
     *
     * Note that {@link #withAmounts(Amounts)} must be called to reflect the value of the basket. This API and the flow processing service do not
     * take the basket data into account for any processing.
     *
     * @param basket The basket
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withBasket(Basket basket) {
        this.basket = basket;
        return this;
    }

    /**
     * Specify the payment method to use for transaction processing for this payment.
     *
     * Setting this has two effects - first it means FPS will filter payment applications based on supported methods, and secondarily it allows
     * any payment app that supports multiple methods to proceed without a user choice.
     *
     * Note that this value has no effect on flow services in general - they may still use any method of their choice to offer services such as loyalty.
     *
     * @param paymentMethod The payment method to use for this payment
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Add customer details for this payment.
     *
     * Note that this should not be set for a split payment.
     *
     * @param customer Customer details
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    /**
     * Set split enabled for this payment which means it *may* be broken up into multiple sub-payments.
     *
     * It is up to the flow processing service configuration if split is enabled or not. Use {@link PaymentClient#getPaymentSettings()} to get
     * list of flow configurations and check for defined stages
     *
     * If this is not called, it is still possible that split will be enabled during the payment flow.
     *
     * @param enabled True to enable split, false to disable
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withSplitEnabled(boolean enabled) {
        this.splitEnabled = enabled;
        return this;
    }

    /**
     * Set any previously generated card token to use for this transaction.
     *
     * @param cardToken The card {@link Token}
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withCardToken(Token cardToken) {
        this.cardToken = cardToken;
        return this;
    }

    /**
     * Convenience wrapper for adding additional data.
     *
     * See {@link AdditionalData#addData(String, Object[])} for more info.
     *
     * @param key    The key to use for this data
     * @param values An array of values for this data
     * @param <T>    The type of object this data is an array of
     * @return This builder
     */
    @NonNull
    public <T> PaymentBuilder addAdditionalData(String key, T... values) {
        additionalData.addData(key, values);
        return this;
    }

    /**
     * @deprecated Please use {@link #withAdditionalData}
     */
    @NonNull
    @Deprecated
    public PaymentBuilder addAdditionalData(AdditionalData additionalData) {
        return withAdditionalData(additionalData);
    }

    /**
     * Sets the additional data to pass via the payment.
     *
     * <strong>NOTE: This will replace any previously added additional data with the data given here.</strong>
     *
     * @param additionalData The additional data
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withAdditionalData(AdditionalData additionalData) {
        this.additionalData = additionalData;
        return this;
    }

    /**
     * Get the current additional data model.
     *
     * @return The additional data
     */
    @NonNull
    public AdditionalData getCurrentAdditionalData() {
        return additionalData;
    }

    /**
     * Set the id of the device this payment should be processed on.
     *
     * @param deviceId The id of the device to process this payment on
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    /**
     * Build an instance of {@link Payment} using the provided parameters.
     *
     * @return An instance of {@link Payment}
     * @throws IllegalArgumentException for invalid data or combinations of data
     */
    @NonNull
    public Payment build() {
        checkArgument(flowType != null, "Flow type must be set");
        checkArgument(amounts != null, "Amounts must be set");
        if (cardToken != null && splitEnabled) {
            throw new IllegalArgumentException("Card token can not be set for a split payment as token relates to only one customer");
        }
        if (basket != null && basket.getTotalBasketValue() != amounts.getBaseAmountValue()) {
            throw new IllegalArgumentException("The basket total value must match base amounts value");
        }
        if (basket != null) {
            basket.setPrimaryBasket(true);
        }
        return new Payment(flowType, flowName, amounts, basket, customer, splitEnabled, cardToken, additionalData, source, deviceId, paymentMethod);
    }
}
