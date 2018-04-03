package com.aevi.payment.api.sample.ui.adapter;


import android.view.View;

import com.aevi.payment.api.sample.R;
import com.aevi.sdk.pos.flow.model.PaymentServiceInfo;
import com.aevi.ui.library.recycler.BaseTwoLineAdapter;

import java.util.List;

public class PaymentServicesAdapter extends BaseTwoLineAdapter<PaymentServiceInfo> {

    public PaymentServicesAdapter(List<PaymentServiceInfo> items, OnItemSelectedListener listener, boolean withContextMenu) {
        super(items, listener, withContextMenu);
    }

    @Override
    protected int getSnippetResource() {
        return R.layout.snippet_two_line_item_medium_title;
    }

    @Override
    protected void onBindViewHolderToObject(BaseTwoLineAdapter.ViewHolder holder, PaymentServiceInfo paymentServiceInfo, int i) {
        holder.title.setText(paymentServiceInfo.getDisplayName());
        holder.subtitle.setText(paymentServiceInfo.getVendor());
        holder.subtitle.setVisibility(View.VISIBLE);
    }
}
