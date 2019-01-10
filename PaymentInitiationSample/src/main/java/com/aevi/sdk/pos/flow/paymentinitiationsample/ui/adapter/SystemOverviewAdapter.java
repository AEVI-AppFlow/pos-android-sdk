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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.aevi.sdk.flow.model.config.FlowConfig;
import com.aevi.sdk.flow.model.config.FpsSettings;
import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.model.PaymentFlowServices;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SystemOverview;

import java.util.List;

public class SystemOverviewAdapter extends BaseServiceInfoAdapter<SystemOverview> {

    private final OnFlowConfigClickListener listener;

    private final String[] fpsSettingsLabels;
    private int[] fpsSettingsResIds;

    private final List<FlowConfig> flowConfigs;

    public SystemOverviewAdapter(Context context, SystemOverview info, OnFlowConfigClickListener listener) {
        super(context, R.array.system_info_labels, info);
        this.listener = listener;

        flowConfigs = info.getFlowConfigurations().getAll();

        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.fps_settings_labels);
        fpsSettingsLabels = new String[typedArray.length()];
        fpsSettingsResIds = new int[typedArray.length()];
        for (int i = 0; i < fpsSettingsLabels.length; i++) {
            fpsSettingsLabels[i] = typedArray.getString(i);
            fpsSettingsResIds[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
    }

    public interface OnFlowConfigClickListener {

        void onClick(FlowConfig config);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            handleViewLine(position, (ViewHolder) viewHolder);
        } else {
            handleHeaderLine(position, (ViewHeaderHolder) viewHolder);
        }
    }

    private void handleHeaderLine(int position, @NonNull ViewHeaderHolder viewHolder) {
        if (position == resIds.length + 1 + fpsSettingsResIds.length) {
            viewHolder.header.setText(R.string.system_overview_header_flow_configs);
        } else {
            viewHolder.header.setText(R.string.system_overview_header_fps_settings);
        }
    }

    private void handleViewLine(int position, @NonNull ViewHolder holder) {
        String value = "";
        holder.lineLayout.setOnClickListener(null);
        if (isFlowConfig(position)) {
            value = handleFlowConfig(position, holder, info.getFlowConfigurations().getAll());
        } else if (isFpsSetting(position)) {
            value = handleFpsSettings(position, holder, info.getFpsSettings());
        } else if (isInfo(position)) {
            value = handleSystemInfo(position, holder, info.getPaymentFlowServices());
        }

        if (value.isEmpty()) {
            holder.value.setVisibility(View.GONE);
        } else {
            holder.value.setText(value);
        }
    }

    private boolean isInfo(int position) {
        return position < resIds.length;
    }

    private boolean isFlowConfig(int position) {
        return position > resIds.length + fpsSettingsResIds.length + 1;
    }

    private boolean isFpsSetting(int position) {
        return position > resIds.length && position < resIds.length + fpsSettingsResIds.length + 1;
    }

    @Override
    protected boolean isPositionHeader(int position) {
        return position == resIds.length || position == resIds.length + fpsSettingsResIds.length + 1;
    }

    private String handleSystemInfo(int position, @NonNull ViewHolder holder, PaymentFlowServices paymentFlowServices) {
        holder.label.setText(labels[position]);
        switch (resIds[position]) {
            case R.string.system_info_label_api_version:
                return PaymentApi.getApiVersion();
            case R.string.system_info_label_fps_version:
                return PaymentApi.getProcessingServiceVersion(context);
            case R.string.system_info_label_num_flow_services:
                return String.valueOf(paymentFlowServices.getNumberOfFlowServices());
            case R.string.system_info_label_num_devices:
                return String.valueOf(info.getNumDevices());
            case R.string.system_info_label_all_currencies:
                return getSetValue(paymentFlowServices.getAllSupportedCurrencies());
            case R.string.system_info_label_all_data_keys:
                return getSetValue(paymentFlowServices.getAllSupportedDataKeys());
            case R.string.system_info_label_all_payment_methods:
                return getSetValue(paymentFlowServices.getAllSupportedPaymentMethods());
            case R.string.system_info_label_all_request_types:
                return getSetValue(paymentFlowServices.getAllCustomRequestTypes());
        }
        return "";
    }

    private String handleFpsSettings(int position, ViewHolder holder, FpsSettings fpsSettings) {
        int index = position - (resIds.length + 1);
        holder.label.setText(fpsSettingsLabels[index]);
        switch (fpsSettingsResIds[index]) {
            case R.string.multi_device:
                return getYesNo(fpsSettings.isMultiDeviceEnabled());
            case R.string.currency_change:
                return getYesNo(fpsSettings.isCurrencyChangeAllowed());
            case R.string.split_response_timeout:
                return toSeconds(fpsSettings.getSplitResponseTimeoutSeconds());
            case R.string.payment_response_timeout:
                return toSeconds(fpsSettings.getPaymentResponseTimeoutSeconds());
            case R.string.flow_response_timeout:
                return toSeconds(fpsSettings.getFlowResponseTimeoutSeconds());
            case R.string.merchant_selection_timeout:
                return toSeconds(fpsSettings.getUserSelectionTimeoutSeconds());
            case R.string.status_update_timeout:
                return toSeconds(fpsSettings.getStatusUpdateTimeoutSeconds());
            case R.string.abort_on_flow_app_error:
                return getYesNo(fpsSettings.shouldAbortOnFlowAppError());
            case R.string.abort_on_payment_app_error:
                return getYesNo(fpsSettings.shouldAbortOnPaymentAppError());
            case R.string.filter_services_by_flow_type:
                return getYesNo(fpsSettings.shouldFilterServicesByFlowType());
            case R.string.always_allow_preflow:
                return getYesNo(fpsSettings.shouldAlwaysCallPreFlow());
            case R.string.enable_legacy_pas:
                return getYesNo(fpsSettings.legacyPaymentAppsEnabled());
        }
        return "";
    }

    @NonNull
    private String handleFlowConfig(int position, @NonNull ViewHolder holder, List<FlowConfig> flowConfigs) {
        String value;
        final FlowConfig config = flowConfigs.get(position - resIds.length - fpsSettingsResIds.length - 2);
        holder.label.setText(holder.label.getContext().getString(R.string.system_info_label_flow_names, config.getName()));
        value = config.getType();
        holder.lineLayout.setOnClickListener(view -> {
            if (listener != null) {
                listener.onClick(config);
            }
        });
        return value;
    }

    private String toSeconds(long timeout) {
        return timeout + " seconds";
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + fpsSettingsLabels.length + flowConfigs.size() + 2;
    }
}
