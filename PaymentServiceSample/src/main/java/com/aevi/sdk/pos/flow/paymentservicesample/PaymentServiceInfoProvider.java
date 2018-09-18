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

package com.aevi.sdk.pos.flow.paymentservicesample;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aevi.sdk.flow.constants.PaymentMethods;
import com.aevi.sdk.flow.constants.ServiceInfoDataKeys;
import com.aevi.sdk.pos.flow.model.Merchant;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfoBuilder;
import com.aevi.sdk.pos.flow.paymentservicesample.util.IdProvider;
import com.aevi.sdk.pos.flow.provider.BasePaymentFlowServiceInfoProvider;

import java.util.Set;

public class PaymentServiceInfoProvider extends BasePaymentFlowServiceInfoProvider {

    private static final String[] SUPPORTED_CURRENCIES = new String[]{"EUR", "GBP", "USD"};

    @Override
    protected PaymentFlowServiceInfo getPaymentFlowServiceInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String[] supportedRequestTypes = getListValue(sharedPreferences, getContext().getString(R.string.pref_request_types_key),
                getContext().getResources().getStringArray(R.array.request_types));
        String[] supportedTransactionTypes = getListValue(sharedPreferences, getContext().getString(R.string.pref_transaction_types_key),
                getContext().getResources().getStringArray(R.array.transaction_types));

        return new PaymentFlowServiceInfoBuilder()
                .withVendor("AEVI")
                .withDisplayName("Payment Service Sample")
                .withCanPayAmounts(true, PaymentMethods.CARD)
                .withSupportedCurrencies(SUPPORTED_CURRENCIES)
                .withDefaultCurrency(SUPPORTED_CURRENCIES[0])
                .withLogicalDeviceId(IdProvider.getTerminalId())
                .withMerchants(new Merchant(IdProvider.getMerchantId(), IdProvider.getMerchantName()))
                .withSupportedRequestTypes(supportedRequestTypes)
                .withSupportedTransactionTypes(supportedTransactionTypes)
                .withManualEntrySupport(false)
                .withSupportsAccessibilityMode(false)
                .withAdditionalInfo(ServiceInfoDataKeys.PRINTS_RECEIPTS, false)
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
