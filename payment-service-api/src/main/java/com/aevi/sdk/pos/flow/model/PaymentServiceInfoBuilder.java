package com.aevi.sdk.pos.flow.model;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Base64;

import com.aevi.sdk.pos.flow.PaymentServiceApi;

import static com.aevi.sdk.flow.util.Preconditions.*;

/**
 * Builder to create a {@link PaymentServiceInfo} instance.
 */
public final class PaymentServiceInfoBuilder {

    private String vendor;
    private String displayName;
    private String[] paymentMethods;
    private String[] supportedCurrencies;
    private String defaultCurrency;
    private String terminalId;
    private String[] merchantIds;
    private String[] supportedRequestTypes;
    private String[] supportedTransactionTypes;
    private boolean supportManualEntry;
    private String operatingMode;
    private boolean hasAccessibilityMode;
    private boolean willPrintReceipts;
    private boolean supportsFlowCardReading;
    private String[] supportedDataKeys;

    /**
     * Set the vendor name of this payment service.
     *
     * Mandatory field.
     *
     * @param vendor The vendor name
     * @return This builder
     */
    public PaymentServiceInfoBuilder withVendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    /**
     * Set the name for this payment service to be used for displaying to users.
     *
     * @param displayName The display name of the service
     * @return This builder
     */
    public PaymentServiceInfoBuilder withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Set the supported payment methods for this payment service.
     *
     * See reference values in the documentation for possible values.
     *
     * @param paymentMethods The supported payment methods
     * @return This builder
     */
    public PaymentServiceInfoBuilder withPaymentMethods(String... paymentMethods) {
        this.paymentMethods = paymentMethods;
        return this;
    }

    /**
     * Sets the list of currencies supported by this payment service.
     *
     * Mandatory field.
     *
     * @param supportedCurrencies A list of 3 letter ISO-4217 currency code strings
     * @return This builder
     */
    public PaymentServiceInfoBuilder withSupportedCurrencies(String... supportedCurrencies) {
        this.supportedCurrencies = supportedCurrencies;
        return this;
    }

