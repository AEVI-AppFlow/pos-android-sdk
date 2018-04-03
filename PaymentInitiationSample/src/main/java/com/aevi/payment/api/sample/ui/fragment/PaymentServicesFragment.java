package com.aevi.payment.api.sample.ui.fragment;


import com.aevi.payment.api.sample.R;
import com.aevi.payment.api.sample.ui.PopupActivity;
import com.aevi.payment.api.sample.ui.adapter.PaymentServicesAdapter;
import com.aevi.sdk.pos.flow.model.PaymentServiceInfo;

import static com.aevi.payment.api.sample.ui.PopupActivity.FRAGMENT_SERVICE_INFO;

public class PaymentServicesFragment extends BaseItemFragment<PaymentServiceInfo> {

    @Override
    protected void setupItems() {
        title.setText(R.string.title_select_payment_service);
        getSampleContext().getPaymentClient().getPaymentServices()
                .subscribe(paymentServices -> {
                    if (paymentServices.getAllPaymentServices().isEmpty()) {
                        showNoItemsAvailable(R.string.no_payment_services_found);
                    } else {
                        PaymentServicesAdapter adapter = new PaymentServicesAdapter(paymentServices.getAllPaymentServices(),
                                PaymentServicesFragment.this, false);
                        items.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onItemSelected(PaymentServiceInfo serviceInfo) {
        PopupActivity popupActivity = (PopupActivity) getActivity();
        popupActivity.showFragment(FRAGMENT_SERVICE_INFO, ServiceInfoFragment.ADAPTER_PAYMENT_SERVICE_INFO, serviceInfo.toJson());
    }
}
