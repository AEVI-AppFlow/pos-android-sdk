package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;


import android.content.Context;

import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.SystemInfo;

public class SystemInfoAdapter extends BaseServiceInfoAdapter<SystemInfo> {

    public SystemInfoAdapter(Context context, SystemInfo info) {
        super(context, R.array.system_info_labels, info);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.label.setText(labels[position]);
        String value = "";
        switch (resIds[position]) {
            case R.string.system_info_label_api_version:
                value = PaymentApi.getApiVersion();
                break;
            case R.string.system_info_label_fps_version:
                value = PaymentApi.getProcessingServiceVersion(context);
                break;
            case R.string.system_info_label_num_flow_services:
                value = String.valueOf(info.getNumFlowServices());
                break;
            case R.string.system_info_label_num_devices:
                value = String.valueOf(info.getNumDevices());
                break;
            case R.string.system_info_label_num_payment_services:
                value = String.valueOf(info.getNumPaymentServices());
                break;
            case R.string.system_info_label_all_fs_capabilities:
                value = getArrayValue(info.getAllFlowServiceCapabilities());
                break;
            case R.string.system_info_label_all_currencies:
                value = getArrayValue(info.getAllCurrencies());
                break;
            case R.string.system_info_label_all_data_keys:
                value = getArrayValue(info.getAllDataKeys());
                break;
            case R.string.system_info_label_all_payment_methods:
                value = getArrayValue(info.getAllPaymentMethods());
                break;
            case R.string.system_info_label_all_request_types:
                value = getArrayValue(info.getAllRequestTypes());
                break;
            case R.string.system_info_label_all_transaction_types:
                value = getArrayValue(info.getAllTransactionTypes());
                break;
        }
        holder.value.setText(value);
    }
}
