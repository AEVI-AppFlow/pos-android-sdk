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


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentFlowServices;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PaymentResultActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PopupActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.FlowServicesAdapter;

public class FlowServicesFragment extends BaseItemFragment<PaymentFlowServiceInfo> {

    private FlowServicesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupRecyclerView(items);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            items.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void setupItems() {
        title.setText(R.string.title_select_flow_service);
        getSampleContext().getPaymentClient().getPaymentSettings()
                .subscribe(paymentSettings -> {
                    PaymentFlowServices paymentFlowServices = paymentSettings.getPaymentFlowServices();
                    if (paymentFlowServices.getNumberOfFlowServices() == 0) {
                        showNoItemsAvailable(R.string.no_flow_services_found);
                    } else {
                        adapter = new FlowServicesAdapter(paymentFlowServices.getAll(),
                                                          FlowServicesFragment.this, false);
                        items.setAdapter(adapter);
                    }
                }, throwable -> {
                    if (throwable instanceof FlowException) {
                        Intent errorIntent = new Intent(getContext(), PaymentResultActivity.class);
                        errorIntent.putExtra(PaymentResultActivity.ERROR_KEY, ((FlowException) throwable).toJson());
                        startActivity(errorIntent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Unrecoverable error occurred - see logs", Toast.LENGTH_SHORT).show();
                        Log.e(FlowServicesFragment.class.getSimpleName(), "Error", throwable);
                        getActivity().finish();
                    }
                });
    }

    @Override
    public void onItemSelected(PaymentFlowServiceInfo paymentFlowServiceInfo) {
        PopupActivity popupActivity = (PopupActivity) getActivity();
        popupActivity.showServiceInfoFragment(ServiceInfoFragment.ADAPTER_FLOW_SERVICE_INFO, paymentFlowServiceInfo.toJson());
    }
}
