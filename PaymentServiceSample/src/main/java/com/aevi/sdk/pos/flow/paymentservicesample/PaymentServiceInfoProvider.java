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


import com.aevi.sdk.flow.constants.FinancialRequestTypes;
import com.aevi.sdk.pos.flow.model.Merchant;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfoBuilder;
import com.aevi.sdk.pos.flow.paymentservicesample.util.IdProvider;
import com.aevi.sdk.pos.flow.provider.BasePaymentFlowServiceInfoProvider;

public class PaymentServiceInfoProvider extends BasePaymentFlowServiceInfoProvider {

    @Override
    protected PaymentFlowServiceInfo getPaymentFlowServiceInfo() {

        String[] supportedTransactionTypes = getContext().getResources().getStringArray(R.array.transaction_types);
        String[] supportedCurrencies = getContext().getResources().getStringArray(R.array.supported_currencies);
        String[] supportedPaymentMethods = getContext().getResources().getStringArray(R.array.payment_methods);

        return new PaymentFlowServiceInfoBuilder()
                .withVendor("AEVI")
                .withDisplayName("Payment Service Sample")
                .withCanPayAmounts(true, supportedPaymentMethods)
                .withCustomRequestTypes(FinancialRequestTypes.RESPONSE_REDELIVERY)
                .withSupportedTransactionTypes(supportedTransactionTypes)
                .withSupportedCurrencies(supportedCurrencies)
                .withDefaultCurrency(supportedCurrencies[0])
                .withLogicalDeviceId(IdProvider.getTerminalId())
                .withMerchants(new Merchant(IdProvider.getMerchantId(), IdProvider.getMerchantName()))
                .withManualEntrySupport(false)
                .withSupportsAccessibilityMode(false)
                .build(getContext());
    }
}
