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

package com.aevi.sdk.pos.flow.stage;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.Customer;
import com.aevi.sdk.flow.model.Token;
import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.model.*;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;

import java.util.List;

import static com.aevi.sdk.flow.service.ActivityHelper.ACTIVITY_REQUEST_KEY;
import static com.aevi.sdk.flow.util.Preconditions.*;

/**
 * Model for the pre-transaction stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService#onPreFlow(PreFlowModel)} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 *
 * If data has been augmented, {@link #sendResponse()} must be called for these changes to be applied. If called with no changes, it has the same
 * effect as calling {@link #skip()}.
 *
 * If no changes are required, call {@link #skip()}.
 *
 * @see <a href="https://github.com/AEVI-AppFlow/pos-android-sdk/wiki/implementing-flow-services" target="_blank">Implementing Flow Services</a>
 */
public class PreTransactionModel extends BaseStageModel {

    private final TransactionRequest transactionRequest;
    private final AmountsModifier amountsModifier;
    private final FlowResponse flowResponse;

    private PreTransactionModel(Activity activity, @NonNull TransactionRequest transactionRequest) {
        super(activity);
        this.transactionRequest = transactionRequest;
        this.amountsModifier = new AmountsModifier(transactionRequest.getAmounts());
        this.flowResponse = new FlowResponse();
    }

    private PreTransactionModel(ClientCommunicator clientCommunicator, @NonNull TransactionRequest transactionRequest) {
        super(clientCommunicator);
        this.transactionRequest = transactionRequest;
        this.amountsModifier = new AmountsModifier(transactionRequest.getAmounts());
        this.flowResponse = new FlowResponse();
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link BaseStageModel#processInActivity(Context, Class)},
     * or via the {@link ActivityProxyService}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link PreTransactionModel}
     */
    public static PreTransactionModel fromActivity(Activity activity) {
        String request = activity.getIntent().getStringExtra(ACTIVITY_REQUEST_KEY);
        return new PreTransactionModel(activity, TransactionRequest.fromJson(request));
    }

    /**
     * Create an instance from a service context.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request            The deserialised Payment provided as a string via {@link BaseApiService#processRequest(ClientCommunicator, String, String)}
     * @return An instance of {@link PreTransactionModel}
     */
    public static PreTransactionModel fromService(ClientCommunicator clientCommunicator, TransactionRequest request) {
        return new PreTransactionModel(clientCommunicator, request);
    }

