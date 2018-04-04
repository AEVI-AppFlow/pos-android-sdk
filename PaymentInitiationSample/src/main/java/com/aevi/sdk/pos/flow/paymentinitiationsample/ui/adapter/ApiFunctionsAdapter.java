package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;


import android.view.View;

import com.aevi.sdk.pos.flow.paymentinitiationsample.model.ApiFunction;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.ui.library.recycler.BaseTwoLineAdapter;

import java.util.List;

public class ApiFunctionsAdapter extends BaseTwoLineAdapter<ApiFunction> {

    public ApiFunctionsAdapter(List<ApiFunction> items, OnItemSelectedListener listener, boolean withContextMenu) {
        super(items, listener, withContextMenu);
    }

    @Override
    protected int getSnippetResource() {
        return R.layout.snippet_two_line_item_large_title;
    }

    @Override
    protected void onBindViewHolderToObject(BaseTwoLineAdapter.ViewHolder holder, ApiFunction apiFunction, int i) {
        holder.title.setText(apiFunction.getName());
        holder.subtitle.setText(apiFunction.getDescription());
        holder.subtitle.setVisibility(View.VISIBLE);
    }
}
