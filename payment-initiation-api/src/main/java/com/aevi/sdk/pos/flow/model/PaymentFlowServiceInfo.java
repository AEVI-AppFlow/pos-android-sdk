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

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.BaseServiceInfo;
import com.aevi.sdk.flow.util.ComparisonUtil;
import com.aevi.util.json.JsonConverter;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the capabilities of a flow service.
 *
 * See {@link BaseServiceInfo} for inherited information.
 *
 * Use flow-service-api FlowServiceInfoBuilder to construct an instance.
 */
public class PaymentFlowServiceInfo extends BaseServiceInfo {

    private final boolean canAdjustAmounts;
    private final boolean canPayAmounts;
    private final String defaultCurrency;
    private final String[] supportedTransactionTypes;
    private final String[] supportedCurrencies;
    private final String[] paymentMethods;

    // Default constructor for deserialisation
    PaymentFlowServiceInfo() {
        canAdjustAmounts = false;
        canPayAmounts = false;
        defaultCurrency = "";
        supportedTransactionTypes = new String[0];
        supportedCurrencies = new String[0];
        paymentMethods = new String[0];
    }

    public PaymentFlowServiceInfo(String id, String packageName, String vendor, String serviceVersion, String apiVersion, String displayName,
                                  boolean hasAccessibilityMode, String[] supportedRequestTypes, String[] supportedDataKeys, String logicalDeviceId,
                                  boolean canAdjustAmounts, boolean canPayAmounts, String defaultCurrency, String[] supportedTransactionTypes,
                                  String[] supportedCurrencies, String[] paymentMethods, AdditionalData additionalInfo) {
        super(id, packageName, vendor, logicalDeviceId, serviceVersion, apiVersion, displayName, hasAccessibilityMode, supportedRequestTypes,
                supportedDataKeys, additionalInfo);
        this.canAdjustAmounts = canAdjustAmounts;
        this.canPayAmounts = canPayAmounts;
        this.defaultCurrency = defaultCurrency;
        this.paymentMethods = paymentMethods != null ? paymentMethods : new String[0];
        this.supportedCurrencies = supportedCurrencies != null ? supportedCurrencies : new String[0];
        this.supportedTransactionTypes = supportedTransactionTypes != null ? supportedTransactionTypes : new String[0];
    }

    /**
     * Gets an array of payment methods supported by the service.
     *
     * See reference values in the documentation for possible values.
     *
     * May be empty.
     *
     * @return An array of supported payment methods
     */
    @NonNull
    public String[] getPaymentMethods() {
        return paymentMethods;
    }

    /**
     * Check whether this service supports the given payment method.
     *
     * @param paymentMethod The payment method to check if supported
     * @return True if supported, false otherwise
     */
    public boolean supportsPaymentMethod(String paymentMethod) {
        return paymentMethods.length > 0 && ComparisonUtil.stringArrayContainsIgnoreCase(paymentMethods, paymentMethod);
    }

    /**
     * Gets an array of currency codes supported by the service.
     *
     * May be empty.
     *
     * @return An array of String objects indicating the 3-letter ISO 4217 currencies supported by the service.
     */
    @NonNull
    public String[] getSupportedCurrencies() {
        return supportedCurrencies;
    }

    /**
     * Check whether this service supports the given currency.
     *
     * @param currency The currency to check if supported
     * @return True if supported, false otherwise
     */
    public boolean supportsCurrency(String currency) {
        return supportedCurrencies.length > 0 && ComparisonUtil.stringArrayContainsIgnoreCase(supportedCurrencies, currency);
    }

    /**
     * Check whether this service can adjust the request amounts.
     *
     * This is typically used to add a charge/fee, or for charity, or to split a request.
     *
     * @return True if the service can adjust request amounts, false otherwise
     */
    public boolean canAdjustAmounts() {
        return canAdjustAmounts;
    }

    /**
     * Check whether this service can pay amounts via non-payment card means, such as loyalty points.
     *
     * @return True if the service can pay amounts, false otherwise.
     */
    public boolean canPayAmounts() {
        return canPayAmounts;
    }

    /**
     * Gets an array of transaction types supported by the service.
     *
     * May be empty.
     *
     * See reference values in the documentation for possible values.
     *
     * @return array of transaction types supported by the service.
     */
    @NonNull
    public String[] getSupportedTransactionTypes() {
        return supportedTransactionTypes;
    }

    /**
     * Check whether this service supports the given transaction type.
     *
     * @param transactionType The transaction type to check if supported
     * @return True if supported, false otherwise
     */
    public boolean supportsTransactionType(String transactionType) {
        return supportedTransactionTypes.length > 0 && ComparisonUtil.stringArrayContainsIgnoreCase(supportedTransactionTypes, transactionType);
    }

    /**
     * Gets the default 3-letter ISO 4217 currency code of the service.
     *
     * @return The default currency of the service.
     */

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static PaymentFlowServiceInfo fromJson(String json) {
        return JsonConverter.deserialize(json, PaymentFlowServiceInfo.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PaymentFlowServiceInfo that = (PaymentFlowServiceInfo) o;
        return canAdjustAmounts == that.canAdjustAmounts &&
                canPayAmounts == that.canPayAmounts &&
                Objects.equals(defaultCurrency, that.defaultCurrency) &&
                Arrays.equals(supportedTransactionTypes, that.supportedTransactionTypes) &&
                Arrays.equals(supportedCurrencies, that.supportedCurrencies) &&
                Arrays.equals(paymentMethods, that.paymentMethods);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(super.hashCode(), canAdjustAmounts, canPayAmounts, defaultCurrency);
        result = 31 * result + Arrays.hashCode(supportedTransactionTypes);
        result = 31 * result + Arrays.hashCode(supportedCurrencies);
        result = 31 * result + Arrays.hashCode(paymentMethods);
        return result;
    }
}
