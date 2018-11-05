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
import com.aevi.sdk.flow.model.Customer;
import com.aevi.util.json.JsonConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.aevi.sdk.pos.flow.model.TransactionResponse.Outcome.APPROVED;
import static com.aevi.sdk.pos.flow.model.TransactionResponse.Outcome.DECLINED;

/**
 * Represents a transaction within a flow.
 *
 * In the case of a split enabled flow, there will be an instance of this class for each "split", which usually represents each customer.
 *
 * {@link TransactionRequest} instances are created as required to call flow services within this transaction, with the remaining amounts to process.
 *
 * A transaction can contain zero to many {@link TransactionResponse} instances, as a result of calling into flow services that pay off a portion
 * or all of the requested amounts.
 *
 * Use {@link #getRequestedAmounts()} to retrieve the total amount requested for this transaction, and {@link #getRemainingAmounts()} to retrieve
 * the amounts remaining to pay for this transaction, if any.
 */
public class Transaction extends BaseModel {

    private Amounts requestedAmounts;
    private List<Basket> baskets;
    private Customer customer;
    private final AdditionalData additionalData;
    private final List<TransactionResponse> transactionResponses;
    private final List<FlowAppInfo> executedFlowApps;

    // Default constructor for deserialisation
    Transaction() {
        this(new Amounts(), null, null, null);
    }

    public Transaction(Amounts requestedAmounts, List<Basket> baskets, Customer customer, AdditionalData additionalData) {
        this(UUID.randomUUID().toString(), requestedAmounts, baskets, customer, additionalData, new ArrayList<TransactionResponse>(),
             new ArrayList<FlowAppInfo>());
    }

    Transaction(String id, Amounts requestedAmounts, List<Basket> baskets, Customer customer, AdditionalData additionalData,
                List<TransactionResponse> transactionResponses, List<FlowAppInfo> executedFlowApps) {
        super(id);
        this.requestedAmounts = requestedAmounts;
        this.baskets = baskets;
        this.customer = customer;
        this.additionalData = additionalData != null ? additionalData : new AdditionalData();
        this.executedFlowApps = executedFlowApps;
        this.transactionResponses = transactionResponses;
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
     * Get the list of baskets for this transaction
     *
     * @return The list of baskets
     */
    @NonNull
    public List<Basket> getBaskets() {
        if (baskets == null) {
            baskets = new ArrayList<>();
        }
        return baskets;
    }

    /**
     * Set the list of baskets for this transaction.
     *
     * @param baskets The list of baskets
     */
    public void setBaskets(List<Basket> baskets) {
        this.baskets = baskets;
    }

    /**
     * Get the customer details for this transaction.
     *
     * @return The customer details, or null if none set
     */
    @Nullable
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Set the customer details for this transaction
     *
     * @param customer The customer details
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
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
            if (transactionResponse.getOutcome() == APPROVED && transactionResponse.getAmountsProcessed() != null) {
                remaining = Amounts.subtractAmounts(remaining, transactionResponse.getAmountsProcessed());
            }
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
            if (transactionResponse.getOutcome() == APPROVED && transactionResponse.getAmountsProcessed() != null) {
                processed = Amounts.addAmounts(processed, transactionResponse.getAmountsProcessed());
            }
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
     * Check whether there were any declined responses.
     *
     * @return True if there were declined responses, false otherwise
     */
    public boolean hasDeclinedResponses() {
        for (TransactionResponse transactionResponse : transactionResponses) {
            if (transactionResponse.getOutcome() == DECLINED) {
                return true;
            }
        }
        return false;
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
     * @return The last transaction response, if one is available.
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
     *
     * @param transactionResponse Transaction response
     */
    public void addTransactionResponse(TransactionResponse transactionResponse) {
        this.transactionResponses.add(transactionResponse);
    }

    /**
     * For internal use
     *
     * @param executedFlowApp Flow app
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
                ", baskets=" + baskets +
                ", customer=" + customer +
                ", additionalData=" + additionalData +
                ", transactionResponses=" + transactionResponses +
                ", executedFlowApps=" + executedFlowApps +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Transaction that = (Transaction) o;
        return Objects.equals(requestedAmounts, that.requestedAmounts) &&
                Objects.equals(baskets, that.baskets) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(additionalData, that.additionalData) &&
                Objects.equals(transactionResponses, that.transactionResponses) &&
                Objects.equals(executedFlowApps, that.executedFlowApps);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), requestedAmounts, baskets, customer, additionalData, transactionResponses, executedFlowApps);
    }
}