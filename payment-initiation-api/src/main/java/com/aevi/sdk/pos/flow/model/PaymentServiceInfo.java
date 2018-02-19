package com.aevi.sdk.pos.flow.model;

import com.aevi.sdk.flow.model.BaseModel;
import com.aevi.util.json.JsonConverter;

import java.util.Arrays;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Represents a payment service capabilities as reported by the service itself.
 *
 * Note that most of the fields are optional for the service to set
 */
public class PaymentServiceInfo extends BaseModel {

    private final String packageName;
    private final String vendor;
    private final String version;
    private final String[] paymentMethods;
    private final String[] supportedCurrencies;
    private final String defaultCurrency;
    private final String terminalId;
    private final String[] merchantIds;
    private final String[] supportedTransactionTypes;
    private final boolean supportManualEntry;
    private final String operatingMode;
    private final boolean hasAccessibilityMode;
    private final boolean canTokenize;
    private final boolean willPrintReceipts;
    private final boolean supportsFlowCardReading;
    private final String[] supportedDataKeys;

    PaymentServiceInfo(String paymentServiceId, String packageName, String vendor, String version, String[] paymentMethods, String[] supportedCurrencies, String defaultCurrency,
                       String terminalId, String[] merchantIds, String[] supportedTransactionTypes, boolean supportManualEntry,
                       String operatingMode, boolean hasAccessibilityMode, boolean canTokenize, boolean willPrintReceipts, boolean supportsFlowCardReading, String[] supportedDataKeys) {
        super(paymentServiceId);
        this.packageName = packageName;
        this.vendor = vendor;
        this.version = version;
        this.paymentMethods = paymentMethods;
        this.supportedCurrencies = supportedCurrencies;
        this.defaultCurrency = defaultCurrency;
        this.terminalId = terminalId;
        this.merchantIds = merchantIds;
        this.supportedTransactionTypes = supportedTransactionTypes;
        this.supportManualEntry = supportManualEntry;
        this.operatingMode = operatingMode;
        this.hasAccessibilityMode = hasAccessibilityMode;
        this.canTokenize = canTokenize;
        this.willPrintReceipts = willPrintReceipts;
        this.supportsFlowCardReading = supportsFlowCardReading;
        this.supportedDataKeys = supportedDataKeys;
    }

    /**
     * Gets the payment service vendor/acquirer name.
     *
     * @return The payment service vendor/acquirer name
     */
    @NonNull
    public String getVendor() {
        return vendor;
    }

    /**
     * Gets the payment service version.
     *
     * @return The payment service version string.
     */
    @NonNull
    public String getVersion() {
        return version;
    }

    /**
     * Gets an array of payment methods supported by the payment service.
     *
     * See reference values in the documentation for possible values.
     *
     * @return An array of supported payment methods
     */
    @Nullable
    public String[] getPaymentMethods() {
        return paymentMethods;
    }

    /**
     * Gets an array of currency codes supported by the payment service.
     *
     * @return An array of String objects indicating the 3-letter ISO 4217 currencies supported by the payment service.
     */
    @NonNull
    public String[] getSupportedCurrencies() {
        return supportedCurrencies;
    }

    /**
     * Gets the default 3-letter ISO 4217 currency code of the payment service.
     *
     * @return The default currency of the payment service.
     */
    @NonNull
    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    /**
     * Gets the payment terminal Id.
     *
     * @return The terminal Id.
     */
    @NonNull
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * Gets a list of merchant ids registered with the device. Usually only one merchant is defined for each payment service.
     *
     * @return Array of Strings containing the list of merchant ids registered with the device.
     */
    @Nullable
    public String[] getMerchantIds() {
        return merchantIds;
    }

    /**
     * Gets an array of transaction types supported by the payment service.
     *
     * Guaranteed to be set (never null).
     *
     * See reference values in the documentation for possible values.
     *
     * @return array of transaction types supported by the payment service.
     */
    @NonNull
    public String[] getSupportedTransactionTypes() {
        return supportedTransactionTypes;
    }

    /**
     * Returns whether or not this payment service support manual entry of card details (aka MOTO).
     *
     * @return true if the payment service supports manual entry (aka MOTO).
     */
    public boolean supportManualEntry() {
        return supportManualEntry;
    }

    /**
     * Returns the operating mode for this payment service, if any.
     *
     * See reference values in the documentation for possible values and what impact they have.
     *
     * @return the operating mode of the payment service.
     */
    @Nullable
    public String getOperatingMode() {
        return operatingMode;
    }

    /**
     * Returns whether or not this payment service supports an accessible mode.
     *
     * @return true if the payment service has an accessible mode.
     */
    public boolean supportsAccessibilityMode() {
        return hasAccessibilityMode;
    }

