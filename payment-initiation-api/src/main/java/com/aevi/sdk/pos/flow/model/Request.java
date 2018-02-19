package com.aevi.sdk.pos.flow.model;

import android.util.Log;

import com.aevi.sdk.flow.constants.RequestSource;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.BaseModel;
import com.aevi.sdk.flow.model.Token;
import com.aevi.sdk.flow.FlowClient;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.util.json.JsonConverter;

import java.util.UUID;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

/**
 * Request for transaction processing.
 */
public class Request extends BaseModel {

    private final String paymentServiceId;
    private final String deviceId;
    private final String transactionType;
    private final Amounts amounts;
    private final boolean splitEnabled;
    private final Token cardToken;
    private final AdditionalData additionalData;
    private final boolean isExternalId;
    private final String source;

    /**
     * Internal constructor for use from the Builder.
     */
    private Request(String paymentServiceId, String deviceId, String transactionType, Amounts amounts, boolean splitEnabled, Token cardToken,
                    AdditionalData additionalData, String source) {
        super(UUID.randomUUID().toString());
        Log.i(Request.class.getSimpleName(), "Created Request with (internal) id: " + getId());
        this.paymentServiceId = paymentServiceId;
        this.deviceId = deviceId;
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
     * Package level constructor, allowing an internal client to set an external id to use for this request.
     *
     * This is useful when a request from a different SDK/API is translated to our Request model.
     */
    Request(String id, String requestSource, String paymentServiceId, String deviceId, String transactionType, Amounts amounts, boolean splitEnabled, Token cardToken, AdditionalData additionalData) {
        super(id);
        Log.i(Request.class.getSimpleName(), "Created Request with (external) id: " + id);
        this.source = requestSource;
        this.paymentServiceId = paymentServiceId;
        this.deviceId = deviceId;
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
    }

    /**
     * Get the id of the payment service to use for this request, if any.
     *
     * @return The payment service to use for this transaction, if any.
     */
    @Nullable
    public String getPaymentServiceId() {
        return paymentServiceId;
    }

    /**
     * Get the id of the device that should be used for customer interactions, if any.
     *
     * @return The device to use for this transaction, if any.
     */
    @Nullable
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Get the amounts associated with this request.
     *
     * @return The amounts
     */
    @Nullable
    public Amounts getAmounts() {
        return amounts;
    }

    /**
     * Get the transaction type associated with this request.
     *
     * @return The transaction type
     */
    @NonNull
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Whether or not this request can/should be split into multiple transactions.
     *
     * If set, it is up to the acquirer environment if and how the request is split into
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
     * Get the extra data set for this request.
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
     * Get information on whether the id of this request was generated internally or externally.
     *
     * For most cases, the id is generated internally, but for cases where requests from another API/SDK
     * is translated to our request model, the id from that external request will be set and this flag set to true.
     *
     * @return True if external, false if internal
     */
    public boolean isExternalId() {
        return isExternalId;
    }

    /**
     * Get the source of this request.
     *
     * @return The source of this request.
     */
    @NonNull
    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "Request{" +
                "paymentServiceId='" + paymentServiceId + '\'' +
                ", deviceId='" + deviceId + '\'' +
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

        Request request = (Request) o;

        if (splitEnabled != request.splitEnabled) return false;
        if (isExternalId != request.isExternalId) return false;
        if (paymentServiceId != null ? !paymentServiceId.equals(request.paymentServiceId) : request.paymentServiceId != null) return false;
        if (deviceId != null ? !deviceId.equals(request.deviceId) : request.deviceId != null) return false;
        if (transactionType != null ? !transactionType.equals(request.transactionType) : request.transactionType != null) return false;
        if (amounts != null ? !amounts.equals(request.amounts) : request.amounts != null) return false;
        if (cardToken != null ? !cardToken.equals(request.cardToken) : request.cardToken != null) return false;
        if (additionalData != null ? !additionalData.equals(request.additionalData) : request.additionalData != null) return false;
        return source != null ? source.equals(request.source) : request.source == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (paymentServiceId != null ? paymentServiceId.hashCode() : 0);
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
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

    public static Request fromJson(String json) {
        return JsonConverter.deserialize(json, Request.class);
    }

    /**
     * Builder for a {@link Request} instance.
     */
    public static class Builder {

        private String paymentServiceId;
        private String deviceId;
        private String type;
        private Amounts amounts;
        private boolean splitEnabled;
        private Token cardToken;
        private AdditionalData additionalData = new AdditionalData();
        private String source = RequestSource.AEVI_V3;

        public Builder() {
        }

        public Builder(Request requestToCopyFrom) {
            paymentServiceId = requestToCopyFrom.getPaymentServiceId();
            deviceId = requestToCopyFrom.getDeviceId();
            type = requestToCopyFrom.getTransactionType();
            amounts = requestToCopyFrom.getAmounts();
            splitEnabled = requestToCopyFrom.isSplitEnabled();
            cardToken = requestToCopyFrom.getCardToken();
            additionalData = requestToCopyFrom.getAdditionalData();
            source = requestToCopyFrom.getSource();
        }

        /**
         * Optionally set what payment service to use for this transaction.
         *
         * The available payment services can be queried via {@link PaymentClient#getPaymentServices()}.
         *
         * Note that if this is not set, a decision will either be made internally or the merchant/customer will be presented
         * with a user interface to choose an appropriate payment service in the case where more than one is available.
         *
         * If only one payment service is available, this parameter will be ignored.
         *
         * @param paymentServiceId The payment service id as available via {@link PaymentServiceInfo#getId()}
         * @return This builder
         */
        public Builder withPaymentService(String paymentServiceId) {
            this.paymentServiceId = paymentServiceId;
            return this;
        }

        /**
         * Optionally set what device to use for interactions with the customer.
         *
         * The available devices can be queried via {@link FlowClient#getDevices()}.
         *
         * Setting this means that all customer facing activities will be run on that device, such as flow apps and payment apps.
         *
         * Note that it is possible that devices will be disconnected in between querying for the list and this request being handled, in which
         * case it will fall back to the default device selection mechanism. See docs for more details.
         *
         * @param deviceId The id of the customer facing device
         * @return This builder
         */
        public Builder onDevice(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        /**
         * Set the transaction type.
         *
         * See online documentation for more information.
         *
         * This parameter is mandatory.
         *
         * @param type The transaction type
         * @return This builder
         */
        public Builder withTransactionType(String type) {
            this.type = type;
            return this;
        }

        /**
         * Set the amounts.
         *
         * This parameter is optional here, but would be mandatory for any transaction type
         * that exchanges money (sale, refund, etc). It is however not required for scenarios such as reversals.
         *
         * @param amounts The amounts.
         * @return This builder
         */
        public Builder withAmounts(Amounts amounts) {
            this.amounts = amounts;
            return this;
        }

        /**
         * Set whether or not this request can/should be split into multiple transactions.
         *
         * If set, it is up to the acquirer environment if and how the request is split into
         * multiple transactions.
         *
         * If not set, it is still possible that split will be enabled during the payment flow.
         *
         * @param splitEnabled True to enable split, false otherwise
         * @return This builder
         */
        public Builder withSplitEnabled(boolean splitEnabled) {
            this.splitEnabled = splitEnabled;
            return this;
        }

        /**
         * Set any previously generated card token to use for this transaction.
         *
         * @param cardToken The card {@link Token}
         * @return This builder
         */
        public Builder withCardToken(Token cardToken) {
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
        public <T> Builder withAdditionalData(String key, T... values) {
            additionalData.addData(key, values);
            return this;
        }

        /**
         * Set the additional data to pass via the request.
         *
         * @param additionalData The additional data
         * @return This builder
         */
        public Builder withAdditionalData(AdditionalData additionalData) {
            this.additionalData = additionalData;
            return this;
        }

        /**
         * Build an instance of {@link Request} using the provided parameters.
         *
         * @return An instance of {@link Request}
         * @throws IllegalArgumentException for invalid data or combinations of data
         */
        public Request build() {
            checkArgument(type != null, "Transaction type must be set");
            if (paymentServiceId != null && cardToken != null) {
                checkArgument(paymentServiceId.equals(cardToken.getSourceAppId()), "paymentServiceId can not be set to a different value than what is set in the Token");
            }
            if (cardToken != null && splitEnabled) {
                throw new IllegalArgumentException("Card token can not be set for a split request as token relates to only one customer");
            }
            return new Request(paymentServiceId, deviceId, type, amounts, splitEnabled, cardToken, additionalData, source);
        }
    }
}
