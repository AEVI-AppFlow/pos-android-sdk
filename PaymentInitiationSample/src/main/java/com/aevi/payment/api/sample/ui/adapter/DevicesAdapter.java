package com.aevi.payment.api.sample.ui.adapter;


import android.view.View;

import com.aevi.payment.api.sample.R;
import com.aevi.sdk.flow.model.Device;
import com.aevi.ui.library.recycler.AbstractListWithMenuAdapter;
import com.aevi.ui.library.recycler.BaseTwoLineAdapter;

import java.util.List;


public class DevicesAdapter extends BaseTwoLineAdapter<Device> {

    public DevicesAdapter(List<Device> items, AbstractListWithMenuAdapter.OnItemSelectedListener listener, boolean withContextMenu) {
        super(items, listener, withContextMenu);
    }

    @Override
    protected int getSnippetResource() {
        return R.layout.snippet_two_line_item_medium_title;
    }

    @Override
    protected void onBindViewHolderToObject(BaseTwoLineAdapter.ViewHolder holder, Device device, int i) {
        holder.title.setText(device.getName());
        holder.subtitle.setText(device.getId());
        holder.subtitle.setVisibility(View.VISIBLE);
    }
}
