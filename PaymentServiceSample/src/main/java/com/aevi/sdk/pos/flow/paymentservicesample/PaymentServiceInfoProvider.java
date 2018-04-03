package com.aevi.sdk.pos.flow.paymentservicesample;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aevi.sdk.flow.constants.PaymentMethods;
import com.aevi.sdk.flow.constants.TransactionTypes;
import com.aevi.sdk.pos.flow.paymentservicesample.R;
import com.aevi.sdk.pos.flow.model.PaymentServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentServiceInfoBuilder;
import com.aevi.sdk.pos.flow.service.BasePaymentServiceInfoProvider;

import static com.aevi.sdk.flow.constants.FinancialRequestTypes.*;

public class PaymentServiceInfoProvider extends BasePaymentServiceInfoProvider {

    @Override
    protected PaymentServiceInfo getPaymentServiceInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean supportsCardReadingStage = sharedPreferences.getBoolean(getContext().getString(R.string.pref_supports_read_card_key), true);
        return new PaymentServiceInfoBuilder()
                .withVendor("AEVI")
                .withDisplayName("Payment Service Sample")
                .withPaymentMethods(PaymentMethods.CARD)
                .withSupportedCurrencies("EUR", "GBP", "USD")
                .withDefaultCurrency("EUR")
                .withTerminalId("12345678")
                .withMerchantIds("1234abcd")
                .withSupportedRequestTypes(PAYMENT, REVERSAL, RESPONSE_REDELIVERY, TOKENISATION)
                .withSupportedTransactionTypes(TransactionTypes.SALE)
                .withSupportManualEntry(false)
                .withSupportsAccessibilityMode(false)
                .withSupportsFlowCardReading(supportsCardReadingStage)
                .withWillPrintReceipts(false)
                .build(getContext());
    }
}
