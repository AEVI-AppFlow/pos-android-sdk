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

import com.aevi.sdk.flow.model.BaseModel;
import com.aevi.util.json.JsonConverter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Response for a previously made {@link Payment}.
 *
 * A payment can lead to a single or multiple transactions and it is up to the client to ensure this is taken into account.
 *
 * This class contains convenience methods to get overall information for the transaction processing.
 */
public class PaymentResponse extends BaseModel {

    /**
     * The outcome of a payment.
     */
    public enum Outcome {
        /**
         * The payment was fulfilled and the requested amount has been charged.
         */
        FULFILLED,
        /**
         * The payment was only partially fulfilled, meaning that part of the requested amount was charged.
         * This can either be due to partial auth (payment app approved less than requested) or one of the reasons in {@link FailureReason}.
         */
        PARTIALLY_FULFILLED,
        /**
         * The payment was not fulfilled and no money was charged.
         * See {@link FailureReason} for the reason of failure.
         */
        FAILED
    }

    /**
     * The reason for failure, if any.
     */
    public enum FailureReason {
        /**
         * No failure occurred
         */
        NONE,
        /**
         * The payment was cancelled before completing
         */
        CANCELLED,
        /**
         * The payment was rejected either due to incorrect parameters or due to payment app not being able to process it at this time
         */
        REJECTED,
        /**
         * The payment was declined, meaning that all transactions processed were declined
         */
        DECLINED,
        /**
         * The payment timed out before completing
         */
        TIMEOUT,
        /**
         * An error occurred that meant processing could not complete
         */
        ERROR
    }

    private Payment originatingPayment;

    protected Outcome outcome;
    protected FailureReason failureReason;
    protected String failureMessage;
    protected boolean allTransactionsApproved = true;
    protected Amounts totalAmountsProcessed;
    protected List<Transaction> transactions; // This will be a single item for most cases, but can be more for split
    protected FlowAppInfo executedPreFlowApp;
    protected FlowAppInfo executedPostFlowApp;

    // Default constructor for deserialisation
    PaymentResponse() {
        super("N/A");
        transactions = new CopyOnWriteArrayList<>();
        totalAmountsProcessed = new Amounts();
    }

    protected PaymentResponse(Payment payment) {
        this(payment.getId(), payment.getAmounts().getCurrency());
        originatingPayment = payment;
    }

    protected PaymentResponse(String paymentId, String currency) {
        super(paymentId);
        transactions = new CopyOnWriteArrayList<>();
        totalAmountsProcessed = new Amounts(0, currency);
    }

    /**
     * This can be called to check whether the payment was fulfilled via multiple split transactions or not.
     *
     * If true, the {@link #getTransactions()} method will return a list with a size greater than 1.
     *
     * @return True if split, false otherwise
     */
    public boolean isSplit() {
        return transactions.size() > 1;
    }

    /**
     * Get the initial {@link Payment} that this response is for.
     *
     * @return The {@link Payment}, if set.
     */
    @Nullable
    public Payment getOriginatingPayment() {
        return originatingPayment;
    }

    /**
     * Get the overall outcome of the payment.
     *
     * This will indicate whether the payment was fulfilled (requested amount processed), partially fulfilled (part of requested amount processed)
     * or failed (no amount processed).
     *
     * @return The overall outcome.
     */
    @NonNull
    public Outcome getOutcome() {
        return outcome;
    }

    /**
     * For {@link Outcome#PARTIALLY_FULFILLED} and {@link Outcome#FAILED}, this will return the reason for why the payment was not fully fulfilled.
     *
     * Note that there are some cases of {@link Outcome#PARTIALLY_FULFILLED} where this will be set to {@link FailureReason#NONE}. An example
     * of this is when the payment app approves an amount less than requested (partial auth), which is not a failure from a transactional point of view.
     *
     * @return The reason for failure.
     */
    @NonNull
    public FailureReason getFailureReason() {
        return failureReason;
    }

    /**
     * When a {@link FailureReason} has been set, this field can be set to provide additional information on what went wrong.
     *
     * @return The failure message. May be null.
     */
    @Nullable
    public String getFailureMessage() {
        return failureMessage;
    }

    /**
     * Convenience method to check whether all transactions that were processed
     * due to the originating payment was approved or not.
     *
     * Note - the payment can still have been fulfilled despite failed transactions, so this method
     * should NOT be used to check the outcome of the payment - use getOutcome() for that.
     *
     * @return True if all transactions were approved, false if some were declined or had errors.
     */
    public boolean isAllTransactionsApproved() {
        return !transactions.isEmpty() && allTransactionsApproved;
    }

