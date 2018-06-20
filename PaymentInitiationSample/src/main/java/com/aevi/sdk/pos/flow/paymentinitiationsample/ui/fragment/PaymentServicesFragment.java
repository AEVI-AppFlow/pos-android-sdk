/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;


import android.widget.Toast;

import com.aevi.sdk.pos.flow.model.PaymentServiceInfo;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PopupActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.PaymentServicesAdapter;


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
                }, throwable -> {
                    if (throwable instanceof IllegalStateException) {
                        Toast.makeText(getContext(), "FPS is not installed on the device", Toast.LENGTH_SHORT).show();
                    }
                    showNoItemsAvailable(R.string.no_payment_services_found);
                });
    }

    @Override
    public void onItemSelected(PaymentServiceInfo serviceInfo) {
        PopupActivity popupActivity = (PopupActivity) getActivity();
        popupActivity.showServiceInfoFragment(ServiceInfoFragment.ADAPTER_PAYMENT_SERVICE_INFO, serviceInfo.toJson());
    }
}
