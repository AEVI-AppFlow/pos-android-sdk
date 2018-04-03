package com.aevi.payment.api.sample.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aevi.payment.api.sample.model.SampleContext;
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