    /**
     * Get the total amounts processed for the transactions that were carried out.
     *
     * This may not match the initially requested amounts. Note that {@link #getOutcome()} can be used to determine whether
     * a transaction was fulfilled, partially fulfilled or failed. This method is a convenience method to get the amount totals and not intended
     * to be used to investigate the outcome of the payment.
     *
     * Note that this may be null or contain empty amounts for non-purchase transactions. It also may be larger than the initially requested amounts.
     *
     * @return The total processed amounts. May be null.
     */
    @Nullable
    public Amounts getTotalAmountsProcessed() {
        return totalAmountsProcessed;
    }

    /**
     * Get the list of the customer transactions.
     *
     * This list will usually only contain one item, but can contain multiple items in split transaction scenarios.
     *
     * {{@link #isAllTransactionsApproved()}} can be used to check whether all transactions were approved.
     *
     * @return The list of {@link Transaction} items.
     */
    @NonNull
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Get the pre-flow app that was executed for this transaction, if any.
     *
     * @return The pre-flow app info, or null.
     */
    @Nullable
    public FlowAppInfo getExecutedPreFlowApp() {
        return executedPreFlowApp;
    }

    /**
     * For internal use.
     *
     * @param executedPreFlowApp Pre flow app
     */
    public void setExecutedPreFlowApp(FlowAppInfo executedPreFlowApp) {
        this.executedPreFlowApp = executedPreFlowApp;
    }

    /**
     * Get the post-flow app that was executed for this transaction, if any.
     *
     * @return The post-flow app info, or null.
     */
    @Nullable
    public FlowAppInfo getExecutedPostFlowApp() {
        return executedPostFlowApp;
    }

    /**
     * For internal use.
     *
     * @param executedPostFlowApp Post flow app
     */
    public void setExecutedPostFlowApp(FlowAppInfo executedPostFlowApp) {
        this.executedPostFlowApp = executedPostFlowApp;
    }

    /**
     * For internal use.
     *
     * @param originatingPayment The originating payment
     */
    public void setOriginatingPayment(Payment originatingPayment) {
        this.originatingPayment = originatingPayment;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static PaymentResponse fromJson(String json) {
        return JsonConverter.deserialize(json, PaymentResponse.class);
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
                "originatingRequest=" + originatingPayment +
                ", outcome=" + outcome +
                ", failureReason=" + failureReason +
                ", failureMessage='" + failureMessage + '\'' +
                ", allTransactionsApproved=" + allTransactionsApproved +
                ", totalAmountsProcessed=" + totalAmountsProcessed +
                ", transactions=" + transactions +
                ", executedPreFlowApp=" + executedPreFlowApp +
                ", executedPostFlowApp=" + executedPostFlowApp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentResponse paymentResponse = (PaymentResponse) o;

        if (allTransactionsApproved != paymentResponse.allTransactionsApproved) return false;
        if (originatingPayment != null ? !originatingPayment.equals(paymentResponse.originatingPayment) : paymentResponse.originatingPayment != null)
            return false;
        if (outcome != paymentResponse.outcome) return false;
        if (failureReason != paymentResponse.failureReason) return false;
        if (failureMessage != null ? !failureMessage.equals(paymentResponse.failureMessage) : paymentResponse.failureMessage != null) return false;
        if (totalAmountsProcessed != null ? !totalAmountsProcessed.equals(paymentResponse.totalAmountsProcessed) : paymentResponse.totalAmountsProcessed != null)
            return false;
        if (transactions != null ? !transactions.equals(paymentResponse.transactions) : paymentResponse.transactions != null) return false;
        if (executedPreFlowApp != null ? !executedPreFlowApp.equals(paymentResponse.executedPreFlowApp) : paymentResponse.executedPreFlowApp != null)
            return false;
        return executedPostFlowApp != null ? executedPostFlowApp.equals(paymentResponse.executedPostFlowApp) : paymentResponse.executedPostFlowApp == null;
    }

    @Override
    public int hashCode() {
        int result = originatingPayment != null ? originatingPayment.hashCode() : 0;
        result = 31 * result + (outcome != null ? outcome.hashCode() : 0);
        result = 31 * result + (failureReason != null ? failureReason.hashCode() : 0);
        result = 31 * result + (failureMessage != null ? failureMessage.hashCode() : 0);
        result = 31 * result + (allTransactionsApproved ? 1 : 0);
        result = 31 * result + (totalAmountsProcessed != null ? totalAmountsProcessed.hashCode() : 0);
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        result = 31 * result + (executedPreFlowApp != null ? executedPreFlowApp.hashCode() : 0);
        result = 31 * result + (executedPostFlowApp != null ? executedPostFlowApp.hashCode() : 0);
        return result;
    }
}