    /**
     * Sets the default currency for this payment service.
     *
     * Mandatory field.
     *
     * @param defaultCurrency The default currency as ISO-4217 currency code
     * @return This builder
     */
    public PaymentServiceInfoBuilder withDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
        return this;
    }

    /**
     * Set the terminal id of this payment service.
     *
     * Mandatory field.
     *
     * @param terminalId The terminal id associated with this payment service
     * @return This builder
     */
    public PaymentServiceInfoBuilder withTerminalId(String terminalId) {
        this.terminalId = terminalId;
        return this;
    }

    /**
     * Sets a list of merchant ids associated with this payment service.
     *
     * @param merchantIds A list of String merchant ids
     * @return This builder
     */
    public PaymentServiceInfoBuilder withMerchantIds(String... merchantIds) {
        this.merchantIds = merchantIds;
        return this;
    }

    /**
     * Sets the request types supported by this payment service. These types could be unique to the service.
     *
     * Mandatory field.
     *
     * See reference values in the documentation for possible values.
     *
     * @param supportedRequestTypes A list of string values indicating the request types this payment service can handle.
     * @return This builder
     */
    public PaymentServiceInfoBuilder withSupportedRequestTypes(String... supportedRequestTypes) {
        this.supportedRequestTypes = supportedRequestTypes;
        return this;
    }

    /**
     * Sets the transaction types supported by this payment service for requests of type "payment". These types could be unique to the payment service.
     *
     * Mandatory field.
     *
     * See reference values in the documentation for possible values.
     *
     * @param supportedTypes A list of string values indicating the transaction types this payment service can handle.
     * @return This builder
     */
    public PaymentServiceInfoBuilder withSupportedTransactionTypes(String... supportedTypes) {
        this.supportedTransactionTypes = supportedTypes;
        return this;
    }

    /**
     * Flag indicating whether or not manual entry (aka MOTO) is supported by this payment service.
     *
     * @param supportManualEntry True if manual entry (aka MOTO) is supported
     * @return This builder
     */
    public PaymentServiceInfoBuilder withSupportManualEntry(boolean supportManualEntry) {
        this.supportManualEntry = supportManualEntry;
        return this;
    }

    /**
     * Flag indicating the operating mode of this payment service.
     *
     * See reference values in the documentation for possible values.
     *
     * @param operatingMode The operating mode of this payment service.
     * @return This builder
     */
    public PaymentServiceInfoBuilder withOperatingMode(String operatingMode) {
        this.operatingMode = operatingMode;
        return this;
    }

    /**
     * Flag indicating whether or not this payment service supports accessibility mode.
     *
     * @param supportsAccessibilityMode True if this payment service supports accessibility mode
     * @return This builder
     */
    public PaymentServiceInfoBuilder withSupportsAccessibilityMode(boolean supportsAccessibilityMode) {
        this.hasAccessibilityMode = supportsAccessibilityMode;
        return this;
    }

    /**
     * Flag indicating if this payment service will print its own receipts or not.
     *
     * @param willPrintReceipts True if this app will print its own receipts
     * @return This builder
     */
    public PaymentServiceInfoBuilder withWillPrintReceipts(boolean willPrintReceipts) {
        this.willPrintReceipts = willPrintReceipts;
        return this;
    }

    /**
     * Flag indicating whether this payment service supports payment card reading as a separate step in the payment flow.
     *
     * If this is supported, it means the payment service can read the payment card, return card information back to the flow and then
     * process the payment at a later stage using these card details.
     *
     * @param supportsFlowCardReading True if separate flow payment card reading is supported
     * @return This builder
     */
    public PaymentServiceInfoBuilder withSupportsFlowCardReading(boolean supportsFlowCardReading) {
        this.supportsFlowCardReading = supportsFlowCardReading;
        return this;
    }

    /**
     * Set which {@link com.aevi.sdk.flow.model.AdditionalData} keys this payment service supports / takes into account.
     *
     * See reference values in the documentation for possible values.
     *
     * @param dataKeys The array of supported data keys
     * @return This builder
     */
    public PaymentServiceInfoBuilder withSupportedDataKeys(String... dataKeys) {
        this.supportedDataKeys = dataKeys;
        return this;
    }

    /**
     * Build and instance of the {@link PaymentServiceInfo} with the provided parameters
     *
     * @param context The android context for your application
     * @return The {@link PaymentServiceInfo} instance
     */
    public PaymentServiceInfo build(Context context) {
        return build(context.getPackageName(), getAppVersion(context));
    }

    PaymentServiceInfo build(String packageName, String appVersion) {
        checkNotNull(terminalId, "Terminal id must be set");
        checkNotNull(packageName, "Package name must be set");
        checkNotNull(vendor, "Vendor must be set");
        checkNotNull(appVersion, "Version must be set");
        checkNotNull(displayName, "Display name must be set");
        checkNotNull(defaultCurrency, "Default currency must be set");
        checkNotEmpty(supportedRequestTypes, "Supported request types must be set");
        checkNotEmpty(supportedCurrencies, "Supported currencies must be set");
        checkNotEmpty(supportedTransactionTypes, "Supported transaction types must be set");
        String apiVersion = PaymentServiceApi.getApiVersion();
        return new PaymentServiceInfo(createPaymentServiceId(packageName, terminalId),
                packageName, vendor, appVersion, apiVersion, displayName, paymentMethods, supportedCurrencies, defaultCurrency, terminalId, merchantIds,
                supportedRequestTypes, supportedTransactionTypes, supportManualEntry, operatingMode, hasAccessibilityMode, willPrintReceipts,
                supportsFlowCardReading, supportedDataKeys);
    }

    private static String createPaymentServiceId(String packageName, String terminalId) {
        String id = packageName + ":" + terminalId;
        return Base64.encodeToString(id.getBytes(), Base64.DEFAULT);
    }

    private static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0.0";
        }
    }
}
