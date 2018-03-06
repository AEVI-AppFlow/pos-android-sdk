package com.aevi.sdk.pos.flow.model;


import com.aevi.sdk.flow.constants.RequestSource;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Token;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

/**
 * Builder to construct {@link Payment} objects.
 */
public class PaymentBuilder {

    private String type;
    private Amounts amounts;
    private boolean splitEnabled;
    private Token cardToken;
    private AdditionalData additionalData = new AdditionalData();
    private String source = RequestSource.AEVI_V3;

    public PaymentBuilder() {
    }

    public PaymentBuilder(Payment paymentToCopyFrom) {
        type = paymentToCopyFrom.getTransactionType();
        amounts = paymentToCopyFrom.getAmounts();
        splitEnabled = paymentToCopyFrom.isSplitEnabled();
        cardToken = paymentToCopyFrom.getCardToken();
        additionalData = paymentToCopyFrom.getAdditionalData();
        source = paymentToCopyFrom.getSource();
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
    public PaymentBuilder withTransactionType(String type) {
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
    public PaymentBuilder withAmounts(Amounts amounts) {
        this.amounts = amounts;
        return this;
    }

    /**
     * Enable split for this payment which means it may be broken up into multiple sub-payments.
     *
     * If set, it is up to the acquirer environment if and how the payment is split into
     * multiple transactions.
     *
     * If this is not called, it is still possible that split will be enabled during the payment flow.
     *
     * @return This builder
     */
    public PaymentBuilder enableSplit() {
        this.splitEnabled = true;
        return this;
    }

    /**
     * Set any previously generated card token to use for this transaction.
     *
     * @param cardToken The card {@link Token}
     * @return This builder
     */
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
    public PaymentBuilder addAdditionalData(AdditionalData additionalData) {
        this.additionalData = additionalData;
        return this;
    }

    /**
     * Build an instance of {@link Payment} using the provided parameters.
     *
     * @return An instance of {@link Payment}
     * @throws IllegalArgumentException for invalid data or combinations of data
     */
    public Payment build() {
        checkArgument(type != null, "Transaction type must be set");
        if (cardToken != null && splitEnabled) {
            throw new IllegalArgumentException("Card token can not be set for a split payment as token relates to only one customer");
        }
        return new Payment(type, amounts, splitEnabled, cardToken, additionalData, source);
    }
}
