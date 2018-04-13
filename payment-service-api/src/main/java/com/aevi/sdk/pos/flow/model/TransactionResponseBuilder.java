package com.aevi.sdk.pos.flow.model;

import com.aevi.sdk.flow.constants.PaymentMethods;
import com.aevi.sdk.flow.model.AdditionalData;

import static com.aevi.sdk.flow.util.Preconditions.*;
import static com.aevi.sdk.pos.flow.model.TransactionResponse.Outcome;

/**
 * Builder to create a {@link TransactionResponse} instance.
 *
 * At a minimum, the transaction outcome must be set via one of the {@link #approve()} or {@link #decline(String)} calls.
 */
public final class TransactionResponseBuilder {

    private String id;
    private Card card;
    private Outcome outcome;
    private String outcomeMessage;
    private Amounts amounts;
    private String responseCode;
    private String paymentMethod = PaymentMethods.CARD; // Default to card
    private AdditionalData references = new AdditionalData();

    /**
     * Initialise this builder with the id of the processed TransactionRequest.
     *
     * @param requestId The id of the TransactionRequest passed to the payment app / service.
     */
    public TransactionResponseBuilder(String requestId) {
        this.id = requestId;
    }

    /**
     * Set the transaction as approved with no processed amounts (for scenarios where no amounts were charged/processed).
     *
     * For cases where amounts were processed, see {@link #approve(Amounts, String...)}.
     *
     * @return This builder
     */
    public TransactionResponseBuilder approve() {
        this.outcome = Outcome.APPROVED;
        return this;
    }

    /**
     * Set the transaction as approved with processed amount details and optionally specify the payment method used (defaults to "card").
     *
     * For cases where no amounts were processed, see {@link #approve()}.
     *
     * @return This builder
     */
    public TransactionResponseBuilder approve(Amounts processedAmounts, String... paymentMethod) {
        this.outcome = Outcome.APPROVED;
        checkNotNull(processedAmounts, "Processed amounts must be set");
        this.amounts = processedAmounts;
        if (paymentMethod.length > 0) {
            this.paymentMethod = paymentMethod[0];
        }
        return this;
    }

    /**
     * Set the transaction as declined and provide a decline message detailing the reason for it.
     *
     * Note that it is also advisable that a response code is set where possible via {@link #withResponseCode(String)}.
     *
     * @param declineMessage The message detailing why the transaction was declined
     * @return This builder
     */
    public TransactionResponseBuilder decline(String declineMessage) {
        this.outcome = Outcome.DECLINED;
        checkNotNull(declineMessage, "Decline message must be set");
        this.outcomeMessage = declineMessage;
        return this;
    }

    /**
     * Set the card used for the transaction.
     *
     * @param card The card details
     * @return This builder
     */
    public TransactionResponseBuilder withCard(Card card) {
        this.card = card;
        return this;
    }

    /**
     * Set an outcome message to further clarify the reason for the outcome.
     *
     * This is mandatory to set for declined outcomes via the {@link #decline(String)} call, but can optionally be set for approved scenarios too.
     *
     * This is not to be confused with {@link #withResponseCode(String)} below, which is tied to the payment backend system used.
     *
     * @param outcomeMessage The outcome message.
     * @return This builder
     */
    public TransactionResponseBuilder withOutcomeMessage(String outcomeMessage) {
        this.outcomeMessage = outcomeMessage;
        return this;
    }

    /**
     * Set the response code for this transaction.
     *
     * The range of values for this field is payment gateway / acquirer / host protocol specific.
     * See online documentation for more information.
     *
     * An example would be ISO-8583 2-alphanumeric character response codes.
     *
     * @param responseCode The response code
     * @return This builder
     */
    public TransactionResponseBuilder withResponseCode(String responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    /**
     * Add an extra/optional reference to pass back to the calling application.
     *
     * Examples of this might be the merchant id used, date time of the transaction, etc.
     *
     * See {@link AdditionalData#addData(String, Object[])} for more info.
     *
     * @param key    The key to use for this option
     * @param values An array of values for this option
     * @param <T>    The type of object this option is an array of
     * @return This builder
     */
    public <T> TransactionResponseBuilder withReference(String key, T... values) {
        references.addData(key, values);
        return this;
    }

    /**
     * Add extra/optional references to pass back to the calling application.
     *
     * Examples of this might be the merchant id used, date time of the transaction, etc.
     *
     * @param references The map of references
     * @return This builder.
     */
    public TransactionResponseBuilder withReferences(AdditionalData references) {
        if (references != null) {
            this.references = references;
        }
        return this;
    }

    /**
     * Build an instance of {@link TransactionResponse} with the provided parameters.
     *
     * @return The TransactionResponse instance
     */
    public TransactionResponse build() {
        checkArgument(id != null && !id.isEmpty(), "Request id must be set");
        checkArgument(outcome != null, "Outcome must be set");
        return new TransactionResponse(id, card, outcome, outcomeMessage, amounts, responseCode, references, paymentMethod);
    }
}


