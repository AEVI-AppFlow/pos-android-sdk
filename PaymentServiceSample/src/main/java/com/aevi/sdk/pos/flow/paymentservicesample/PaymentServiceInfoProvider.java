package com.aevi.sdk.pos.flow.paymentservicesample;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aevi.sdk.flow.constants.PaymentMethods;
import com.aevi.sdk.pos.flow.model.PaymentServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentServiceInfoBuilder;
import com.aevi.sdk.pos.flow.service.BasePaymentServiceInfoProvider;

import java.util.Set;

public class PaymentServiceInfoProvider extends BasePaymentServiceInfoProvider {

    private static final String[] SUPPORTED_CURRENCIES = new String[]{"EUR", "GBP", "USD"};

    @Override
    protected PaymentServiceInfo getPaymentServiceInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean supportsCardReadingStage = sharedPreferences.getBoolean(getContext().getString(R.string.pref_supports_read_card_key), true);
        String[] supportedRequestTypes = getListValue(sharedPreferences, getContext().getString(R.string.pref_request_types_key),
                getContext().getResources().getStringArray(R.array.request_types));
        String[] supportedTransactionTypes = getListValue(sharedPreferences, getContext().getString(R.string.pref_transaction_types_key),
                getContext().getResources().getStringArray(R.array.transaction_types));


        return new PaymentServiceInfoBuilder()
                .withVendor("AEVI")
                .withDisplayName("Payment Service Sample")
                .withPaymentMethods(PaymentMethods.CARD)
                .withSupportedCurrencies(SUPPORTED_CURRENCIES)
                .withDefaultCurrency(SUPPORTED_CURRENCIES[0])
                .withTerminalId("12345678")
                .withMerchantIds("1234abcd")
                .withSupportedRequestTypes(supportedRequestTypes)
                .withSupportedTransactionTypes(supportedTransactionTypes)
                .withSupportManualEntry(false)
                .withSupportsAccessibilityMode(false)
                .withSupportsFlowCardReading(supportsCardReadingStage)
                .withWillPrintReceipts(false)
                .build(getContext());
    }

    private String[] getListValue(SharedPreferences sharedPreferences, String prefKey, String[] defaults) {
        Set<String> selectedValues = sharedPreferences.getStringSet(prefKey, null);
        if (selectedValues != null) {
            return selectedValues.toArray(new String[selectedValues.size()]);
        }
        return defaults;
    }
}
