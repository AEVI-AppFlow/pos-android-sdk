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
package com.aevi.sdk.pos.flow.config.sample.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aevi.sdk.app.scanning.model.AppInfoModel;
import com.aevi.sdk.pos.flow.config.sample.DefaultConfigProvider;
import com.aevi.sdk.pos.flow.config.sample.R;
import com.aevi.sdk.flow.model.config.FlowStage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlowSectionRecyclerList extends LinearLayout implements FlowAppViewAdapter.OnFlowSectionListener {

    private FlowAppViewAdapter adapter;

    private OnChangeCompleteListener onChangeCompleteListener;
    private FlowStage flowStage;

    @BindView(R.id.section_title)
    TextView title;

    @BindView(R.id.section_flow_app_type)
    TextView flowAppType;

    @BindView(R.id.flow_app_list)
    RecyclerView flowAppList;

    public FlowSectionRecyclerList(Context context) {
        super(context);
        init(context, null);
    }

    public FlowSectionRecyclerList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    protected void init(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.flow_section_layout, this, true);
        ButterKnife.bind(this, v);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        flowAppList.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        flowAppList.addItemDecoration(dividerItemDecoration);
    }

    public FlowSectionRecyclerList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowSectionRecyclerList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setFlowApps(List<AppInfoModel> flowApps) {
        if (flowApps != null) {
            if (adapter == null) {
                adapter = new FlowAppViewAdapter(getContext(), flowApps, this);
                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(flowAppList);
                flowAppList.setAdapter(adapter);
            } else {
                adapter.setApps(flowApps);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public List<AppInfoModel> getFlowApps() {
        return adapter.getFlowApps();
    }

    @Override
    public void notifyAppsChanged(List<AppInfoModel> apps) {
        adapter.setApps(apps);
        if (!flowAppList.isComputingLayout()) {
            adapter.notifyDataSetChanged();
            if (onChangeCompleteListener != null) {
                onChangeCompleteListener.onChangeComplete(flowStage);
            }
            DefaultConfigProvider.notifyConfigUpdated(this.getContext());
        }
    }

    public void setOnChangeCompleteListener(OnChangeCompleteListener listener) {
        onChangeCompleteListener = listener;
    }

    public void setFlowStage(FlowStage flowStage) {
        this.flowStage = flowStage;
        this.title.setText(flowStage.getName().replace('_', ' '));
        this.flowAppType.setText(flowStage.getAppExecutionType().name());
    }

    public interface OnChangeCompleteListener {

        void onChangeComplete(FlowStage stage);

    }
}
