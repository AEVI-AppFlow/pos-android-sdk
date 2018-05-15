package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;


import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SystemInfo;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.SystemInfoAdapter;

import butterknife.BindView;
import io.reactivex.Single;

public class SystemInfoFragment extends BaseFragment {

    @BindView(R.id.items)
    RecyclerView infoItems;

    @BindView(R.id.title)
    TextView title;

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView(infoItems);
        title.setText(R.string.system_overview);

        createSystemInfo().subscribe(systemInfo -> {
            SystemInfoAdapter systemInfoAdapter = new SystemInfoAdapter(getContext(), systemInfo);
            infoItems.setAdapter(systemInfoAdapter);
        });
    }

    private Single<SystemInfo> createSystemInfo() {
        PaymentClient paymentClient = getSampleContext().getPaymentClient();
        return Single.zip(paymentClient.getFlowServices(), paymentClient.getPaymentServices(), paymentClient.getDevices(),
                (flowServices, paymentServices, devices) -> {
                    SystemInfo systemInfo = new SystemInfo();
                    systemInfo.setNumFlowServices(flowServices.getAllFlowServices().size());
                    systemInfo.setNumPaymentServices(paymentServices.getAllPaymentServices().size());
                    systemInfo.setNumDevices(devices.size());
                    systemInfo.setAllFlowServiceCapabilities(flowServices.getAllCapabilities());
                    systemInfo.setAllCurrencies(paymentServices.getAllSupportedCurrencies());
                    systemInfo.addPaymentMethods(flowServices.getAllSupportedPaymentMethods());
                    systemInfo.addPaymentMethods(paymentServices.getAllSupportedPaymentMethods());
                    systemInfo.addDataKeys(flowServices.getAllSupportedDataKeys());
                    systemInfo.addDataKeys(paymentServices.getAllSupportedDataKeys());
                    systemInfo.addRequestTypes(flowServices.getAllSupportedRequestTypes());
                    systemInfo.addRequestTypes(paymentServices.getAllSupportedRequestTypes());
                    systemInfo.setAllTransactionTypes(paymentServices.getAllSupportedTransactionTypes());
                    return systemInfo;
                });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recycler_view;
    }
}
