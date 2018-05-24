package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SampleContext;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.SystemEventAdapter;

import java.util.List;

import butterknife.BindView;

public class SystemEventFragment extends BaseFragment {

    @BindView(R.id.items)
    RecyclerView infoItems;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.no_items)
    TextView noEvents;

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView(infoItems);
        title.setText(R.string.system_events);
        List<FlowEvent> receivedFlowEvents = SampleContext.getInstance(getActivity()).getSystemEventHandler().getReceivedFlowEvents();
        if (!receivedFlowEvents.isEmpty()) {
            infoItems.setAdapter(new SystemEventAdapter(receivedFlowEvents, null));
        } else {
            infoItems.setVisibility(View.GONE);
            noEvents.setVisibility(View.VISIBLE);
            noEvents.setText(R.string.no_events);
        }
    }

}
