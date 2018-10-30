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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.aevi.sdk.app.scanning.model.AppInfoModel;
import com.aevi.sdk.pos.flow.config.sample.DefaultConfigProvider;
import com.aevi.sdk.pos.flow.config.sample.FpsConfig;
import com.aevi.sdk.pos.flow.config.sample.R;
import com.aevi.sdk.pos.flow.config.sample.SettingsProvider;
import com.aevi.sdk.pos.flow.config.sample.flowapps.AppDatabase;
import com.aevi.sdk.pos.flow.config.sample.flowapps.FlowConfigStore;
import com.aevi.sdk.pos.flow.config.sample.ui.view.AppGridAdapter;
import com.aevi.sdk.pos.flow.config.sample.ui.view.FlowSectionRecyclerList;
import com.aevi.sdk.flow.model.config.AppExecutionType;
import com.aevi.sdk.flow.model.config.FlowApp;
import com.aevi.sdk.flow.model.config.FlowConfig;
import com.aevi.sdk.flow.model.config.FlowStage;
import com.aevi.ui.library.recycler.DropDownSpinner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnItemSelected;

public class FlowConfigurationFragment extends BaseFragment {

    private static final String TAG = FlowConfigurationFragment.class.getSimpleName();

    private static final String KEY_POS_STORE = "keyPosStore";
    private static final String KEY_TX_SPINNER = "txSpinner";

    private final Map<String, FlowSectionRecyclerList> displayColumns = new HashMap<>();
    private boolean flowChanged;

    @BindView(R.id.select_app_grid)
    RecyclerView appGrid;

    @BindView(R.id.flow_section_scroll)
    NestedScrollView flowSectionScrollView;

    @BindView(R.id.flow_section_holder)
    LinearLayout flowSectionHolder;

    @BindView(R.id.transaction_type_spinner)
    DropDownSpinner flowNameSpinner;

    @BindView(R.id.no_flow_config_message)
    TextView noFlowConfigMessage;

    @BindView(R.id.auto_switch)
    Switch autoModeSwitch;

    @Inject
    FlowConfigStore flowConfigStore;

    @Inject
    AppDatabase appDatabase;

    @Inject
    SettingsProvider settingsProvider;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_flow_configuration;
    }

    private String flowName;
    private String appType;
    private SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FpsConfig.getFpsConfigComponent().inject(this);
        setRetainInstance(true);
        preferences = getContext().getSharedPreferences(KEY_POS_STORE, Context.MODE_PRIVATE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autoModeSwitch.setVisibility(View.VISIBLE);
        autoModeSwitch.setOnCheckedChangeListener((compoundButton, mode) -> settingsProvider.updateAutoGenerateConfig(mode));
        autoModeSwitch.setChecked(settingsProvider.shouldAutoGenerateConfigs());
    }

    @Override
    public void onResume() {
        super.onResume();
        setupTransactionTypes();
        setupStages();
        setupAppGridRecyclerView();
        scanForFlowApps();

        observe(appDatabase.subscribeToAppUpdates()).subscribe(appInfoModels -> scanForFlowApps());
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.edit()
                .putInt(KEY_TX_SPINNER, flowNameSpinner.getSelectedItemPosition())
                .apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (flowChanged) {
            DefaultConfigProvider.notifyConfigUpdated(getContext().getApplicationContext());
        }
    }

    private void setupTransactionTypes() {
        Set<String> flowNames = flowConfigStore.getAllFlowNames();
        setupFlows(flowNames);
    }

    private void setupFlows(Set<String> flowNames) {
        // TODO - sort out ordering here
        ArrayAdapter requestTypeAdapter = new ArrayAdapter<>(this.getContext(), R.layout.dropdown_layout,
                flowNames.toArray(new String[flowNames.size()]));
        requestTypeAdapter.setDropDownViewResource(R.layout.dropdown_item);
        flowNameSpinner.setAdapter(requestTypeAdapter);
        flowNameSpinner.setSpinnerEventsListener(new DropDownSpinner.OnSpinnerEventsListener() {
            public void onSpinnerOpened() {
                flowNameSpinner.setSelected(true);
            }

            public void onSpinnerClosed() {
                flowNameSpinner.setSelected(false);
            }
        });
        setSpinnerToPos(KEY_TX_SPINNER, flowNameSpinner);
    }

    protected void setupStages() {
        flowSectionHolder.removeAllViews();
        if (flowName != null) {
            FlowConfig flowConfig = flowConfigStore.readFlowConfig(flowName);
            List<FlowStage> flowStages = flowConfig.getStages(true);
            boolean configurable = false;
            if (flowStages.size() > 0) {
                for (FlowStage stage : flowStages) {
                    if (stage.getAppExecutionType() != AppExecutionType.NONE) {
                        configurable = true;
                        FlowSectionRecyclerList flowSectionRecyclerList = new FlowSectionRecyclerList(getContext());
                        flowSectionRecyclerList.setFlowStage(stage);
                        displayColumns.put(stage.getName(), flowSectionRecyclerList);
                        List<FlowApp> flowApps = stage.getFlowApps();
                        List<AppInfoModel> models = appDatabase.getAppInfoModelsForFlowApps(flowApps);
                        setupAppColumn(flowSectionRecyclerList, models);
                        flowSectionHolder.addView(flowSectionRecyclerList);
                    }
                }
            }

            if (configurable) {
                noFlowConfigMessage.setVisibility(View.GONE);
                flowSectionHolder.setVisibility(View.VISIBLE);
            } else {
                noFlowConfigMessage.setVisibility(View.VISIBLE);
                flowSectionHolder.setVisibility(View.GONE);
            }
        }
    }

    private void setSpinnerToPos(String prefKey, Spinner spinner) {
        int position = preferences.getInt(prefKey, 0);
        if (position < spinner.getCount()) {
            spinner.setSelection(position);
        }
    }

    @OnItemSelected(R.id.transaction_type_spinner)
    public void transactionTypeSelected(Spinner spinner, int position) {
        flowName = (String) spinner.getItemAtPosition(position);
        setupStages();
    }

    private void scanForFlowApps() {
        if (isAdded()) {
            updateFlowApps(appDatabase.getAll());
        }
    }

    private void setupAppColumn(FlowSectionRecyclerList flowSection, List<AppInfoModel> flowapps) {
        flowSection.setFlowApps(flowapps);
        flowSection.setOnChangeCompleteListener(requestStage -> {
            List<AppInfoModel> apps = displayColumns.get(requestStage.getName()).getFlowApps();
            flowConfigStore.setAppsOrder(flowName, requestStage.getName(), apps);
        });
    }

    public void setupAppGridRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), calculateNoOfColumns());
        appGrid.setLayoutManager(gridLayoutManager);
    }

    private int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
        if (isLandscape()) {
            dpWidth /= 2;
        }
        float appIconWidth = getResources().getDimensionPixelSize(R.dimen.app_grid_width);
        return (int) (dpWidth / appIconWidth);
    }

    private void updateFlowApps(List<AppInfoModel> flowApps) {
        AppGridAdapter adapter = new AppGridAdapter(flowApps, (app) -> {
            if (flowName != null) {
                flowConfigStore.toggleFlowAppInConfig(flowName, app);
            }
            setupStages();
            flowChanged = true;
            DefaultConfigProvider.notifyConfigUpdated(this.getContext());
            String stage = app.getStages().iterator().next();
            if (displayColumns.containsKey(stage)) {
                FlowSectionRecyclerList stageView = displayColumns.get(stage);
                flowSectionScrollView.requestChildFocus(stageView, stageView);
            }
        });

        appGrid.setAdapter(adapter);
    }
}
