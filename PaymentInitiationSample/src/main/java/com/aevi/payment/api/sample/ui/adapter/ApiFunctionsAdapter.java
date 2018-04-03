package com.aevi.payment.api.sample.ui.adapter;


import android.view.View;

import com.aevi.payment.api.sample.R;
import com.aevi.payment.api.sample.model.ApiFunction;
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
