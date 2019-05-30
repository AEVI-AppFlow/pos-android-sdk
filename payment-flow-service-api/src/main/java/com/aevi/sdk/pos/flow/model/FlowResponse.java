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

import android.support.annotation.Nullable;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.util.json.JsonConverter;
import com.aevi.util.json.Sendable;

import java.util.List;
import java.util.Objects;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;
import static com.aevi.sdk.flow.util.Preconditions.checkNotEmpty;

/**
 * Response model for augmenting the flow.
 *
 * Note that this is intended for internal use by the stage models.
 */
public class FlowResponse implements Sendable {

    private String id;
    private Amounts updatedRequestAmounts;
    private AdditionalData requestAdditionalData;

    private Amounts amountsPaid;
    private Basket additionalBasket;
    private Basket modifiedBasket;
    private Customer customer;
    private String amountsPaidPaymentMethod;
    private Payment updatedPayment;

    private AdditionalData paymentReferences;

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

    /**
     * For internal use.
     *
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get updated request amounts.
     *
     * @return The updated request amounts
     */
    @Nullable
    public Amounts getUpdatedRequestAmounts() {
        return updatedRequestAmounts;
    }

    /**
     * Update request amounts.
     *
     * @param modifiedRequestAmounts The updated amounts
     */
    public void updateRequestAmounts(Amounts modifiedRequestAmounts) {
        this.updatedRequestAmounts = modifiedRequestAmounts;
    }

    /**
     * Add or update customer details.
     *
     * @param customer The customer details
     */
    public void addOrUpdateCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Get the customer details
     *
     * @return The customer details
     */
    @Nullable
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Add a new Basket.
     *
     * @param basket The new Basket
     */
    public void addNewBasket(Basket basket) {
        this.additionalBasket = basket;
    }

    /**
     * Update an existing basket.
     *
     * @param basketId    Basket id of existing basket
     * @param basketItems Basket items to add
     */
    public void updateBasket(String basketId, List<BasketItem> basketItems) {
        this.modifiedBasket = new Basket(basketId, "flowBasketUpdates", basketItems);
    }

    /**
     * Get additional basket.
     *
     * @return The additional basket
     */
    @Nullable
    public Basket getAdditionalBasket() {
        return additionalBasket;
    }

    /**
     * Get modified basket.
     *
     * @return The modified basket
     */
    @Nullable
    public Basket getModifiedBasket() {
        return modifiedBasket;
    }

    /**
     * Get the amounts paid.
     *
     * @return The amounts paid
     */
    @Nullable
    public Amounts getAmountsPaid() {
        return amountsPaid;
    }

    /**
     * Get the amounts paid method.
     *
     * @return The amounts paid method
     */
    @Nullable
    public String getAmountsPaidPaymentMethod() {
        return amountsPaidPaymentMethod;
    }

    /**
     * Set amounts paid.
     *
     * @param amountsPaid   The amounts paid
     * @param paymentMethod The method of payment
     */
    public void setAmountsPaid(Amounts amountsPaid, String paymentMethod) {
        this.amountsPaid = amountsPaid;
        this.amountsPaidPaymentMethod = paymentMethod;
    }

    /**
     * Get request additional data.
     *
     * @return The request additional data
     */
    @Nullable
    public AdditionalData getRequestAdditionalData() {
        return requestAdditionalData;
    }

    /**
     * Add request data.
     *
     * @param key    The key to use for this data
     * @param values An array of values for this data
     * @param <T>    The type of object this data is an array of
     */
    @SafeVarargs
    public final <T> void addAdditionalRequestData(String key, T... values) {
        if (requestAdditionalData == null) {
            requestAdditionalData = new AdditionalData();
        }
        requestAdditionalData.addData(key, values);
    }

    /**
     * Set additional request data.
     *
     * @param requestAdditionalData Additional data to set in the request.
     */
    public void setAdditionalRequestData(AdditionalData requestAdditionalData) {
        this.requestAdditionalData = requestAdditionalData;
    }

    /**
     * Get the payment references.
     *
     * @return The payment references
     */
    @Nullable
    public AdditionalData getPaymentReferences() {
        return paymentReferences;
    }

    /**
     * Add payment reference.
     *
     * @param key    The key to use for this data
     * @param values An array of values for this data
     * @param <T>    The type of object this data is an array of
     */
    @SafeVarargs
    public final <T> void addPaymentReference(String key, T... values) {
        if (paymentReferences == null) {
            paymentReferences = new AdditionalData();
        }
        paymentReferences.addData(key, values);
    }

