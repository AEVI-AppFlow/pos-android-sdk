package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.ui.library.BaseObservableFragment;

abstract class BaseFragment extends BaseObservableFragment {

    protected void setupRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager serviceInfoViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(serviceInfoViewLayoutManager);
    }

    protected SampleContext getSampleContext() {
        return SampleContext.getInstance(getContext());
    }
}
