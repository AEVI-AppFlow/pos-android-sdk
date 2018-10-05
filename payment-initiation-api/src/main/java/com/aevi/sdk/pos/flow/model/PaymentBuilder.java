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

import com.aevi.sdk.flow.constants.AdditionalDataKeys;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Token;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.model.config.PaymentSettings;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

/**
 * Builder to construct {@link Payment} objects.
 */
public class PaymentBuilder {

    public static final String AEVI_POS_FLOW = "AEVI POS Flow";

    private String flowName;
    private Amounts amounts;
    private boolean splitEnabled;
    private Token cardToken;
    private AdditionalData additionalData = new AdditionalData();
    private String source = AEVI_POS_FLOW;
    private String deviceId;

    public PaymentBuilder() {
    }

    public PaymentBuilder(Payment paymentToCopyFrom) {
        flowName = paymentToCopyFrom.getFlowName();
        amounts = paymentToCopyFrom.getAmounts();
        splitEnabled = paymentToCopyFrom.isSplitEnabled();
        cardToken = paymentToCopyFrom.getCardToken();
        additionalData = paymentToCopyFrom.getAdditionalData();
        source = paymentToCopyFrom.getSource();
        deviceId = paymentToCopyFrom.getDeviceId();
    }

    /**
     * Set what flow to use for processing this payment.
     *
     * The flow will determine what stages the payment goes through and what applications get called. Please see documentation for more details.
     *
     * See {@link PaymentSettings} for retrieving flow information.
     *
     * @param flowName The name of the flow to use
     * @return This builder
     */
    @NonNull
    public PaymentBuilder withPaymentFlow(String flowName) {
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
     * Set split enabled for this payment which means it *may* be broken up into multiple sub-payments.
     *
     * It is up to the flow processing service configuration if split is allowed or not. Use {@link PaymentClient#getPaymentSettings()} to check
     * whether it is allowed. See documentation/samples for how to retrieve the value.
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
     * @param <T>    The paymentFunction of object this data is an array of
     * @return This builder
     */
    @NonNull
    public <T> PaymentBuilder addAdditionalData(String key, T... values) {
        additionalData.addData(key, values);
        return this;
    }

    /**
     * Set the additional data to pass via the payment.
     *
     * @param additionalData The additional data
     * @return This builder
     */
    @NonNull
    public PaymentBuilder addAdditionalData(AdditionalData additionalData) {
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
        checkArgument(flowName != null, "Flow name must be set");
        checkArgument(amounts != null, "Amounts must be set");
        if (cardToken != null && splitEnabled) {
            throw new IllegalArgumentException("Card token can not be set for a split payment as token relates to only one customer");
        }
        if (additionalData.hasData(AdditionalDataKeys.DATA_KEY_BASKET)) {
            Basket basket = additionalData.getValue(AdditionalDataKeys.DATA_KEY_BASKET, Basket.class);
            if (basket.getTotalBasketValue() != amounts.getBaseAmountValue()) {
                throw new IllegalArgumentException("Basket total value must match the request amount base value!");
            }
        }
        return new Payment(flowName, amounts, splitEnabled, cardToken, additionalData, source, deviceId);
    }
}
