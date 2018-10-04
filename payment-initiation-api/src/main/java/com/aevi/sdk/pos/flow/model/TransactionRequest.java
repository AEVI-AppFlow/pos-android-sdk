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

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.BaseModel;
import com.aevi.sdk.flow.model.DeviceAudience;
import com.aevi.util.json.JsonConverter;

import java.util.Objects;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

/**
 * Request for an individual transaction to be processed by a payment app / service.
 */
public class TransactionRequest extends BaseModel {

    private final String flowType;
    private final Amounts amounts;
    private final String flowStage;
    private final AdditionalData additionalData;
    private final Card card;
    private DeviceAudience deviceAudience;
    private String targetPaymentAppComponent;

    // Default constructor for deserialisation
    TransactionRequest() {
        this("N/A", "", "", new Amounts(), new AdditionalData(), null);
    }

    /**
     * Construct a new instance.
     *
     * @param id             The transaction id
     * @param flowType       The flow type
     * @param flowStage      The current flow stage
     * @param amounts        The amounts to process
     * @param additionalData The additional data
     * @param card           The card details from the VAA or from the payment card reading step
     */
    public TransactionRequest(String id, String flowType, String flowStage, Amounts amounts, AdditionalData additionalData, Card card) {
        super(id);
        this.flowStage = flowStage;
        this.flowType = flowType;
        this.amounts = amounts;
        this.additionalData = additionalData != null ? additionalData : new AdditionalData();
        this.card = card != null ? card : Card.getEmptyCard();
        this.deviceAudience = DeviceAudience.MERCHANT;
        checkArgument(id != null && !id.isEmpty(), "Id must be set");
        checkArgument(flowType != null, "Flow type must be set");
    }

    /**
     * Get the flow type (aka transaction type).
     *
     * Can not be null.
     *
     * @return The flow type.
     */
    @NonNull
    public String getFlowType() {
        return flowType;
    }

    /**
     * Get the amounts to process.
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
     * Get the additional data
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
     * @return The current flow stage this request was generated for
     */
    @NonNull
    public String getFlowStage() {
        return flowStage;
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

    /**
     * For internal use.
     *
     * @return String
     */
    public String getTargetPaymentAppComponent() {
        return targetPaymentAppComponent;
    }

    /**
     * For internal use.
     *
     * @param targetPaymentAppComponent String
     */
    public void setTargetPaymentAppComponent(String targetPaymentAppComponent) {
        if (targetPaymentAppComponent != null) {
            this.targetPaymentAppComponent = targetPaymentAppComponent;
        }
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "flowType='" + flowType + '\'' +
                ", amounts=" + amounts +
                ", flowStage=" + flowStage +
                ", additionalData=" + additionalData +
                ", card=" + card +
                ", deviceAudience=" + deviceAudience +
                ", targetPaymentAppComponent='" + targetPaymentAppComponent + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TransactionRequest that = (TransactionRequest) o;
        return Objects.equals(flowType, that.flowType) &&
                Objects.equals(amounts, that.amounts) &&
                flowStage == that.flowStage &&
                Objects.equals(additionalData, that.additionalData) &&
                Objects.equals(card, that.card) &&
                deviceAudience == that.deviceAudience &&
                Objects.equals(targetPaymentAppComponent, that.targetPaymentAppComponent);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), flowType, amounts, flowStage, additionalData, card, deviceAudience, targetPaymentAppComponent);
    }
}