    /**
     * Set payment references.
     *
     * @param paymentReferences The references via Options
     */
    public void setPaymentReferences(AdditionalData paymentReferences) {
        this.paymentReferences = paymentReferences;
    }

    /**
     * Get the updated payment.
     *
     * @return The updated payment
     */
    public Payment getUpdatedPayment() {
        return updatedPayment;
    }

    /**
     * Set the updated payment.
     *
     * @param updatedPayment The updated payment
     */
    public void setUpdatedPayment(Payment updatedPayment) {
        this.updatedPayment = updatedPayment;
    }

    /**
     * Check whether this response has any augmented data.
     *
     * @return True if data has been augmented, false otherwisee
     */
    public boolean hasAugmentedData() {
        return requestAdditionalData != null || updatedRequestAmounts != null || amountsPaid != null || paymentReferences != null ||
                additionalBasket != null || modifiedBasket != null || customer != null || updatedPayment != null;
    }

    /**
     * Check whether to cancel the transaction / flow.
     *
     * @return True to cancel, false otherwise
     */
    public boolean shouldCancelTransaction() {
        return cancelTransaction;
    }

    /**
     * Cancel transaction.
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

    /**
     * For internal use.
     *
     * @param flowResponse Flow response
     */
    public void copyFrom(FlowResponse flowResponse) {
        this.id = flowResponse.id;
        this.updatedRequestAmounts = flowResponse.updatedRequestAmounts;
        this.requestAdditionalData = flowResponse.requestAdditionalData;
        this.amountsPaid = flowResponse.amountsPaid;
        this.amountsPaidPaymentMethod = flowResponse.amountsPaidPaymentMethod;
        this.paymentReferences = flowResponse.paymentReferences;
        this.cancelTransaction = flowResponse.cancelTransaction;
        this.additionalBasket = flowResponse.additionalBasket;
        this.modifiedBasket = flowResponse.modifiedBasket;
        this.customer = flowResponse.customer;
    }

    private void validateAmounts() {
        if (amountsPaid != null && updatedRequestAmounts != null) {
            checkCurrencyMatch();
            checkAmountsPaidLessEqualUpdatedAmounts();
            checkNotEmpty(amountsPaidPaymentMethod, "Payment method must be set for paid amounts");
        }
    }

    private void checkAmountsPaidLessEqualUpdatedAmounts() {
        checkArgument(amountsPaid.getBaseAmountValue() <= updatedRequestAmounts.getBaseAmountValue(),
                      "Amounts paid can not be > than updated amounts");
    }

    private void checkCurrencyMatch() {
        checkArgument(updatedRequestAmounts.getCurrency().equals(amountsPaid.getCurrency()),
                      "Updated amounts and amounts paid currency must be the same");
    }

    public void validate() {
        validateAmounts();
        if (modifiedBasket != null) {
            if (amountsPaid == null || amountsPaid.getTotalAmountValue() < modifiedBasket.getTotalBasketValue()) {
                throw new IllegalArgumentException("Amounts paid must be set when applying discounts to baskets");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowResponse that = (FlowResponse) o;
        return cancelTransaction == that.cancelTransaction &&
                Objects.equals(id, that.id) &&
                Objects.equals(updatedRequestAmounts, that.updatedRequestAmounts) &&
                Objects.equals(requestAdditionalData, that.requestAdditionalData) &&
                Objects.equals(amountsPaid, that.amountsPaid) &&
                Objects.equals(additionalBasket, that.additionalBasket) &&
                Objects.equals(modifiedBasket, that.modifiedBasket) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(amountsPaidPaymentMethod, that.amountsPaidPaymentMethod) &&
                Objects.equals(updatedPayment, that.updatedPayment) &&
                Objects.equals(paymentReferences, that.paymentReferences);
    }

    @Override
    public int hashCode() {

        return Objects
                .hash(id, updatedRequestAmounts, requestAdditionalData, amountsPaid, additionalBasket, modifiedBasket, customer,
                      amountsPaidPaymentMethod,
                      updatedPayment, paymentReferences, cancelTransaction);
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static FlowResponse fromJson(String json) {
        return JsonConverter.deserialize(json, FlowResponse.class);
    }
}
