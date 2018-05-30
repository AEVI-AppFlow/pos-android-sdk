package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;

import android.content.Context;

import com.aevi.sdk.pos.flow.model.PaymentServiceInfo;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;

public class PaymentServiceInfoAdapter extends BaseServiceInfoAdapter<PaymentServiceInfo> {

    public PaymentServiceInfoAdapter(Context context, PaymentServiceInfo paymentServiceInfo) {
        super(context, R.array.payment_service_labels, paymentServiceInfo);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.label.setText(labels[position]);
        String value = "";
        switch (resIds[position]) {
            case R.string.service_label_id:
                value = info.getId();
                break;
            case R.string.service_label_app_version:
                value = info.getServiceVersion();
                break;
            case R.string.service_label_api_version:
                value = info.getApiVersion();
                break;
            case R.string.service_label_vendor:
                value = info.getVendor();
                break;
            case R.string.service_label_accessible_mode:
                value = getYesNo(info.supportsAccessibilityMode());
                break;
            case R.string.ps_label_card_reading:
                value = getYesNo(info.supportsFlowCardReading());
                break;
            case R.string.service_label_currencies:
                value = getArrayValue(info.getSupportedCurrencies());
                break;
            case R.string.ps_label_default_currency:
                value = info.getDefaultCurrency();
                break;
            case R.string.ps_label_manual_entry:
                value = getYesNo(info.supportManualEntry());
                break;
            case R.string.ps_label_merchant_ids:
                value = getArrayValue(info.getMerchantIds());
                break;
            case R.string.service_label_payment_methods:
                value = getArrayValue(info.getPaymentMethods());
                break;
            case R.string.ps_label_print_receipts:
                value = getYesNo(info.willPrintReceipts());
                break;
            case R.string.service_label_supported_data_keys:
                value = getArrayValue(info.getSupportedDataKeys());
                break;
            case R.string.ps_label_terminal_id:
                value = info.getTerminalId();
                break;
            case R.string.service_label_transaction_types:
                value = getArrayValue(info.getSupportedTransactionTypes());
                break;
            case R.string.service_label_request_types:
                value = getArrayValue(info.getSupportedRequestTypes());
                break;
        }
        holder.value.setText(value);
    }
}
