package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;


import com.aevi.sdk.flow.model.FlowServiceInfo;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PopupActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.FlowServicesAdapter;

public class FlowServicesFragment extends BaseItemFragment<FlowServiceInfo> {

    @Override
    protected void setupItems() {
        title.setText(R.string.title_select_flow_service);
        getSampleContext().getPaymentClient().getFlowServices()
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
        popupActivity.showServiceInfoFragment(ServiceInfoFragment.ADAPTER_FLOW_SERVICE_INFO, flowServiceInfo.toJson());
    }
}
