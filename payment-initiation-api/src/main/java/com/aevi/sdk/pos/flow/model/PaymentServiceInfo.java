package com.aevi.sdk.pos.flow.model;

import android.support.annotation.NonNull;

import com.aevi.sdk.flow.model.BaseServiceInfo;
import com.aevi.util.json.JsonConverter;

import java.util.Arrays;
import java.util.Objects;

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
    private final boolean willPrintReceipts;
    private final boolean supportsFlowCardReading;

    PaymentServiceInfo(String paymentServiceId, String packageName, String vendor, String version, String apiVersion, String displayName, String[] paymentMethods, String[] supportedCurrencies, String defaultCurrency,
                       String terminalId, String[] merchantIds, String[] supportedRequestTypes, String[] supportedTransactionTypes, boolean supportManualEntry,
                       boolean hasAccessibilityMode, boolean willPrintReceipts, boolean supportsFlowCardReading, String[] supportedDataKeys) {
        super(paymentServiceId, vendor, version, apiVersion, displayName, hasAccessibilityMode, paymentMethods, supportedCurrencies, supportedRequestTypes, supportedTransactionTypes, supportedDataKeys);
        this.packageName = packageName;
        this.defaultCurrency = defaultCurrency;
        this.terminalId = terminalId;
        this.merchantIds = merchantIds != null ? merchantIds : new String[0];
        this.supportManualEntry = supportManualEntry;
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
     *
     * @return Package name
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
        return supportManualEntry == that.supportManualEntry &&
                willPrintReceipts == that.willPrintReceipts &&
                supportsFlowCardReading == that.supportsFlowCardReading &&
                Objects.equals(packageName, that.packageName) &&
                Objects.equals(defaultCurrency, that.defaultCurrency) &&
                Objects.equals(terminalId, that.terminalId) &&
                Arrays.equals(merchantIds, that.merchantIds);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(super.hashCode(), packageName, defaultCurrency, terminalId, supportManualEntry, willPrintReceipts, supportsFlowCardReading);
        result = 31 * result + Arrays.hashCode(merchantIds);
        return result;
    }
}