    /**
     * Returns whether or not this payment service can tokenize cards for use with later requests.
     *
     * @return true if the payment service has the ability to tokenize cards.
     */
    public boolean supportsTokenization() {
        return canTokenize;
    }

    /**
     * Returns true if this payment service prints transaction receipts internally.
     *
     * If this flag is set, external receipt printing should not be performed.
     *
     * @return true if the payment service will perform receipt printing.
     */
    public boolean willPrintReceipts() {
        return willPrintReceipts;
    }

    /**
     * Returns true if this payment service supports payment card reading as a separate step in the payment flow.
     *
     * If this is supported, it means the payment service can read the payment card, return card information back to the flow and then
     * process the payment at a later stage using these card details.
     *
     * @return True if this payment service supports separate flow payment card reading
     */
    public boolean supportsFlowCardReading() {
        return supportsFlowCardReading;
    }

    /**
     * Returns an array of supported request {@link com.aevi.sdk.flow.model.AdditionalData} keys (such as "card_entry_methods").
     *
     * The payment request can set various optional and custom flags in the {@link com.aevi.sdk.flow.model.AdditionalData} object. This array will return an array of the
     * keys this payment service supports.
     *
     * See reference values in the documentation for possible values.
     *
     * @return An array of supported AdditionalData keys
     */
    @Nullable
    public String[] getSupportedDataKeys() {
        return supportedDataKeys;
    }

    @Override
    public String toJson() {
        return JsonConverter.serialize(this);
    }

    public static PaymentServiceInfo fromJson(String json) {
        return JsonConverter.deserialize(json, PaymentServiceInfo.class);
    }

    /**
     * For internal use.
     */
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String toString() {
        return "PaymentServiceInfo{" +
                "packageName='" + packageName + '\'' +
                ", vendor='" + vendor + '\'' +
                ", version='" + version + '\'' +
                ", paymentMethods=" + Arrays.toString(paymentMethods) +
                ", supportedCurrencies=" + Arrays.toString(supportedCurrencies) +
                ", defaultCurrency='" + defaultCurrency + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", merchantIds=" + Arrays.toString(merchantIds) +
                ", supportedTransactionTypes=" + Arrays.toString(supportedTransactionTypes) +
                ", supportManualEntry=" + supportManualEntry +
                ", operatingMode='" + operatingMode + '\'' +
                ", hasAccessibilityMode=" + hasAccessibilityMode +
                ", canTokenize=" + canTokenize +
                ", willPrintReceipts=" + willPrintReceipts +
                ", supportsFlowCardReading=" + supportsFlowCardReading +
                ", supportedDataKeys=" + Arrays.toString(supportedDataKeys) +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PaymentServiceInfo that = (PaymentServiceInfo) o;

        if (supportManualEntry != that.supportManualEntry) return false;
        if (hasAccessibilityMode != that.hasAccessibilityMode) return false;
        if (canTokenize != that.canTokenize) return false;
        if (willPrintReceipts != that.willPrintReceipts) return false;
        if (supportsFlowCardReading != that.supportsFlowCardReading) return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
        if (vendor != null ? !vendor.equals(that.vendor) : that.vendor != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(paymentMethods, that.paymentMethods)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(supportedCurrencies, that.supportedCurrencies)) return false;
        if (defaultCurrency != null ? !defaultCurrency.equals(that.defaultCurrency) : that.defaultCurrency != null) return false;
        if (terminalId != null ? !terminalId.equals(that.terminalId) : that.terminalId != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(merchantIds, that.merchantIds)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(supportedTransactionTypes, that.supportedTransactionTypes)) return false;
        if (operatingMode != null ? !operatingMode.equals(that.operatingMode) : that.operatingMode != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(supportedDataKeys, that.supportedDataKeys);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (vendor != null ? vendor.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(paymentMethods);
        result = 31 * result + Arrays.hashCode(supportedCurrencies);
        result = 31 * result + (defaultCurrency != null ? defaultCurrency.hashCode() : 0);
        result = 31 * result + (terminalId != null ? terminalId.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(merchantIds);
        result = 31 * result + Arrays.hashCode(supportedTransactionTypes);
        result = 31 * result + (supportManualEntry ? 1 : 0);
        result = 31 * result + (operatingMode != null ? operatingMode.hashCode() : 0);
        result = 31 * result + (hasAccessibilityMode ? 1 : 0);
        result = 31 * result + (canTokenize ? 1 : 0);
        result = 31 * result + (willPrintReceipts ? 1 : 0);
        result = 31 * result + (supportsFlowCardReading ? 1 : 0);
        result = 31 * result + Arrays.hashCode(supportedDataKeys);
        return result;
    }
}
