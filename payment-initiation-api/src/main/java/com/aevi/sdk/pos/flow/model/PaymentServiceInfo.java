package com.aevi.sdk.pos.flow.model;

import com.aevi.sdk.flow.model.BaseServiceInfo;
import com.aevi.util.json.JsonConverter;

import java.util.Arrays;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Represents the capabilities of a payment service.
 *
 * See {@link BaseServiceInfo} for inherited information.
 *
 * Use PaymentServiceInfoBuilder to construct an instance.
 */
public class PaymentServiceInfo extends BaseServiceInfo {

    private final String packageName;
    private final String defaultCurrency;
    private final String terminalId;
    private final String[] merchantIds;
    private final boolean supportManualEntry;
    private final String operatingMode;
    private final boolean canTokenize;
    private final boolean willPrintReceipts;
    private final boolean supportsFlowCardReading;

    PaymentServiceInfo(String paymentServiceId, String packageName, String vendor, String version, String displayName, String[] paymentMethods, String[] supportedCurrencies, String defaultCurrency,
                       String terminalId, String[] merchantIds, String[] supportedTransactionTypes, boolean supportManualEntry,
                       String operatingMode, boolean hasAccessibilityMode, boolean canTokenize, boolean willPrintReceipts, boolean supportsFlowCardReading, String[] supportedDataKeys) {
        super(paymentServiceId, vendor, version, displayName, hasAccessibilityMode, paymentMethods, supportedCurrencies, supportedTransactionTypes, supportedDataKeys);
        this.packageName = packageName;
        this.defaultCurrency = defaultCurrency;
        this.terminalId = terminalId;
        this.merchantIds = merchantIds != null ? merchantIds : new String[0];
        this.supportManualEntry = supportManualEntry;
        this.operatingMode = operatingMode;
        this.canTokenize = canTokenize;
        this.willPrintReceipts = willPrintReceipts;
        this.supportsFlowCardReading = supportsFlowCardReading;
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
    @NonNull
    public String[] getMerchantIds() {
        return merchantIds;
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
                ", defaultCurrency='" + defaultCurrency + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", merchantIds=" + Arrays.toString(merchantIds) +
                ", supportManualEntry=" + supportManualEntry +
                ", operatingMode='" + operatingMode + '\'' +
                ", canTokenize=" + canTokenize +
                ", willPrintReceipts=" + willPrintReceipts +
                ", supportsFlowCardReading=" + supportsFlowCardReading +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PaymentServiceInfo that = (PaymentServiceInfo) o;

        if (supportManualEntry != that.supportManualEntry) return false;
        if (canTokenize != that.canTokenize) return false;
        if (willPrintReceipts != that.willPrintReceipts) return false;
        if (supportsFlowCardReading != that.supportsFlowCardReading) return false;
        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
        if (defaultCurrency != null ? !defaultCurrency.equals(that.defaultCurrency) : that.defaultCurrency != null) return false;
        if (terminalId != null ? !terminalId.equals(that.terminalId) : that.terminalId != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(merchantIds, that.merchantIds)) return false;
        return operatingMode != null ? operatingMode.equals(that.operatingMode) : that.operatingMode == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (defaultCurrency != null ? defaultCurrency.hashCode() : 0);
        result = 31 * result + (terminalId != null ? terminalId.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(merchantIds);
        result = 31 * result + (supportManualEntry ? 1 : 0);
        result = 31 * result + (operatingMode != null ? operatingMode.hashCode() : 0);
        result = 31 * result + (canTokenize ? 1 : 0);
        result = 31 * result + (willPrintReceipts ? 1 : 0);
        result = 31 * result + (supportsFlowCardReading ? 1 : 0);
        return result;
    }
}
