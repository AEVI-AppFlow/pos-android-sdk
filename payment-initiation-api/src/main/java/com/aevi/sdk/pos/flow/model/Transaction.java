package com.aevi.sdk.pos.flow.model;


import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.BaseModel;
import com.aevi.util.json.JsonConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Represents a transaction for a single customer (aka split).
 *
 * A transaction can contain one or more {@link TransactionResponse} from different sources, using potentially different payment methods.
 *
 * Use {@link #getRequestedAmounts()} to retrieve the total amount requested for this transaction, and {@link #getRemainingAmounts()} to retrieve
 * the amounts remaining to pay for this transaction, if any.
 */
public class Transaction extends BaseModel {

    private Amounts requestedAmounts;
    private final AdditionalData additionalData;
    private final List<TransactionResponse> transactionResponses;
    private final List<FlowAppInfo> executedFlowApps;

    public Transaction(Amounts requestedAmounts) {
        this(requestedAmounts, new AdditionalData());
    }

    public Transaction(Amounts requestedAmounts, AdditionalData additionalData) {
        super(UUID.randomUUID().toString());
        this.requestedAmounts = requestedAmounts;
        this.additionalData = additionalData != null ? additionalData : new AdditionalData();
        executedFlowApps = new ArrayList<>();
        transactionResponses = new ArrayList<>();
    }

    public Transaction(Transaction other, Amounts newAmounts) {
        super(other.getId());
        this.additionalData = other.additionalData;
        this.executedFlowApps = other.executedFlowApps;
        this.transactionResponses = other.transactionResponses;
        this.requestedAmounts = newAmounts;
    }

    /**
     * Update the request amounts.
     *
     * @param newAmounts The new request amounts.
     */
    public void updateAmounts(Amounts newAmounts) {
        this.requestedAmounts = newAmounts;
    }

    /**
     * Additional data such as basket, customer, etc.
     *
     * @return The data
     */
    @NonNull
    public AdditionalData getAdditionalData() {
        return additionalData;
    }

    /**
     * Get the amounts requested for this transaction.
     *
     * Note that this should not be used by flow apps to determine how much is left to charge the customer.
     * Please use {@link #getRemainingAmounts()} for this.
     *
     * @return The requested amounts
     */
    @NonNull
    public Amounts getRequestedAmounts() {
        return requestedAmounts;
    }

    /**
     * Get the remaining amounts required to fulfill the requested amounts for this transaction.
     *
     * This shall be used to determine how much to charge the customer at any given point.
     *
     * @return The remaining amounts.
     */
    @NonNull
    public Amounts getRemainingAmounts() {
        Amounts remaining = new Amounts(requestedAmounts);
        for (TransactionResponse transactionResponse : transactionResponses) {
            remaining = Amounts.subtractAmounts(remaining, transactionResponse.getAmountsProcessed());
        }
        return remaining;
    }

    /**
     * Get the amounts processed at this point in time.
     *
     * Note that the processed amount may be larger than the requested amounts.
     *
     * @return The amounts processed at this point in time.
     */
    @NonNull
    public Amounts getProcessedAmounts() {
        Amounts processed = new Amounts(0, requestedAmounts.getCurrency());
        for (TransactionResponse transactionResponse : transactionResponses) {
            processed = Amounts.addAmounts(processed, transactionResponse.getAmountsProcessed());
        }
        return processed;
    }

    /**
     * Check whether the requested amounts have been fully processed.
     *
     * @return True if the amounts have been fully processed, false otherwise.
     */
    public boolean hasProcessedRequestedAmounts() {
        return getRemainingAmounts().getTotalAmountValue() == 0;
    }

    /**
     * Check whether this transaction has any responses or not yet.
     *
     * @return True if there are transaction responses, false otherwise.
     */
    public boolean hasResponses() {
        return transactionResponses.size() > 0;
    }

    /**
     * Get the list of transaction responses for this transaction.
     *
     * A transaction will have one response for each application that has paid the full or a portion of the requested amounts.
     *
     * As an example, the first entry might be a loyalty app that has paid 20% of the amounts, followed by a second entry where the
     * payment application charges the customer for the remaining 80%.
     *
     * @return The list of transaction responses.
     */
    public List<TransactionResponse> getTransactionResponses() {
        return transactionResponses;
    }

    /**
     * Get the last transaction response, if any.
     *
     * @return The last transaction, if one is available.
     */
    @Nullable
    public TransactionResponse getLastResponse() {
        if (transactionResponses.size() > 0) {
            return transactionResponses.get(transactionResponses.size() - 1);
        }
        return null;
    }

    /**
     * Get the list of executed flow apps for this transaction.
     *
     * @return The list of executed flow apps
     */
    @NonNull
    public List<FlowAppInfo> getExecutedFlowApps() {
        return executedFlowApps;
    }

    /**
     * For internal use.
     */
    public void addTransactionResponse(TransactionResponse transactionResponse) {
        this.transactionResponses.add(transactionResponse);
    }

    /**
     * For internal use
     */
    public void addExecutedFlowApp(FlowAppInfo executedFlowApp) {
        executedFlowApps.add(executedFlowApp);
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "requestedAmounts=" + requestedAmounts +
                ", additionalData=" + additionalData +
                ", transactionResponses=" + transactionResponses +
                ", executedFlowApps=" + executedFlowApps +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Transaction that = (Transaction) o;

        if (requestedAmounts != null ? !requestedAmounts.equals(that.requestedAmounts) : that.requestedAmounts != null) return false;
        if (additionalData != null ? !additionalData.equals(that.additionalData) : that.additionalData != null) return false;
        if (transactionResponses != null ? !transactionResponses.equals(that.transactionResponses) : that.transactionResponses != null) return false;
        return executedFlowApps != null ? executedFlowApps.equals(that.executedFlowApps) : that.executedFlowApps == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (requestedAmounts != null ? requestedAmounts.hashCode() : 0);
        result = 31 * result + (additionalData != null ? additionalData.hashCode() : 0);
        result = 31 * result + (transactionResponses != null ? transactionResponses.hashCode() : 0);
        result = 31 * result + (executedFlowApps != null ? executedFlowApps.hashCode() : 0);
        return result;
    }
}