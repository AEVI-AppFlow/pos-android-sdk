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

import com.aevi.sdk.flow.constants.SystemSettingsKeys;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PopupActivity;

import butterknife.BindView;

import static com.aevi.sdk.flow.constants.SystemSettingsKeys.*;

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
        SampleContext.getInstance(getActivity()).getPaymentClient().getSystemSettings().subscribe(systemInfoData -> {
            StringBuilder stringBuilder = new StringBuilder();
            addEnabledDisabled(systemInfoData, stringBuilder, R.string.multi_device, SYSTEM_SETTINGS_KEY_MULTI_DEVICE_ENABLED);
            addTimeout(systemInfoData, stringBuilder, R.string.split_response_timeout, SYSTEM_SETTINGS_KEY_SPLIT_RESPONSE_TIMEOUT_SECONDS);
            addTimeout(systemInfoData, stringBuilder, R.string.payment_response_timeout, SYSTEM_SETTINGS_KEY_PAYMENT_SERVICE_RESPONSE_TIMEOUT_SECONDS);
            addTimeout(systemInfoData, stringBuilder, R.string.flow_response_timeout, SYSTEM_SETTINGS_KEY_FLOW_SERVICE_RESPONSE_TIMEOUT_SECONDS);
            addTimeout(systemInfoData, stringBuilder, R.string.merchant_selection_timeout, SYSTEM_SETTINGS_KEY_MERCHANT_SELECTION_TIMEOUT_SECONDS);
            addEnabledDisabled(systemInfoData, stringBuilder, R.string.abort_on_flow_app_error, SYSTEM_SETTINGS_KEY_ABORTS_ON_FLOW_SERVICE_ERROR);
            addEnabledDisabled(systemInfoData, stringBuilder, R.string.abort_on_payment_app_error, SYSTEM_SETTINGS_KEY_ABORTS_ON_PAYMENT_SERVICE_ERROR);

            settingsInfo.setText(stringBuilder.toString());

            AdditionalData flowConfigs = systemInfoData.getValue(SystemSettingsKeys.SYSTEM_SETTINGS_KEY_FLOW_CONFIGS, AdditionalData.class);
            for (String requestType : flowConfigs.getKeys()) {
                handleRequestType(requestType, flowConfigs);
            }
        }, throwable -> settingsInfo.setText("Operation failed: " + throwable.getMessage()));
    }

    private void handleRequestType(String requestType, AdditionalData flowConfigs) {
        ViewGroup buttonLayout = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.snippet_request_type_button, settingsContainer, false);
        TextView requestTypeView = buttonLayout.findViewById(R.id.request_type);
        requestTypeView.setText(requestType);

        AdditionalData requestConfig = flowConfigs.getValue(requestType, AdditionalData.class);
        String requestStagesJson = requestConfig.getStringValue(SYSTEM_SETTINGS_KEY_REQUEST_STAGES_JSON);
        String flowConfigJson = requestConfig.getStringValue(SYSTEM_SETTINGS_KEY_FLOW_CONFIG_JSON);

        Button requestStageJsonButton = buttonLayout.findViewById(R.id.request_stage_json);
        requestStageJsonButton.setOnClickListener(view -> showJsonView(requestType, requestStagesJson));
        Button flowConfigJsonButton = buttonLayout.findViewById(R.id.flow_config_json);
        flowConfigJsonButton.setOnClickListener(view -> showJsonView(requestType, flowConfigJson));

        settingsContainer.addView(buttonLayout);
    }

    private void showJsonView(String requestType, String json) {
        ((PopupActivity) getActivity()).showJsonFragment(requestType, json);
    }

    private void addEnabledDisabled(AdditionalData additionalData, StringBuilder stringBuilder, int resId, String key) {
        stringBuilder.append(getString(resId));
        stringBuilder.append(additionalData.getValue(key, Boolean.class, false) ? getString(R.string.enabled) : getString(R.string.disabled));
        stringBuilder.append("\n");
    }

    private void addTimeout(AdditionalData additionalData, StringBuilder stringBuilder, int resId, String key) {
        stringBuilder.append(getString(resId));
        stringBuilder.append(additionalData.getValue(key, Integer.class, 0));
        stringBuilder.append(getString(R.string.seconds));
        stringBuilder.append("\n");
    }
}
