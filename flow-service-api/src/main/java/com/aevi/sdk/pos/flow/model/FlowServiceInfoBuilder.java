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

import android.content.Context;
import android.content.pm.PackageManager;

import com.aevi.sdk.flow.model.FlowServiceInfo;
import com.aevi.sdk.pos.flow.FlowServiceApi;

import static com.aevi.sdk.flow.util.Preconditions.*;

/**
 * Builder to construct {@link FlowServiceInfo} instances.
 */
public class FlowServiceInfoBuilder {

    private String vendor;
    private String displayName;
    private boolean supportsAccessibility;
    private String[] paymentMethods;
    private String[] supportedCurrencies = new String[0];
    private String[] supportedRequestTypes = new String[0];
    private String[] supportedTransactionTypes = new String[0];
    private boolean requiresCardToken;
    private String[] supportedDataKeys;
    private boolean canAdjustAmounts;
    private boolean canPayAmounts;

    /**
     * Set the vendor name of this flow service.
     *
     * Mandatory field.
     *
     * @param vendor The vendor name
     * @return This builder
     */
    public FlowServiceInfoBuilder withVendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    /**
     * Set the name for this flow service to be used for displaying to users.
     *
     * Mandatory field.
     *
     * @param displayName The display name of the service
     * @return This builder
     */
    public FlowServiceInfoBuilder withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Flag indicating whether or not this flow service supports accessibility mode to assist visually impaired users.
     *
     * Defaults to false.
     *
     * @param supportsAccessibilityMode True if this flow service supports accessibility mode
     * @return This builder
     */
    public FlowServiceInfoBuilder withSupportsAccessibilityMode(boolean supportsAccessibilityMode) {
        this.supportsAccessibility = supportsAccessibilityMode;
        return this;
    }

    /**
     * Sets the request types supported by this flow service. These types could be unique to the service.
     *
     * Mandatory field.
     *
     * These values determine for what types of requests your service will be called.
     *
     * See reference values in the documentation for possible values.
     *
     * @param supportedRequestTypes A list of string values indicating the request types this flow service can handle.
     * @return This builder
     */
    public FlowServiceInfoBuilder withSupportedRequestTypes(String... supportedRequestTypes) {
        this.supportedRequestTypes = supportedRequestTypes;
        return this;
    }

    /**
     * Sets the transaction types supported by this flow service for payment requests. These types could be unique to the service.
     *
     * These values determine for what type of transactions your service will be called.
     *
     * See reference values in the documentation for all possible values.
     *
     * @param supportedTransactionTypes A list of string values indicating the transaction types this flow service can handle.
     * @return This builder
     */
    public FlowServiceInfoBuilder withSupportedTransactionTypes(String... supportedTransactionTypes) {
        this.supportedTransactionTypes = supportedTransactionTypes;
        return this;
    }

    /**
     * Set whether this service requires a card token to be able to process the request.
     *
     * If this flag is set and the payment service does not provide a card token, this service will not get called.
     *
     * Defaults to false.
     *
     * @param requiresCardToken True if card token is required, false otherwise
     * @return This builder
     */
    public FlowServiceInfoBuilder withRequiresCardToken(boolean requiresCardToken) {
        this.requiresCardToken = requiresCardToken;
        return this;
    }


    /**
     * Set which {@link com.aevi.sdk.flow.model.AdditionalData} keys this flow service supports / takes into account.
     *
     * See reference values in the documentation for possible values.
     *
     * Defaults to empty list.
     *
     * @param supportedDataKeys The array of supported data keys
     * @return This builder
     */
    public FlowServiceInfoBuilder withSupportedDataKeys(String... supportedDataKeys) {
        this.supportedDataKeys = supportedDataKeys;
        return this;
    }

    /**
     * Set whether this service can adjust the requested amounts for the current request.
     *
     * This is typically used to add a charge/fee, or for charity, or to split a request.
     *
     * Note that such operations will be rejected from this service if the flag is not set.
     *
     * In order to specify what currencies this is supported for, please see {@link #withSupportedCurrencies(String...)}.
     *
     * Defaults to false.
     *
     * See reference values in the documentation for possible values of payment methods.
     *
     * @param canAdjustAmounts True if the service can adjust amounts, false otherwise
     * @return This builder
     */
    public FlowServiceInfoBuilder withCanAdjustAmounts(boolean canAdjustAmounts) {
        this.canAdjustAmounts = canAdjustAmounts;
        return this;
    }

    /**
     * Set whether this service can pay/charge the customer via non-payment card means, such as rewards or loyalty points.
     *
     * If set to true, the payment methods used must be set.
     *
     * Note that attempts of paying amounts will be rejected from this service if these flags are not set correctly.
     *
     * In order to specify what currencies this is supported for, please see {@link #withSupportedCurrencies(String...)}.
     *
     * Defaults to false.
     *
     * See reference values in the documentation for possible values of payment methods.
     *
     * @param canPayAmounts  True if the service can pay amounts, false otherwise
     * @param paymentMethods A set of payment methods used by the service (must not be empty)
     * @return This builder
     */
    public FlowServiceInfoBuilder withCanPayAmounts(boolean canPayAmounts, String... paymentMethods) {
        if (canPayAmounts && (paymentMethods == null || paymentMethods.length == 0)) {
            throw new IllegalArgumentException("If canPayAmounts flag is set, payment methods must be provided");
        }
        this.canPayAmounts = canPayAmounts;
        this.paymentMethods = paymentMethods;
        return this;
    }

    /**
     * Set a list of supported currencies. If this is not set, the default is that all currencies are supported.
     *
     * Note that if you do restrict the currency support, it is possible your application will not get called for requests of other currencies.
     *
     * This is only relevant if the application supports either adjusting or paying amounts.
     *
     * @param supportedCurrencies The set of supported currencies as 3 letter ISO-4217 currency codes (can be empty to support all currencies)
     * @return This builder
     */
    public FlowServiceInfoBuilder withSupportedCurrencies(String... supportedCurrencies) {
        this.supportedCurrencies = supportedCurrencies;
        return this;
    }

    /**
     * Build the {@link FlowServiceInfo}.
     *
     * @param context The Android context
     * @return A new FlowServiceInfo instance
     */
    public FlowServiceInfo build(Context context) {
        checkNotNull(vendor, "Vendor must be set");
        checkNotNull(displayName, "Display name must be set");
        checkNotEmpty(supportedRequestTypes, "At least one request type must be supported");
        String version = getAppVersion(context);
        String apiVersion = FlowServiceApi.getApiVersion();
        return new FlowServiceInfo(context.getPackageName(), vendor, version, apiVersion, displayName, supportsAccessibility, paymentMethods,
                supportedCurrencies, supportedRequestTypes, supportedTransactionTypes, requiresCardToken, supportedDataKeys, canAdjustAmounts, canPayAmounts);
    }

    private static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0.0";
        }
    }
}