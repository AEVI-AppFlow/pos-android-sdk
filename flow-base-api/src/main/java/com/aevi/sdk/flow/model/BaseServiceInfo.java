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

package com.aevi.sdk.flow.model;


import android.support.annotation.NonNull;

import com.aevi.sdk.flow.util.ComparisonUtil;

import java.util.Arrays;

import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

/**
 * Common flags for service info models.
 */
public abstract class BaseServiceInfo extends BaseModel {

    private final String vendor;
    private final String serviceVersion;
    private final String apiVersion;
    private final String displayName;
    private final boolean hasAccessibilityMode;
    private final String[] paymentMethods;
    private final String[] supportedCurrencies;
    private final String[] supportedRequestTypes;
    private final String[] supportedTransactionTypes;
    private final String[] supportedDataKeys;

    // Default constructor for deserialisation
    protected BaseServiceInfo() {
        this("", "", "", "", "", false, null,
                null, null, null, null);
    }

    protected BaseServiceInfo(String id, String vendor, String serviceVersion, String apiVersion, String displayName, boolean hasAccessibilityMode,
                              String[] paymentMethods, String[] supportedCurrencies, String[] supportedRequestTypes, String[] supportedTransactionTypes,
                              String[] supportedDataKeys) {
        super(id);
        this.vendor = vendor;
        this.serviceVersion = serviceVersion;
        this.apiVersion = apiVersion;
        this.displayName = displayName;
        this.hasAccessibilityMode = hasAccessibilityMode;
        this.paymentMethods = paymentMethods != null ? paymentMethods : new String[0];
        this.supportedCurrencies = supportedCurrencies != null ? supportedCurrencies : new String[0];
        this.supportedRequestTypes = supportedRequestTypes != null ? supportedRequestTypes : new String[0];
        this.supportedTransactionTypes = supportedTransactionTypes != null ? supportedTransactionTypes : new String[0];
        this.supportedDataKeys = supportedDataKeys != null ? supportedDataKeys : new String[0];
        checkArguments();
    }

    private void checkArguments() {
        checkArgument(vendor != null, "Vendor must be set");
        checkArgument(serviceVersion != null, "Service version must be set");
        checkArgument(apiVersion != null, "API version must be set");
        checkArgument(displayName != null, "Display name must be set");
    }

    /**
     * Gets the service vendor name.
     *
     * @return The service vendor name
     */
    @NonNull
    public String getVendor() {
        return vendor;
    }

    /**
     * Gets the service application version.
     *
     * @return The service application version string
     */
    @NonNull
    public String getServiceVersion() {
        return serviceVersion;
    }

    /**
     * Gets the version of the API the service is built against.
     *
     * @return The version of the API the service built against
     */
    @NonNull
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * Get the name of this service for displaying to users.
     *
     * @return The display name of this service
     */
    @NonNull
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns whether or not this service supports an accessible mode.
     *
     * @return true if the service has an accessible mode.
     */
    public boolean supportsAccessibilityMode() {
        return hasAccessibilityMode;
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
     * Gets an array of the supported request types, indicating what type of requests it can handle.
     *
     * See reference values in the documentation for possible values.
     *
     * @return array of request types supported by the service
     */
    @NonNull
    public String[] getSupportedRequestTypes() {
        return supportedRequestTypes;
    }

    /**
     * Check whether this service supports the given request type.
     *
     * @param requestType The request type to check if supported
     * @return True if supported, false otherwise
     */
    public boolean supportsRequestType(String requestType) {
        return supportedRequestTypes.length > 0 && ComparisonUtil.stringArrayContainsIgnoreCase(supportedRequestTypes, requestType);
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
     * Returns an array of supported request {@link AdditionalData} keys.
     *
     * A request can set various optional and custom flags in the {@link AdditionalData} object.
     * This array will return an array of the keys this service supports.
     *
     * May be empty.
     *
     * See reference values in the documentation for possible values.
     *
     * @return An array of supported AdditionalData keys
     */
    @NonNull
    public String[] getSupportedDataKeys() {
        return supportedDataKeys;
    }

    /**
     * Check whether this service supports the given data key (for {@link AdditionalData usage}.
     *
     * @param dataKey The data key to check if supported
     * @return True if supported, false otherwise
     */
    public boolean supportsDataKey(String dataKey) {
        return supportedDataKeys.length > 0 && ComparisonUtil.stringArrayContainsIgnoreCase(supportedDataKeys, dataKey);
    }

    @Override
    public String toString() {
        return "BaseServiceInfo{" +
                "vendor='" + vendor + '\'' +
                ", version='" + serviceVersion + '\'' +
                ", displayName='" + displayName + '\'' +
                ", hasAccessibilityMode=" + hasAccessibilityMode +
                ", paymentMethods=" + Arrays.toString(paymentMethods) +
                ", supportedCurrencies=" + Arrays.toString(supportedCurrencies) +
                ", supportedRequestTypes=" + Arrays.toString(supportedRequestTypes) +
                ", supportedTransactionTypes=" + Arrays.toString(supportedTransactionTypes) +
                ", supportedDataKeys=" + Arrays.toString(supportedDataKeys) +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BaseServiceInfo that = (BaseServiceInfo) o;

        if (hasAccessibilityMode != that.hasAccessibilityMode) return false;
        if (vendor != null ? !vendor.equals(that.vendor) : that.vendor != null) return false;
        if (serviceVersion != null ? !serviceVersion.equals(that.serviceVersion) : that.serviceVersion != null) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(paymentMethods, that.paymentMethods)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(supportedCurrencies, that.supportedCurrencies)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(supportedRequestTypes, that.supportedRequestTypes)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(supportedTransactionTypes, that.supportedTransactionTypes)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(supportedDataKeys, that.supportedDataKeys);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (vendor != null ? vendor.hashCode() : 0);
        result = 31 * result + (serviceVersion != null ? serviceVersion.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (hasAccessibilityMode ? 1 : 0);
        result = 31 * result + Arrays.hashCode(paymentMethods);
        result = 31 * result + Arrays.hashCode(supportedCurrencies);
        result = 31 * result + Arrays.hashCode(supportedRequestTypes);
        result = 31 * result + Arrays.hashCode(supportedTransactionTypes);
        result = 31 * result + Arrays.hashCode(supportedDataKeys);
        return result;
    }


}
