package com.aevi.sdk.pos.flow.model;

import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Sendable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Response for a previously made {@link Request}.
 *
 * A request can lead to a single or multiple transactions and it is up to the client to ensure this is taken into account.
 *
 * This class contains convenience methods to get overall information for the transaction processing.
 */
public class Response implements Sendable {

    /**
     * The outcome of a request.
     */
    public enum Outcome {
        /**
         * The request was fulfilled and the requested amount has been charged.
         */
        FULFILLED,
        /**
         * The request was only partially fulfilled, meaning that part of the requested amount was charged.
         * This can either be due to partial auth (payment app approved less than requested) or one of the reasons in {@link FailureReason}.
         */
        PARTIALLY_FULFILLED,
        /**
         * The request was not fulfilled and no money was charged.
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
         * The request was cancelled before completing
         */
        CANCELLED,
        /**
         * The request was rejected either due to incorrect parameters or due to payment app not being able to process it at this time
         */
        REJECTED,
        /**
         * The request was declined, meaning that all transactions processed were declined
         */
        DECLINED,
        /**
         * The request timed out before completing
         */
        TIMEOUT,
        /**
         * An error occurred that meant processing could not complete
         */
        ERROR
    }

    private final Request originatingRequest;

    protected Outcome outcome;
    protected FailureReason failureReason;
    protected String failureMessage;
    protected boolean allTransactionsApproved = true;
    protected Amounts totalAmountsProcessed;
    protected List<Transaction> transactions; // This will be a single item for most cases, but can be more for split
    protected FlowAppInfo executedPreFlowApp;
    protected FlowAppInfo executedPostFlowApp;

    protected Response(Request originatingRequest) {
        this.originatingRequest = originatingRequest;
        transactions = new CopyOnWriteArrayList<>();
        totalAmountsProcessed = new Amounts(0, originatingRequest.getAmounts().getCurrency());
    }

    /**
     * This can be called to check whether the request was fulfilled via multiple split transactions or not.
     *
     * If true, the {@link #getTransactions()} method will return a list with a size greater than 1.
     *
     * @return True if split, false otherwise
     */
    public boolean isSplit() {
        return transactions.size() > 1;
    }

    @Override
    @NonNull
    public String getId() {
        return originatingRequest.getId();
    }

    /**
     * Get the initial {@link Request} that this response is for.
     *
     * @return The {@link Request}
     */
    @NonNull
    public Request getOriginatingRequest() {
        return originatingRequest;
    }

    /**
     * Get the overall outcome of the request.
     *
     * This will indicate whether the request was fulfilled (requested amount processed), partially fulfilled (part of requested amount processed)
     * or failed (no amount processed).
     *
     * @return The overall outcome.
     */
    @NonNull
    public Outcome getOutcome() {
        return outcome;
    }

    /**
     * For {@link Outcome#PARTIALLY_FULFILLED} and {@link Outcome#FAILED}, this will return the reason for why the request was not fully fulfilled.
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
     * due to the originating request was approved or not.
     *
     * Note - the request can still have been fulfilled despite failed transactions, so this method
     * should NOT be used to check the outcome of the request - use getOutcome() for that.
     *
     * @return True if all transactions were approved, false if some were declined or had errors.
     */
    public boolean isAllTransactionsApproved() {
        return allTransactionsApproved;
    }

    /**
     * Get the total amounts processed for the transactions that were carried out.
     *
     * This may not match the initially requested amounts. Note that {@link #getOutcome()} can be used to determine whether
     * a transaction was fulfilled, partially fulfilled or failed. This method is a convenience method to get the amount totals and not intended
     * to be used to investigate the outcome of the request.
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
     */
    public void setExecutedPostFlowApp(FlowAppInfo executedPostFlowApp) {
        this.executedPostFlowApp = executedPostFlowApp;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static Response fromJson(String json) {
        return JsonConverter.deserialize(json, Response.class);
    }

    @Override
    public String toString() {
        return "Response{" +
                "originatingRequest=" + originatingRequest +
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

        Response response = (Response) o;

        if (allTransactionsApproved != response.allTransactionsApproved) return false;
        if (originatingRequest != null ? !originatingRequest.equals(response.originatingRequest) : response.originatingRequest != null) return false;
        if (outcome != response.outcome) return false;
        if (failureReason != response.failureReason) return false;
        if (failureMessage != null ? !failureMessage.equals(response.failureMessage) : response.failureMessage != null) return false;
        if (totalAmountsProcessed != null ? !totalAmountsProcessed.equals(response.totalAmountsProcessed) : response.totalAmountsProcessed != null)
            return false;
        if (transactions != null ? !transactions.equals(response.transactions) : response.transactions != null) return false;
        if (executedPreFlowApp != null ? !executedPreFlowApp.equals(response.executedPreFlowApp) : response.executedPreFlowApp != null) return false;
        return executedPostFlowApp != null ? executedPostFlowApp.equals(response.executedPostFlowApp) : response.executedPostFlowApp == null;
    }

    @Override
    public int hashCode() {
        int result = originatingRequest != null ? originatingRequest.hashCode() : 0;
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
