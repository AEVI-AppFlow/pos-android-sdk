package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;


import android.content.Context;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.flow.model.FlowServiceInfo;

import java.util.Arrays;

public class FlowServiceInfoAdapter extends BaseServiceInfoAdapter<FlowServiceInfo> {

    public FlowServiceInfoAdapter(Context context, FlowServiceInfo flowServiceInfo) {
        super(context, R.array.flow_service_labels, flowServiceInfo);
    }

    @Override
    public void onBindViewHolder(FlowServiceInfoAdapter.ViewHolder holder, int position) {
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
            case R.string.fs_label_stages:
                value = getArrayValue(info.getStages());
                break;
            case R.string.fs_label_capabilities:
                value = getArrayValue(info.getCapabilities());
                break;
            case R.string.fs_label_can_adjust_amounts:
                value = getYesNo(info.canAdjustAmounts());
                break;
            case R.string.fs_label_can_pay_amounts:
                value = getPayAmountsValue();
                break;
            case R.string.service_label_currencies:
                value = info.getSupportedCurrencies().length > 0 ? Arrays.toString(info.getSupportedCurrencies()) : "All currencies";
                break;
            case R.string.service_label_supported_data_keys:
                value = getArrayValue(info.getSupportedDataKeys());
                break;
            case R.string.fs_label_background_only:
                value = getYesNo(info.isBackgroundOnly());
                break;
            case R.string.service_label_request_types:
                value = getArrayValue(info.getSupportedRequestTypes());
                break;
        }
        holder.value.setText(value);
    }

    private String getPayAmountsValue() {
        if (info.canPayAmounts()) {
            return yes + ", via payment methods: " + Arrays.toString(info.getPaymentMethods());
        }
        return no;
    }
}
