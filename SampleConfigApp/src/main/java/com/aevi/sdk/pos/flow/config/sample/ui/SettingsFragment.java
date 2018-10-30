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
package com.aevi.sdk.pos.flow.config.sample.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aevi.sdk.pos.flow.config.sample.DefaultConfigProvider;
import com.aevi.sdk.pos.flow.config.sample.FpsConfig;
import com.aevi.sdk.pos.flow.config.sample.R;
import com.aevi.sdk.pos.flow.config.sample.SettingsProvider;
import com.aevi.sdk.pos.flow.config.sample.ui.view.EditTimeout;
import com.aevi.sdk.pos.flow.config.sample.ui.view.SettingSwitch;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

import static com.aevi.sdk.pos.flow.config.sample.model.Channels.MESSENGER;
import static com.aevi.sdk.pos.flow.config.sample.model.Channels.WEBSOCKET;

public class SettingsFragment extends BaseFragment {

    private static final int TIMEOUT_MIN = 0;
    private static final int TIMEOUT_MAX = 3600;

    @BindView(R.id.split_timeout)
    EditTimeout splitTimeout;

    @BindView(R.id.flow_response_timeout)
    EditTimeout flowResponseTimeout;

    @BindView(R.id.payment_response_timeout)
    EditTimeout paymentResponseTimeout;

    @BindView(R.id.select_timeout)
    EditTimeout selectTimeout;

    @BindView(R.id.abort_on_flow_error)
    SettingSwitch abortOnFlow;

    @BindView(R.id.abort_on_payment_error)
    SettingSwitch abortOnPayment;

    @BindView(R.id.allow_status_bar_access)
    SettingSwitch allowStatusBar;

    @BindView(R.id.use_websocket)
    SettingSwitch useWebsocket;

    @Inject
    SettingsProvider settingsProvider;

    @Inject
    Context appContext;

    private boolean settingsChanged = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FpsConfig.getFpsConfigComponent().inject(this);
        setRetainInstance(true);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (settingsChanged) {
            DefaultConfigProvider.notifyConfigUpdated(appContext);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupTimeouts();
        setupFlags();
    }

    private void setupFlags() {
        abortOnPayment.subscribeToValueChanges().subscribe(new FlagChange(R.id.abort_on_payment_error));
        abortOnFlow.subscribeToValueChanges().subscribe(new FlagChange(R.id.abort_on_flow_error));
        allowStatusBar.subscribeToValueChanges().subscribe(new FlagChange(R.id.allow_status_bar_access));
        useWebsocket.subscribeToValueChanges().subscribe(new FlagChange(R.id.use_websocket));
        abortOnPayment.setChecked(settingsProvider.shouldAbortOnPaymentAppError());
        abortOnFlow.setChecked(settingsProvider.shouldAbortOnFlowAppError());
        allowStatusBar.setChecked(settingsProvider.allowAccessViaStatusBar());
        useWebsocket.setChecked(settingsProvider.getCommsChannel().equals(WEBSOCKET));
    }

    protected void setupTimeouts() {
        setupEditTimeout(splitTimeout, settingsProvider.getSplitResponseTimeoutSeconds(), R.id.split_timeout);
        setupEditTimeout(flowResponseTimeout, settingsProvider.getFlowResponseTimeoutSeconds(), R.id.flow_response_timeout);
        setupEditTimeout(paymentResponseTimeout, settingsProvider.getPaymentResponseTimeoutSeconds(), R.id.payment_response_timeout);
        setupEditTimeout(selectTimeout, settingsProvider.getAppOrDeviceSelectionTimeoutSeconds(), R.id.select_timeout);
    }

    private void setupEditTimeout(EditTimeout editTimeout, int value, int resId) {
        editTimeout.setMinMax(TIMEOUT_MIN, TIMEOUT_MAX);
        editTimeout.setInitialValue(value);
        observe(editTimeout.subscribeToValueChanges()).subscribe(new TimeoutChange(resId));
    }

    class TimeoutChange implements Consumer<Integer> {

        private final int resId;

        TimeoutChange(int resId) {
            this.resId = resId;
        }

        @Override
        public void accept(Integer integer) throws Exception {
            switch (resId) {
                case R.id.split_timeout:
                    settingsProvider.setSplitResponseTimeoutSeconds(integer);
                    break;
                case R.id.flow_response_timeout:
                    settingsProvider.setFlowResponseTimeoutSeconds(integer);
                    break;
                case R.id.payment_response_timeout:
                    settingsProvider.setPaymentResponseTimeoutSeconds(integer);
                    break;
                case R.id.select_timeout:
                    settingsProvider.setAppOrDeviceSelectionTimeoutSeconds(integer);
                    break;
            }
            settingsChanged = true;
        }
    }

    class FlagChange implements Consumer<Boolean> {

        private final int resId;

        FlagChange(int resId) {
            this.resId = resId;
        }

        @Override
        public void accept(Boolean value) throws Exception {
            switch (resId) {
                case R.id.abort_on_payment_error:
                    settingsProvider.abortOnPaymentError(value);
                    break;
                case R.id.abort_on_flow_error:
                    settingsProvider.abortOnFlowError(value);
                    break;
                case R.id.allow_status_bar_access:
                    settingsProvider.allowStatusBarAccess(value);
                    break;
                case R.id.use_websocket:
                    if(value) {
                        settingsProvider.setCommsChannel(WEBSOCKET);
                    } else {
                        settingsProvider.setCommsChannel(MESSENGER);
                    }
            }
            settingsChanged = true;
        }
    }
}
