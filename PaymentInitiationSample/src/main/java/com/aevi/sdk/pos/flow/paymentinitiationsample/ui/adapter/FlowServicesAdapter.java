package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;


import android.view.View;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.flow.model.FlowServiceInfo;
import com.aevi.ui.library.recycler.BaseTwoLineAdapter;

import java.util.List;

public class FlowServicesAdapter extends BaseTwoLineAdapter<FlowServiceInfo> {

    public FlowServicesAdapter(List<FlowServiceInfo> items, OnItemSelectedListener listener, boolean withContextMenu) {
        super(items, listener, withContextMenu);
    }

    @Override
    protected int getSnippetResource() {
        return R.layout.snippet_two_line_item_medium_title;
    }

    @Override
    protected void onBindViewHolderToObject(BaseTwoLineAdapter.ViewHolder holder, FlowServiceInfo flowServiceInfo, int i) {
        holder.title.setText(flowServiceInfo.getDisplayName());
        holder.subtitle.setText(flowServiceInfo.getVendor());
        holder.subtitle.setVisibility(View.VISIBLE);
    }
}
