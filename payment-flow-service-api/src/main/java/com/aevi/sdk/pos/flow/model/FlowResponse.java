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

import java.util.Arrays;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

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
    private Basket basket;
    private Customer customer;
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
        validateAmounts();
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
     * Add a basket.
     *
     * @param basket The basket
     */
    public void addBasket(Basket basket) {
        this.basket = basket;
    }

    /**
     * Update an existing basket.
     *
     * @param basketId    Basket id of existing basket
     * @param basketItems Basket items to add
     */
    public void updateBasket(String basketId, BasketItem... basketItems) {
        this.basket = new Basket(basketId, "", Arrays.asList(basketItems));
    }

    /**
     * Get basket.
     *
     * @return The basket
     */
    @Nullable
    public Basket getBasket() {
        return basket;
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
        validateAmounts();
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
     * Set payment references.
     *
     * @param paymentReferences The references via Options
     */
    public void setPaymentReferences(AdditionalData paymentReferences) {
        this.paymentReferences = paymentReferences;
    }

    /**
     * Check whether this response has any augmented data.
     *
     * @return True if data has been augmented, false otherwisee
     */
    public boolean hasAugmentedData() {
        return requestAdditionalData != null || updatedRequestAmounts != null || amountsPaid != null || paymentReferences != null || enableSplit;
    }

    /**
     * Check whether split should be enabled.
     *
     * @return True if split should be enabled
     */
    public boolean shouldEnableSplit() {
        return enableSplit;
    }

    /**
     * Enable or disable split.
     *
     * @param enableSplit True to enable split, false to disable it
     */
    public void setEnableSplit(boolean enableSplit) {
        this.enableSplit = enableSplit;
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
        this.enableSplit = flowResponse.enableSplit;
        this.cancelTransaction = flowResponse.cancelTransaction;
    }

    private void validateAmounts() {
        if (amountsPaid != null && updatedRequestAmounts != null) {
            checkCurrencyMatch();
            checkAmountsPaidLessEqualUpdatedAmounts();
        }
    }

    private void checkAmountsPaidLessEqualUpdatedAmounts() {
        checkArgument(amountsPaid.getBaseAmountValue() <= updatedRequestAmounts.getBaseAmountValue(),
                      "Amounts paid can not be > than updated amounts");
    }

    private void checkCurrencyMatch() {
        checkArgument(updatedRequestAmounts.getCurrency() == null || updatedRequestAmounts.getCurrency().equals(amountsPaid.getCurrency()),
                      "Updated amounts and amounts paid currency must be the same");
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

        if (enableSplit != that.enableSplit) {
            return false;
        }
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (updatedRequestAmounts != null ? !updatedRequestAmounts.equals(that.updatedRequestAmounts) : that.updatedRequestAmounts != null) {
            return false;
        }
        if (requestAdditionalData != null ? !requestAdditionalData.equals(that.requestAdditionalData) : that.requestAdditionalData != null) {
            return false;
        }
        if (amountsPaid != null ? !amountsPaid.equals(that.amountsPaid) : that.amountsPaid != null) {
            return false;
        }
        if (amountsPaidPaymentMethod != null ? !amountsPaidPaymentMethod.equals(that.amountsPaidPaymentMethod) :
                that.amountsPaidPaymentMethod != null) {
            return false;
        }
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

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static FlowResponse fromJson(String json) {
        return JsonConverter.deserialize(json, FlowResponse.class);
    }
}
