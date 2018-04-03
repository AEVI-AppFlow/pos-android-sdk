package com.aevi.payment.api.sample.ui.fragment;


import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.aevi.payment.api.sample.R;
import com.aevi.payment.api.sample.ui.adapter.FlowServiceInfoAdapter;
import com.aevi.payment.api.sample.ui.adapter.PaymentServiceInfoAdapter;
import com.aevi.sdk.flow.model.FlowServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentServiceInfo;

import butterknife.BindView;

@SuppressLint("ValidFragment")
public class ServiceInfoFragment extends BaseFragment {

    public static final String ADAPTER_FLOW_SERVICE_INFO = "flowServiceInfo";
    public static final String ADAPTER_PAYMENT_SERVICE_INFO = "paymentServiceInfo";

    @BindView(R.id.items)
    RecyclerView infoItems;

    @BindView(R.id.title)
    TextView title;

    private final String adapter;
    private final String model;

    public ServiceInfoFragment(String adapter, String model) {
        this.adapter = adapter;
        this.model = model;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView(infoItems);
        if (adapter.equals(ADAPTER_FLOW_SERVICE_INFO)) {
            FlowServiceInfo serviceInfo = FlowServiceInfo.fromJson(model);
            title.setText(serviceInfo.getDisplayName());
            FlowServiceInfoAdapter serviceInfoAdapter = new FlowServiceInfoAdapter(getContext(), serviceInfo);
            infoItems.setAdapter(serviceInfoAdapter);
        } else if (adapter.equals(ADAPTER_PAYMENT_SERVICE_INFO)) {
            PaymentServiceInfo serviceInfo = PaymentServiceInfo.fromJson(model);
            title.setText(serviceInfo.getDisplayName());
            PaymentServiceInfoAdapter serviceInfoAdapter = new PaymentServiceInfoAdapter(getContext(), serviceInfo);
            infoItems.setAdapter(serviceInfoAdapter);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recycler_view;
    }
}
