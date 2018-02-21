package com.aevi.sdk.flow.model;

import android.content.Context;

import com.aevi.sdk.flow.constants.TransactionTypes;

import static com.aevi.sdk.flow.util.Preconditions.*;

public class FlowServiceInfoBuilder {

    private String vendor;
    private String version;
    private String displayName;
    private String[] stages;
    private String[] capabilities;
    private boolean supportsAccessibility;
    private String[] paymentMethods;
    private String[] supportedCurrencies;
    private String[] supportedTransactionTypes = new String[]{TransactionTypes.TYPE_PAY};
    private boolean requiresCardToken;
    private String[] supportedDataKeys;
    private boolean backgroundOnly;
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
     * Set the version of this flow service.
     *
     * Mandatory field.
     *
     * @param version The version number or name as a string
     * @return This builder
     */
    public FlowServiceInfoBuilder withVersion(String version) {
        this.version = version;
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
     * Set what flow stages this service supports.
     *
     * Mandatory field.
     *
     * For POS flow, the possible stages are defined in the PaymentStage model and PaymentStage.XXX.name() should be used here.
     *
     * This would typically map to what intent actions your services are defined with in the manifest.
     *
     * @param stages The supported flow stages
     * @return This builder
     */
    public FlowServiceInfoBuilder withStages(String... stages) {
        this.stages = stages;
        return this;
    }

    /**
     * Set what capabilities this service provides.
     *
     * Mandatory field.
     *
     * This is to outline the functions of the services. Examples may be "loyalty", "currencyConversion", "split", etc.
     *
     * See reference values in the documentation for possible values.
     *
     * @param capabilities The set of capabilities
     * @return This builder
     */
    public FlowServiceInfoBuilder withCapabilities(String... capabilities) {
        this.capabilities = capabilities;
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
     * Sets the transaction types supported by this flow service. These types could be unique to the service.
     *
     * If none are set by the service, it will default to supporting payment/sale types.
     *
     * ONLY set this if service can handle non-sale flows.
     *
     * See reference values in the documentation for possible values.
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
     * @param supportedDataKeys The array of supported data keys
     * @return This builder
     */
    public FlowServiceInfoBuilder withSupportedDataKeys(String... supportedDataKeys) {
        this.supportedDataKeys = supportedDataKeys;
        return this;
    }

    /**
     * Set whether this service operates in the background only - i.e does not launch any activities and handles the request in the service alone.
     *
     * This is typically useful for "post-payment/flow" applications that perform reporting/analytics/etc.
     *
     * If this flag is set, this service will be called but the system will not wait for a response.
     *
     * Defaults to false.
     *
     * @param backgroundOnly True if this service operates in the background, false otherwise.
     * @return This builder
     */
    public FlowServiceInfoBuilder withBackgroundOnly(boolean backgroundOnly) {
        this.backgroundOnly = backgroundOnly;
        return this;
    }

    /**
     * Set whether this service can adjust the requested amounts for the current request, and for what currencies this is supported for.
     *
     * This is typically used to add a charge/fee, or for charity, or to split a request.
     *
     * Note that such operations will be rejected from this service if the flag is not set.
     *
     * Defaults to false. If set and there is no currency restriction, the currency var-args can be left out.
     *
     * See reference values in the documentation for possible values of payment methods.
     *
     * @param canAdjustAmounts    True if the service can adjust amounts, false otherwise
     * @param supportedCurrencies The set of supported currencies as 3 letter ISO-4217 currency codes. If not set, defaults to all possible currencies
     * @return This builder
     */
    public FlowServiceInfoBuilder withCanAdjustAmounts(boolean canAdjustAmounts, String... supportedCurrencies) {
        this.canAdjustAmounts = canAdjustAmounts;
        this.supportedCurrencies = supportedCurrencies;
        return this;
    }

    /**
     * Set whether this service can pay/charge the customer via non-payment card means, such as rewards or loyalty points.
     *
     * If set to true, the payment methods used must be set. If no supported currencies are set, it is assumed all currencies are supported.
     *
     * Note that attempts of paying amounts will be rejected from this service if these flags are not set correctly.
     *
     * Defaults to false.
     *
     * See reference values in the documentation for possible values of payment methods.
     *
     * @param canPayAmounts       True if the service can pay amounts, false otherwise
     * @param paymentMethods      A set of payment methods used by the service (must not be empty)
     * @param supportedCurrencies The set of supported currencies as 3 letter ISO-4217 currency codes (can be empty to support all currencies)
     * @return This builder
     */
    public FlowServiceInfoBuilder withCanPayAmounts(boolean canPayAmounts, String[] paymentMethods, String... supportedCurrencies) {
        if (canPayAmounts && (paymentMethods == null || paymentMethods.length == 0)) {
            throw new IllegalArgumentException("If canPayAmounts flag is set, payment methods must be provided");
        }
        this.canPayAmounts = canPayAmounts;
        this.paymentMethods = paymentMethods;
        this.supportedCurrencies = supportedCurrencies;
        return this;
    }

    public FlowServiceInfo build(Context context) {
        checkNotNull(vendor, "Vendor must be set");
        checkNotNull(version, "Version must be set");
        checkNotNull(displayName, "Display name must be set");
        checkNotEmpty(capabilities, "Capabilities must be set");
        checkNotEmpty(stages, "Stages must be set");
        return new FlowServiceInfo(context.getPackageName(), vendor, version, displayName, supportsAccessibility, stages, capabilities, paymentMethods,
                supportedCurrencies, supportedTransactionTypes, requiresCardToken, supportedDataKeys, backgroundOnly, canAdjustAmounts, canPayAmounts);
    }
}