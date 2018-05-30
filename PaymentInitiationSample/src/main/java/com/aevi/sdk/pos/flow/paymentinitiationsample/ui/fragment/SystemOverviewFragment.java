package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;


import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SystemOverview;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.SystemOverviewAdapter;

import butterknife.BindView;
import io.reactivex.Single;

public class SystemOverviewFragment extends BaseFragment {

    @BindView(R.id.items)
    RecyclerView infoItems;

    @BindView(R.id.title)
    TextView title;

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView(infoItems);
        title.setText(R.string.system_overview);

        createSystemInfo().subscribe(systemOverview -> {
            SystemOverviewAdapter systemOverviewAdapter = new SystemOverviewAdapter(getContext(), systemOverview);
            infoItems.setAdapter(systemOverviewAdapter);
        });
    }

    private Single<SystemOverview> createSystemInfo() {
        PaymentClient paymentClient = getSampleContext().getPaymentClient();
        return Single.zip(paymentClient.getFlowServices(), paymentClient.getPaymentServices(), paymentClient.getDevices(),
                (flowServices, paymentServices, devices) -> {
                    SystemOverview systemOverview = new SystemOverview();
                    systemOverview.setNumFlowServices(flowServices.getAllFlowServices().size());
                    systemOverview.setNumPaymentServices(paymentServices.getAllPaymentServices().size());
                    systemOverview.setNumDevices(devices.size());
                    systemOverview.setAllCurrencies(paymentServices.getAllSupportedCurrencies());
                    systemOverview.addPaymentMethods(flowServices.getAllSupportedPaymentMethods());
                    systemOverview.addPaymentMethods(paymentServices.getAllSupportedPaymentMethods());
                    systemOverview.addDataKeys(flowServices.getAllSupportedDataKeys());
                    systemOverview.addDataKeys(paymentServices.getAllSupportedDataKeys());
                    systemOverview.addRequestTypes(flowServices.getAllSupportedRequestTypes());
                    systemOverview.addRequestTypes(paymentServices.getAllSupportedRequestTypes());
                    systemOverview.setAllTransactionTypes(paymentServices.getAllSupportedTransactionTypes());
                    return systemOverview;
                });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recycler_view;
    }
}