    /**
     * Get the transaction request.
     *
     * @return The transaction request
     */
    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * Change the currency associated with the amounts, provided that it is allowed.
     *
     * It is up to the flow processing service configuration if currency conversion is allowed or not.
     * Use {@link PaymentClient#getPaymentSettings()} to get hold of FpsSettings to check whether it is allowed or not.
     *
     * Note that this will update all the amount values based on the provided exchange rate.
     *
     * The provided exchange rate must be the rate for converting the existing currency into the updated currency.
     *
     * As an example - if the current currency is USD and the updated currency is GBP, the exchange rate passed in must
     * be the USD to GBP rate.
     *
     * @param currency     The new ISO-4217 currency code
     * @param exchangeRate The exchange rate (from original currency to updated currency)
     * @throws IllegalArgumentException If currency is not set
     */
    public void changeCurrency(String currency, double exchangeRate) {
        checkNotEmpty(currency, "Currency must be set");
        amountsModifier.changeCurrency(currency, exchangeRate);
    }

    /**
     * Set or update an additional payment amount with an amount value.
     *
     * This is typically used to add a fee/charge, charity contribution, etc. Note that this should never be used to represent the value of any
     * goods or services.
     *
     * Must be 0 or greater.
     *
     * @param identifier The amount identifier
     * @param amount     The amount value
     * @throws IllegalArgumentException If identifier or amount are invalid
     */
    public void setAdditionalAmount(String identifier, long amount) {
        checkNotEmpty(identifier, "Identifier must be set");
        checkNotNegative(amount, "Amount must be zero or greater");
        amountsModifier.setAdditionalAmount(identifier, amount);
    }

    /**
     * Set or update an additional amount as a fraction of the base amount.
     *
     * This is useful for cases where a fee, charity contribution, etc is calculated as a fraction or percentage of the base amount.
     * Note that this should never be used to represent the value of any goods or services.
     *
     * @param identifier The string identifier for the amount
     * @param fraction   The fraction of the base amount, ranging from 0.0 to 1.0f (0% to 100%)
     * @throws IllegalArgumentException If identifier is not set
     */
    public void setAdditionalAmountAsBaseFraction(String identifier, float fraction) {
        checkNotEmpty(identifier, "Identifier must be set");
        checkNotNegative(fraction, "Fractions must not be negative");
        amountsModifier.setAdditionalAmountAsBaseFraction(identifier, fraction);
    }

    /**
     * Add request key/value pair data to the request.
     *
     * The value of this can be any arbitrary data or collection of data.
     *
     * See {@link AdditionalData#addData(String, Object[])} for more info.
     *
     * @param key    The key to use for this data
     * @param values A var-args input of values associated with the key
     * @param <T>    The type of object this data is an array of
     * @throws IllegalArgumentException If key or values are not set
     */
    @SafeVarargs
    public final <T> void addRequestData(String key, T... values) {
        checkNotNull(key, "Key must be set");
        checkNotEmpty(values, "At least one value must be provided");
        flowResponse.addAdditionalRequestData(key, values);
    }

    /**
     * Add a new basket to the transaction.
     *
     * This method can be used to add extras/upsells/etc to a purchase, whereas {@link #applyDiscountsToBasket(String, List, String)} can be used
     * to apply discounts to an existing basket.
     *
     * Note that it is not allowed to add additional items to the primary basket - only via new baskets.
     *
     * Only one basket can be added. Any subsequent calls to this method will overwrite any previous baskets.
     *
     * The transaction base amount will automatically be updated to take the value of this basket into account.
     *
     * @param basket The basket to add
     * @throws IllegalArgumentException If the total basket value is not greater than zero
     */
    public void addNewBasket(Basket basket) {
        checkNotNull(basket, "Basket must not be null");
        checkNotEmpty(basket.getBasketItems(), "At least one basket item must be set");
        if (basket.getTotalBasketValue() < 0) {
            throw new IllegalArgumentException("Total basket value must be greater than or equal zero");
        }
        flowResponse.addNewBasket(basket);
        amountsModifier.offsetBaseAmount(basket.getTotalBasketValue());
    }

    /**
     * Apply discounts to an existing basket.
     *
     * This can be used to apply rewards, points, etc for particular basket items. As an example, this can be used to provide a reward for a
     * free coffee. If the basket contains an item "Latte" with an amount of "300", the reward can be applied by adding an item here
     * with a negative amount of "-300" and a label such as "Reward: Free Latte".
     *
     * The discounts here will automatically be recorded as amounts paid, there is no need to call {@link #setAmountsPaid(Amounts, String)}.
     *
     * For adding extras, upsells or any form of new purchase item to a transaction, please create a new basket and add via {@link #addNewBasket(Basket)}.
     *
     * This method should only be called once. Any previous values set will be overwritten if the method is called again.
     *
     * Any relevant payment references can be set via {@link #setPaymentReferences(AdditionalData)}.
     *
     * @param basketId      The id of the basket from transactionRequest.getBaskets() to add the items to
     * @param basketItems   The basket items to add
     * @param paymentMethod The payment method used for the discounts
     * @throws IllegalArgumentException If any basket item has a positive amount, or there are no baskets in the request
     */
    public void applyDiscountsToBasket(String basketId, List<BasketItem> basketItems, String paymentMethod) {
        checkNotEmpty(basketId, "Basket id must be set");
        checkNotEmpty(basketItems, "Basket items must be set");
        checkNotEmpty(paymentMethod, "Payment method must be set");
        boolean foundBasket = false;
        for (Basket basket : transactionRequest.getBaskets()) {
            if (basket.getId().equals(basketId)) {
                foundBasket = true;
                break;
            }
        }
        if (!foundBasket) {
            throw new IllegalArgumentException("No basket with provided id found in TransactionRequest");
        }

        for (BasketItem basketItem : basketItems) {
            if (basketItem.getIndividualAmount() >= 0) {
                throw new IllegalArgumentException("Discount items must all have negative amount values");
            }
        }
        if (basketItems.size() > 0) {
            flowResponse.updateBasket(basketId, basketItems);
            setAmountsPaid(new Amounts(Math.abs(flowResponse.getModifiedBasket().getTotalBasketValue()),
                                       transactionRequest.getAmounts().getCurrency()), paymentMethod);
        }
    }

    /**
     * Pay off a portion or the full requested amounts.
     *
     * Note that if you are applying discounts to specific basket items, please use {@link #applyDiscountsToBasket(String, List, String)} instead.
     *
     * The use cases for this involves the customer paying part or all of the base amount owed via means other than payment cards.
     * Examples are loyalty points, cash, etc.
     *
     * Note that paying off the "additional amounts" is not supported at this time - only the base amount. The paid amounts must only set
     * the base amount value, and it must not exceed the request base amount value. If it exceeds it, or there are additional amounts set,
     * an exception will be thrown.
     *
     * If there is remaining base amounts to pay after this, the remaining amount will be processed by the payment app.
     *
     * If the request contains no additional amounts and this payment equals the requested amounts, the transaction will be considered fulfilled and completed.
     *
     * NOTE! This response only tracks one paid amounts - if this method is called more than once, any previous values will be overwritten.
     * It is up to the client to ensure that a consolidated Amounts object is constructed and provided here.
     *
     * Any relevant payment references can be set via {@link #setPaymentReferences(AdditionalData)}.
     *
     * @param amountsPaid   The amounts paid
     * @param paymentMethod The method of payment
     * @throws IllegalArgumentException If either argument is null or the values violate the restrictions mentioned above
     */
    public void setAmountsPaid(Amounts amountsPaid, String paymentMethod) {
        checkNotNull(amountsPaid, "Amounts paid must be set");
        checkNotEmpty(paymentMethod, "Payment method must be set");
        if (amountsPaid.getBaseAmountValue() > transactionRequest.getAmounts().getBaseAmountValue()) {
            throw new IllegalArgumentException("Paid base amount value can not exceed the request base amount value");
        }
        if (!amountsPaid.getAdditionalAmounts().isEmpty()) {
            throw new IllegalArgumentException("Paid additional amounts is not supported at the moment - set base amount only");
        }
        if (amountsPaid.getTotalAmountValue() > transactionRequest.getAmounts().getTotalAmountValue()) {
            throw new IllegalArgumentException("Paid amounts can not exceed requested amounts");
        }
        if (!amountsPaid.getCurrency().equals(transactionRequest.getAmounts().getCurrency())) {
            throw new IllegalArgumentException("Paid currency does not match request currency");
        }
        flowResponse.setAmountsPaid(amountsPaid, paymentMethod);
    }

    /**
     * Set payment references for use with {@link #applyDiscountsToBasket(String, List, String)} or {@link #setAmountsPaid(Amounts, String)}.
     *
     * @param paymentReferences Payment references
     */
    public void setPaymentReferences(AdditionalData paymentReferences) {
        flowResponse.setPaymentReferences(paymentReferences);
    }

    /**
     * Add a customer or update existing customer details.
     *
     * If you are updating an existing customer, use the {@link Customer} from {@link TransactionRequest#getCustomer()}, update as required and
     * pass back via this method. Note that only {@link Customer#addToken(Token)} and {@link Customer#addCustomerDetails(String, Object[])} are taken
     * into account for updating customer details. It is assumed that the id and name of a customer is managed by the source of the customer data.
     *
     * @param customer The customer details
     * @throws IllegalArgumentException If customer is null
     */
    public void addOrUpdateCustomerDetails(Customer customer) {
        checkNotNull(customer, "Customer must be set");
        flowResponse.addOrUpdateCustomer(customer);
    }

    /**
     * Get the flow response that is created from this model.
     *
     * For internal use.
     *
     * @return The flow response
     */
    FlowResponse getFlowResponse() {
        if (amountsModifier.hasModifications()) {
            flowResponse.updateRequestAmounts(amountsModifier.build());
        }
        return flowResponse;
    }

    /**
     * Send off the response.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void sendResponse() {
        FlowResponse flowResponse = getFlowResponse();
        flowResponse.validate();
        doSendResponse(flowResponse.toJson());
    }

    /**
     * Call to inform FPS that processing is done and no augmentation is required.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void skip() {
        sendEmptyResponse();
    }

    @Override
    public String getRequestJson() {
        return transactionRequest.toJson();
    }
}
