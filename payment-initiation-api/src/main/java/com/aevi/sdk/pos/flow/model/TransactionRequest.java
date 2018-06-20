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
import android.support.annotation.Nullable;
import android.util.Log;

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.BaseModel;
import com.aevi.sdk.flow.model.DeviceAudience;
import com.aevi.util.json.JsonConverter;

import java.util.Objects;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;
import static com.aevi.sdk.pos.flow.model.PaymentStage.TRANSACTION_PROCESSING;

/**
 * Request for an individual transaction to be processed by a payment app / service.
 */
public class TransactionRequest extends BaseModel {

    private final String transactionType;
    private final Amounts amounts;
    private final PaymentStage paymentStage;
    private final AdditionalData additionalData;
    private final Card card;
    private DeviceAudience deviceAudience;
    private String targetPaymentAppComponent;
    private String componentName;

    // Default constructor for deserialisation
    TransactionRequest() {
        this("N/A", "", TRANSACTION_PROCESSING, new Amounts(), new AdditionalData(), null);
    }

    /**
     * Construct a new instance.
     *
     * @param id              The transaction id
     * @param transactionType The transaction type
     * @param paymentStage    The current payment stage
     * @param amounts         The amounts to process
     * @param additionalData  The additional data
     * @param card            The card details from the VAA or from the payment card reading step
     */
    public TransactionRequest(String id, String transactionType, PaymentStage paymentStage, Amounts amounts, AdditionalData additionalData,
                              Card card) {
        super(id);
        this.paymentStage = paymentStage;
        Log.i(TransactionRequest.class.getSimpleName(), "Created TransactionRequest with id: " + id);
        checkArgument(id != null && !id.isEmpty(), "Id must be set");
        checkArgument(transactionType != null, "Transaction type must be set");
        this.transactionType = transactionType;
        this.amounts = amounts;
        this.additionalData = additionalData != null ? additionalData : new AdditionalData();
        this.card = card != null ? card : Card.getEmptyCard();
        this.deviceAudience = DeviceAudience.MERCHANT;
    }

    /**
     * The transaction type.
     *
     * Can not be null.
     *
     * @return The transaction type.
     */
    @NonNull
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * The amounts to process.
     *
     * Can be null for non-payment type of transaction.
     *
     * @return The amounts
     */
    @Nullable
    public Amounts getAmounts() {
        return amounts;
    }

    /**
     * The additional data
     *
     * @return The data. May be empty.
     */
    @NonNull
    public AdditionalData getAdditionalData() {
        return additionalData;
    }

    /**
     * Get the stage the payment is at, which will determine what data is available and what response data can be set.
     *
     * @return The {@link PaymentStage}
     * @deprecated This is no longer required and will be deleted in the next major API update
     */
    @NonNull
    @Deprecated
    public PaymentStage getPaymentStage() {
        return paymentStage;
    }

    public void setDeviceAudience(DeviceAudience deviceAudience) {
        this.deviceAudience = deviceAudience;
    }

    /**
     * Get the type of audience (merchant or customer) that will be interacting with the device your app is running on.
     *
     * This information can be used to customise your application for the audience appropriately.
     *
     * @return The type of audience (merchant or customer) of the device this app is running on
     */
    @NonNull
    public DeviceAudience getDeviceAudience() {
        return deviceAudience;
    }

    /**
     * Get the card details provided from the VAA or from the payment card reading step.
     *
     * Note that in some cases, only the token will be available.
     *
     * @return The card details (may be empty)
     */
    @NonNull
    public Card getCard() {
        return card;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static TransactionRequest fromJson(String json) {
        return JsonConverter.deserialize(json, TransactionRequest.class);
    }

    public String getTargetPaymentAppComponent() {
        return targetPaymentAppComponent;
    }

    public void setTargetPaymentAppComponent(String targetPaymentAppComponent) {
        if (targetPaymentAppComponent != null) {
            this.targetPaymentAppComponent = targetPaymentAppComponent;
        }
    }

    public String getComponentName() {
        return componentName;
    }

    public String getComponentNameValue() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "transactionType='" + transactionType + '\'' +
                ", amounts=" + amounts +
                ", paymentStage=" + paymentStage +
                ", additionalData=" + additionalData +
                ", card=" + card +
                ", deviceAudience=" + deviceAudience +
                ", targetPaymentAppComponent='" + targetPaymentAppComponent + '\'' +
                ", componentName='" + componentName + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TransactionRequest that = (TransactionRequest) o;
        return Objects.equals(transactionType, that.transactionType) &&
                Objects.equals(amounts, that.amounts) &&
                paymentStage == that.paymentStage &&
                Objects.equals(additionalData, that.additionalData) &&
                Objects.equals(card, that.card) &&
                deviceAudience == that.deviceAudience &&
                Objects.equals(targetPaymentAppComponent, that.targetPaymentAppComponent) &&
                Objects.equals(componentName, that.componentName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), transactionType, amounts, paymentStage, additionalData, card, deviceAudience, targetPaymentAppComponent, componentName);
    }
}
