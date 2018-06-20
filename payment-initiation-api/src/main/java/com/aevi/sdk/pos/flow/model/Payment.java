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
import com.aevi.sdk.flow.model.Token;
import com.aevi.util.json.JsonConverter;

import java.util.UUID;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

/**
 * Payment model containing relevant information for initiating payments involving amounts.
 *
 * For other type of requests (such as reversals), please see {@link com.aevi.sdk.flow.model.Request}
 */
public class Payment extends BaseModel {

    private final String transactionType;
    private final Amounts amounts;
    private final boolean splitEnabled;
    private final Token cardToken;
    private final AdditionalData additionalData;
    private final boolean isExternalId;
    private final String source;

    // Default constructor for deserialisation
    Payment() {
        this("", new Amounts(), false, null, null, "");
    }

    /**
     * Internal constructor for use from the Builder.
     */
    Payment(String transactionType, Amounts amounts, boolean splitEnabled, Token cardToken,
            AdditionalData additionalData, String source) {
        super(UUID.randomUUID().toString());
        Log.i(Payment.class.getSimpleName(), "Created Payment with (internal) id: " + getId());
        this.isExternalId = false;
        this.transactionType = transactionType;
        this.amounts = amounts;
        this.splitEnabled = splitEnabled;
        this.cardToken = cardToken;
        this.additionalData = additionalData != null ? additionalData : new AdditionalData();
        this.source = source;
        validateArguments();
    }

    /**
     * Package level constructor, allowing an internal client to set an external id to use for this payment.
     *
     * This is useful when a request from a different SDK/API is translated to our Payment model.
     */
    Payment(String id, String requestSource, String transactionType, Amounts amounts, boolean splitEnabled, Token cardToken, AdditionalData additionalData) {
        super(id);
        Log.i(Payment.class.getSimpleName(), "Created Payment with (external) id: " + id);
        this.source = requestSource;
        this.isExternalId = true;
        this.transactionType = transactionType;
        this.amounts = amounts;
        this.splitEnabled = splitEnabled;
        this.cardToken = cardToken;
        this.additionalData = additionalData != null ? additionalData : new AdditionalData();
        validateArguments();
    }

    private void validateArguments() {
        checkArgument(getId() != null, "Id cannot be null");
        checkArgument(transactionType != null, "Type cannot be null");
        checkArgument(amounts != null, "Amounts cannot be null");
    }

    /**
     * Get the amounts associated with this payment.
     *
     * @return The amounts
     */
    @NonNull
    public Amounts getAmounts() {
        return amounts;
    }

    /**
     * Get the transaction type associated with this payment.
     *
     * @return The transaction type
     */
    @NonNull
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Whether or not this payment can/should be split into multiple transactions.
     *
     * If set, it is up to the acquirer environment if and how the payment is split into
     * multiple transactions.
     *
     * If not set, it is still possible that split will be enabled during the payment flow.
     *
     * @return True if split is enabled, false otherwise.
     */
    public boolean isSplitEnabled() {
        return splitEnabled;
    }

    /**
     * Get any card token to be used for this transaction.
     *
     * @return The card {@link Token} to be used
     */
    @Nullable
    public Token getCardToken() {
        return cardToken;
    }

    /**
     * Get the extra data set for this payment.
     *
     * This contains optional and bespoke parameters.
     *
     * @return The additional data (may be empty).
     */
    @NonNull
    public AdditionalData getAdditionalData() {
        return additionalData;
    }

    /**
     * Get information on whether the id of this payment was generated internally or externally.
     *
     * For most cases, the id is generated internally, but for cases where requests from another API/SDK
     * is translated to our payment model, the id from that external request will be set and this flag set to true.
     *
     * @return True if external, false if internal
     */
    public boolean isExternalId() {
        return isExternalId;
    }

    /**
     * Get the source of this payment.
     *
     * @return The source of this payment.
     */
    @NonNull
    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "Payment{" +
                ", transactionType='" + transactionType + '\'' +
                ", amounts=" + amounts +
                ", splitEnabled=" + splitEnabled +
                ", cardToken=" + cardToken +
                ", additionalData=" + additionalData +
                ", isExternalId=" + isExternalId +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Payment payment = (Payment) o;

        if (splitEnabled != payment.splitEnabled) return false;
        if (isExternalId != payment.isExternalId) return false;
        if (transactionType != null ? !transactionType.equals(payment.transactionType) : payment.transactionType != null) return false;
        if (amounts != null ? !amounts.equals(payment.amounts) : payment.amounts != null) return false;
        if (cardToken != null ? !cardToken.equals(payment.cardToken) : payment.cardToken != null) return false;
        if (additionalData != null ? !additionalData.equals(payment.additionalData) : payment.additionalData != null) return false;
        return source != null ? source.equals(payment.source) : payment.source == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (transactionType != null ? transactionType.hashCode() : 0);
        result = 31 * result + (amounts != null ? amounts.hashCode() : 0);
        result = 31 * result + (splitEnabled ? 1 : 0);
        result = 31 * result + (cardToken != null ? cardToken.hashCode() : 0);
        result = 31 * result + (additionalData != null ? additionalData.hashCode() : 0);
        result = 31 * result + (isExternalId ? 1 : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static Payment fromJson(String json) {
        return JsonConverter.deserialize(json, Payment.class);
    }

}
