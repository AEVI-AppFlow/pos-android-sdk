package com.aevi.sdk.pos.flow.model;

import android.util.Log;

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.DeviceAudience;
import com.aevi.util.json.JsonConverter;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

/**
 * Request for an individual transaction to be processed by a payment app / service.
 */
public class TransactionRequest extends BasePaymentModel {

    private final String transactionType;
    private final Amounts amounts;
    private final PaymentStage paymentStage;
    private final AdditionalData additionalData;
    private final Card card;
    private DeviceAudience deviceAudience;

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
    public TransactionRequest(String id, String transactionType, PaymentStage paymentStage, Amounts amounts, AdditionalData additionalData, Card card) {
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
     */
    @NonNull
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

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "transactionType='" + transactionType + '\'' +
                ", amounts=" + amounts +
                ", paymentStage=" + paymentStage +
                ", additionalData=" + additionalData +
                ", card=" + card +
                ", deviceAudience=" + deviceAudience +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TransactionRequest that = (TransactionRequest) o;

        if (transactionType != null ? !transactionType.equals(that.transactionType) : that.transactionType != null) return false;
        if (amounts != null ? !amounts.equals(that.amounts) : that.amounts != null) return false;
        if (paymentStage != that.paymentStage) return false;
        if (additionalData != null ? !additionalData.equals(that.additionalData) : that.additionalData != null) return false;
        if (card != null ? !card.equals(that.card) : that.card != null) return false;
        return deviceAudience == that.deviceAudience;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (transactionType != null ? transactionType.hashCode() : 0);
        result = 31 * result + (amounts != null ? amounts.hashCode() : 0);
        result = 31 * result + (paymentStage != null ? paymentStage.hashCode() : 0);
        result = 31 * result + (additionalData != null ? additionalData.hashCode() : 0);
        result = 31 * result + (card != null ? card.hashCode() : 0);
        result = 31 * result + (deviceAudience != null ? deviceAudience.hashCode() : 0);
        return result;
    }
}
