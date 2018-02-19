package com.aevi.sdk.pos.flow.model;

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Sendable;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

/**
 * Response model for a flow app to augment data or add information to influence the transaction from this point forward.
 *
 * Note that what is allowed to augment and when depends on the current payment stage. Please see documentation for more information.
 */
public class FlowResponse implements Sendable {

    private String id;
    private FlowAmounts updatedRequestAmounts;
    private AdditionalData requestAdditionalData;

    private Amounts amountsPaid;
    private String amountsPaidPaymentMethod;

    private AdditionalData paymentReferences;

    private boolean enableSplit;
    private boolean cancelTransaction;

    public FlowResponse() {
    }

    /**
     * For internal use.
     *
     * @param id FlowRequest id
     */
    public FlowResponse(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FlowAmounts getUpdatedRequestAmounts() {
        return updatedRequestAmounts;
    }

    /**
     * This can be used to update the amount the payment app should charge the customer for this particular transaction.
     *
     * A typical use case for this is split transactions, when the current remaining amount is split and the current transaction is set up
     * to pay a share of that remaining sum.
     *
     * Note that the total amount across all transactions is NOT reduced by this - the remaining balance is just shifted forward.
     *
     * This can be combined with setting amounts paid, typically for a split case where the amount is modified and paid via cash (for instance).
     *
     * This call won't have any effect if set after {@link PaymentStage#TRANSACTION_PROCESSING}.
     *
     * @param updatedAmountsToCharge The updated amounts
     */
    public void updateRequestAmounts(FlowAmounts updatedAmountsToCharge) {
        this.updatedRequestAmounts = updatedAmountsToCharge;
        validateAmounts();
    }

    public Amounts getAmountsPaid() {
        return amountsPaid;
    }

    public String getAmountsPaidPaymentMethod() {
        return amountsPaidPaymentMethod;
    }

    /**
     * This can be used to set an amount that has been paid for outside of the payment app.
     *
     * The use cases for this involves the customer paying part or all of the amounts owed via means other than payment cards.
     * Examples are loyalty points, cash, etc.
     *
     * If this amount is less than the overall amount for the transaction, the remaining amount will be processed by the payment app.
     *
     * If this amount equals the overall (original or updated) amounts, the transaction will be considered fulfilled and completed.
     *
     * Optional reference data can be set via {@link #setPaymentReferences(AdditionalData)}
     *
     * This call won't have any effect if set after {@link PaymentStage#TRANSACTION_PROCESSING}.
     *
     * @param amountsPaid   The amounts paid
     * @param paymentMethod The method of payment
     */
    public void setAmountsPaid(Amounts amountsPaid, String paymentMethod) {
        this.amountsPaid = amountsPaid;
        this.amountsPaidPaymentMethod = paymentMethod;
        validateAmounts();
    }

    public AdditionalData getRequestAdditionalData() {
        return requestAdditionalData;
    }

    /**
     * This can be used to set arbitrary data to be passed on in the request to down-stream flow apps and/or payment apps.
     *
     * This will overwrite any existing options with the same key.
     *
     * This call won't have any effect if set after {@link PaymentStage#TRANSACTION_PROCESSING}.
     *
     * @param requestAdditionalData Options to set in the request.
     */
    public void setRequestAdditionalData(AdditionalData requestAdditionalData) {
        this.requestAdditionalData = requestAdditionalData;
    }

    public AdditionalData getPaymentReferences() {
        return paymentReferences;
    }

    /**
     * Optional references that are added to the transaction response references.
     *
     * If used together with {@link #setAmountsPaid(Amounts, String)}, this will be set in the transaction response.
     *
     * If set by a post-transaction application, these will be copied into the existing transaction response references. Note that overwriting
     * existing values will NOT be allowed in this case.
     *
     * @param paymentReferences The references via Options
     */
    public void setPaymentReferences(AdditionalData paymentReferences) {
        this.paymentReferences = paymentReferences;
    }

    public boolean hasAugmentedData() {
        return requestAdditionalData != null || updatedRequestAmounts != null || amountsPaid != null || paymentReferences != null || enableSplit;
    }

    public boolean shouldEnableSplit() {
        return enableSplit;
    }

    /**
     * This can be used to override the splitEnabled flag in the source request, to allow the transaction to be split.
     *
     * Note that this call only has an effect in the {@link PaymentStage#PRE_FLOW} stage.
     *
     * @param enableSplit True to enable split, false to disable it
     */
    public void setEnableSplit(boolean enableSplit) {
        this.enableSplit = enableSplit;
    }

    public boolean shouldCancelTransaction() {
        return cancelTransaction;
    }

    /**
     * Request that the current transaction should be cancelled. Note that depending on the transaction stage and state,
     * this request may not be respected.
     *
     * This call won't have any effect if set after {@link PaymentStage#TRANSACTION_PROCESSING}.
     *
     * @param cancelTransaction True if transaction should be cancelled.
     */
    public void setCancelTransaction(boolean cancelTransaction) {
        this.cancelTransaction = cancelTransaction;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    private void validateAmounts() {
        if (amountsPaid != null && updatedRequestAmounts != null) {
            checkCurrencyMatch();
            checkAmountsPaidLessEqualUpdatedAmounts();
        }
    }

    private void checkAmountsPaidLessEqualUpdatedAmounts() {
        checkArgument(amountsPaid.getBaseAmountValue() <= updatedRequestAmounts.getBaseAmount(), "Amounts paid can not be > than updated amounts");
    }

    private void checkCurrencyMatch() {
        checkArgument(updatedRequestAmounts.getCurrency() == null || updatedRequestAmounts.getCurrency().equals(amountsPaid.getCurrency()),
                "Updated amounts and amounts paid currency must be the same");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowResponse that = (FlowResponse) o;

        if (enableSplit != that.enableSplit) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (updatedRequestAmounts != null ? !updatedRequestAmounts.equals(that.updatedRequestAmounts) : that.updatedRequestAmounts != null)
            return false;
        if (requestAdditionalData != null ? !requestAdditionalData.equals(that.requestAdditionalData) : that.requestAdditionalData != null)
            return false;
        if (amountsPaid != null ? !amountsPaid.equals(that.amountsPaid) : that.amountsPaid != null) return false;
        if (amountsPaidPaymentMethod != null ? !amountsPaidPaymentMethod.equals(that.amountsPaidPaymentMethod) : that.amountsPaidPaymentMethod != null)
            return false;
        return paymentReferences != null ? paymentReferences.equals(that.paymentReferences) : that.paymentReferences == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (updatedRequestAmounts != null ? updatedRequestAmounts.hashCode() : 0);
        result = 31 * result + (requestAdditionalData != null ? requestAdditionalData.hashCode() : 0);
        result = 31 * result + (amountsPaid != null ? amountsPaid.hashCode() : 0);
        result = 31 * result + (amountsPaidPaymentMethod != null ? amountsPaidPaymentMethod.hashCode() : 0);
        result = 31 * result + (paymentReferences != null ? paymentReferences.hashCode() : 0);
        result = 31 * result + (enableSplit ? 1 : 0);
        return result;
    }
}
