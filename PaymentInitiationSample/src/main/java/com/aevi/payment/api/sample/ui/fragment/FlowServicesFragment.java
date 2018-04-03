package com.aevi.payment.api.sample.ui.fragment;


import com.aevi.payment.api.sample.R;
import com.aevi.payment.api.sample.ui.PopupActivity;
import com.aevi.payment.api.sample.ui.adapter.FlowServicesAdapter;
import com.aevi.sdk.flow.model.FlowServiceInfo;

import static com.aevi.payment.api.sample.ui.PopupActivity.FRAGMENT_SERVICE_INFO;

public class FlowServicesFragment extends BaseItemFragment<FlowServiceInfo> {

    @Override
    protected void setupItems() {
        title.setText(R.string.title_select_flow_service);
        getSampleContext().getFlowClient().getFlowServices()
                .subscribe(flowServices -> {
                    if (flowServices.getAllFlowServices().isEmpty()) {
                        showNoItemsAvailable(R.string.no_flow_services_found);
                    } else {
                        FlowServicesAdapter adapter = new FlowServicesAdapter(flowServices.getAllFlowServices(),
                                FlowServicesFragment.this, false);
                        items.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onItemSelected(FlowServiceInfo flowServiceInfo) {
        PopupActivity popupActivity = (PopupActivity) getActivity();
        popupActivity.showFragment(FRAGMENT_SERVICE_INFO, ServiceInfoFragment.ADAPTER_FLOW_SERVICE_INFO, flowServiceInfo.toJson());
    }
}
