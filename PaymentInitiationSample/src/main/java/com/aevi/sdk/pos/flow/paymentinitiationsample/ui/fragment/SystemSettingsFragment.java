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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aevi.sdk.flow.model.config.FlowConfig;
import com.aevi.sdk.flow.model.config.FpsSettings;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PopupActivity;

import butterknife.BindView;

public class SystemSettingsFragment extends BaseFragment {

    @BindView(R.id.settings_container)
    LinearLayout settingsContainer;

    @BindView(R.id.settings_info)
    TextView settingsInfo;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_system_settings;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateSystemSettings();
    }

    private void populateSystemSettings() {
        SampleContext.getInstance(getActivity()).getPaymentClient().getPaymentSettings().subscribe(systemSettings -> {
            FpsSettings fpsSettings = systemSettings.getFpsSettings();
            StringBuilder stringBuilder = new StringBuilder();
            addEnabledDisabled(stringBuilder, R.string.multi_device, fpsSettings.isMultiDevice());
            addEnabledDisabled(stringBuilder, R.string.currency_change, fpsSettings.isCurrencyChangeAllowed());
            addTimeout(stringBuilder, R.string.split_response_timeout, fpsSettings.getSplitResponseTimeoutSeconds());
            addTimeout(stringBuilder, R.string.payment_response_timeout, fpsSettings.getPaymentResponseTimeoutSeconds());
            addTimeout(stringBuilder, R.string.flow_response_timeout, fpsSettings.getFlowResponseTimeoutSeconds());
            addTimeout(stringBuilder, R.string.merchant_selection_timeout, fpsSettings.getAppOrDeviceSelectionTimeoutSeconds());
            addEnabledDisabled(stringBuilder, R.string.abort_on_flow_app_error, fpsSettings.shouldAbortOnFlowAppError());
            addEnabledDisabled(stringBuilder, R.string.abort_on_payment_app_error, fpsSettings.shouldAbortOnPaymentAppError());

            settingsInfo.setText(stringBuilder.toString());

            systemSettings.getFlowConfigurations().stream().subscribe(this::handleFlowConfig);
        }, throwable -> settingsInfo.setText("Operation failed: " + throwable.getMessage()));
    }

    private void handleFlowConfig(FlowConfig flowConfig) {
        ViewGroup buttonLayout = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.snippet_request_type_button, settingsContainer, false);

        Button flowConfigJsonButton = buttonLayout.findViewById(R.id.flow_config_json);
        flowConfigJsonButton.setText(flowConfig.getType());
        flowConfigJsonButton.setOnClickListener(view -> showJsonView(flowConfig.getType(), flowConfig.toJson()));

        settingsContainer.addView(buttonLayout);
    }

    private void showJsonView(String requestType, String json) {
        ((PopupActivity) getActivity()).showJsonFragment(requestType, json);
    }

    private void addEnabledDisabled(StringBuilder stringBuilder, int resId, boolean value) {
        stringBuilder.append(getString(resId));
        stringBuilder.append(value ? getString(R.string.enabled) : getString(R.string.disabled));
        stringBuilder.append("\n");
    }

    private void addTimeout(StringBuilder stringBuilder, int resId, long timeout) {
        stringBuilder.append(getString(resId));
        stringBuilder.append(timeout);
        stringBuilder.append(getString(R.string.seconds));
        stringBuilder.append("\n");
    }
}
