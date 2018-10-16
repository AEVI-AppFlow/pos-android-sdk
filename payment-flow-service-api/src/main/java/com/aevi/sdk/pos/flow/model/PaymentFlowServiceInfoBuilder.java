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
import android.support.annotation.NonNull;

import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.pos.flow.PaymentFlowServiceApi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.aevi.sdk.flow.util.Preconditions.checkNotNull;

/**
 * Builder to construct {@link PaymentFlowServiceInfo} instances.
 */
public class PaymentFlowServiceInfoBuilder {

    private String logicalDeviceId;
    private String vendor;
    private String displayName;
    private boolean supportsAccessibility;
    private String defaultCurrency;
    private Set<String> paymentMethods;
    private Set<String> supportedCurrencies;
    private Set<String> customRequestTypes;
    private Set<String> supportedDataKeys;
    private boolean canAdjustAmounts;
    private boolean canPayAmounts;
    private AdditionalData additionalInfo = new AdditionalData();

    /**
     * Set the logical device id that represents how this service identifies the device it is running on.
     *
     * In payment contexts, this is often also referred to as terminal id.
     *
     * @param logicalDeviceId The logical device id
     * @return This builder
     */
    @NonNull
    public PaymentFlowServiceInfoBuilder withLogicalDeviceId(String logicalDeviceId) {
        this.logicalDeviceId = logicalDeviceId;
        return this;
    }

    /**
     * Set the vendor name of this flow service.
     *
     * Mandatory field.
     *
     * @param vendor The vendor name
     * @return This builder
     */
    @NonNull
    public PaymentFlowServiceInfoBuilder withVendor(String vendor) {
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
    @NonNull
    public PaymentFlowServiceInfoBuilder withDisplayName(String displayName) {
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
    @NonNull
    public PaymentFlowServiceInfoBuilder withSupportsAccessibilityMode(boolean supportsAccessibilityMode) {
        this.supportsAccessibility = supportsAccessibilityMode;
        return this;
    }

    /**
     * Define custom request types that this service can handle.
     *
     * A service can via an implementation of {@link com.aevi.sdk.flow.service.BaseRequestService} handle any custom request.
     *
     * These custom requests are identified via their type, which is set in the {@link com.aevi.sdk.flow.model.Request} and routed to the
     * service that has defined it as a supported custom type here.
     *
     * @param customRequestTypes A list of string values representing custom request types
     * @return This builder
     */
    @NonNull
    public PaymentFlowServiceInfoBuilder withCustomRequestTypes(String... customRequestTypes) {
        if (customRequestTypes != null) {
            this.customRequestTypes = new HashSet<>(Arrays.asList(customRequestTypes));
        }
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
    @NonNull
    public PaymentFlowServiceInfoBuilder withSupportedDataKeys(String... supportedDataKeys) {
        if (supportedDataKeys != null) {
            this.supportedDataKeys = new HashSet<>(Arrays.asList(supportedDataKeys));
        }
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
    @NonNull
    public PaymentFlowServiceInfoBuilder withCanAdjustAmounts(boolean canAdjustAmounts) {
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
    @NonNull
    public PaymentFlowServiceInfoBuilder withCanPayAmounts(boolean canPayAmounts, String... paymentMethods) {
        if (canPayAmounts && (paymentMethods == null || paymentMethods.length == 0)) {
            throw new IllegalArgumentException("If canPayAmounts flag is set, payment methods must be provided");
        }
        this.canPayAmounts = canPayAmounts;
        this.paymentMethods = new HashSet<>(Arrays.asList(paymentMethods));
        return this;
    }

    /**
     * Sets the default currency for this service.
     *
     * If none is set, the first entry in the {@link #withSupportedCurrencies(String...)} data will be used.
     *
     * @param defaultCurrency The default currency as ISO-4217 currency code
     * @return This builder
     */
    @NonNull
    public PaymentFlowServiceInfoBuilder withDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
        return this;
    }

    /**
     * Set a list of supported currencies. If this is not set, the default is that all currencies are supported.
     *
     * Note that if you do restrict the currency support, your application will not get called for requests of other currencies.
     *
     * This is only relevant if the application supports either adjusting or paying amounts.
     *
     * @param supportedCurrencies The set of supported currencies as 3 letter ISO-4217 currency codes (can be empty to support all currencies)
     * @return This builder
     */
    @NonNull
    public PaymentFlowServiceInfoBuilder withSupportedCurrencies(String... supportedCurrencies) {
        if (supportedCurrencies != null) {
            this.supportedCurrencies = new HashSet<>(Arrays.asList(supportedCurrencies));
        }
        return this;
    }

    /**
     * Convenience wrapper for adding additional info.
     *
     * See {@link AdditionalData#addData(String, Object[])} for more info.
     *
     * @param key    The key to use for this data
     * @param values An array of values for this data
     * @param <T>    The type of object this data is an array of
     * @return This builder
     */
    @NonNull
    public <T> PaymentFlowServiceInfoBuilder withAdditionalInfo(String key, T... values) {
        additionalInfo.addData(key, values);
        return this;
    }

    /**
     * Set the additional info.
     *
     * @param additionalInfo The additional info
     * @return This builder
     */
    @NonNull
    public PaymentFlowServiceInfoBuilder withAdditionalInfo(AdditionalData additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    /**
     * Get the current additional info.
     *
     * @return The additional info
     */
    @NonNull
    public AdditionalData getCurrentAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * Helper for adding merchant info to additional info.
     *
     * @param merchants The merchants
     * @return This builder
     */
    @NonNull
    public PaymentFlowServiceInfoBuilder withMerchants(Merchant... merchants) {
        additionalInfo.addData("merchants", merchants);
        return this;
    }

    /**
     * Helper for adding manual entry support to additional info.
     *
     * @param manualEntrySupport True if manual entry is supported, false otherwise
     * @return This builder
     */
    @NonNull
    public PaymentFlowServiceInfoBuilder withManualEntrySupport(boolean manualEntrySupport) {
        additionalInfo.addData("supportsManualEntry", manualEntrySupport);
        return this;
    }

    /**
     * Build the {@link PaymentFlowServiceInfo}.
     *
     * @param context The Android context
     * @return A new FlowServiceInfo instance
     */
    @NonNull
    public PaymentFlowServiceInfo build(Context context) {
        String version = getAppVersion(context);
        return build(context.getPackageName(), version);
    }

    @NonNull
    PaymentFlowServiceInfo build(String packageName, String serviceVersion) {
        String apiVersion = PaymentFlowServiceApi.getApiVersion();
        checkNotNull(vendor, "Vendor must be set");
        checkNotNull(displayName, "Display name must be set");
        return new PaymentFlowServiceInfo(packageName, packageName, vendor, serviceVersion, apiVersion, displayName, supportsAccessibility,
                customRequestTypes, supportedDataKeys, logicalDeviceId, canAdjustAmounts, canPayAmounts, defaultCurrency,
                supportedCurrencies, paymentMethods, additionalInfo);
    }

    @NonNull
    private static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0.0";
        }
    }
}