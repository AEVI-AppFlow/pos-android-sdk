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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.config.FlowConfig;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SystemOverview;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PaymentResultActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PopupActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.SystemOverviewAdapter;
import io.reactivex.Single;

public class SystemOverviewFragment extends BaseFragment implements SystemOverviewAdapter.OnFlowConfigClickListener {

    @BindView(R.id.items)
    RecyclerView infoItems;

    @BindView(R.id.title)
    TextView title;

    private SystemOverviewAdapter systemOverviewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        title.setText(R.string.system_overview);
        setupRecyclerView(infoItems);
        if (systemOverviewAdapter == null) {
            createSystemInfo().subscribe(systemOverview -> {
                systemOverviewAdapter = new SystemOverviewAdapter(getContext(), systemOverview, this);
                infoItems.setAdapter(systemOverviewAdapter);
            }, throwable -> {
                if (throwable instanceof FlowException) {
                    Intent errorIntent = new Intent(getContext(), PaymentResultActivity.class);
                    errorIntent.putExtra(PaymentResultActivity.ERROR_KEY, ((FlowException) throwable).toJson());
                    startActivity(errorIntent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Unrecoverable error occurred - see logs", Toast.LENGTH_SHORT).show();
                    Log.e(SystemOverviewFragment.class.getSimpleName(), "Error", throwable);
                    getActivity().finish();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (systemOverviewAdapter != null) {
            infoItems.setAdapter(systemOverviewAdapter);
            systemOverviewAdapter.notifyDataSetChanged();
        }
    }

    private Single<SystemOverview> createSystemInfo() {
        PaymentClient paymentClient = getSampleContext().getPaymentClient();
        return Single.zip(paymentClient.getPaymentSettings(), paymentClient.getDevices(),
                          (paymentSettings, devices) -> {
                              SystemOverview systemOverview = new SystemOverview();
                              systemOverview.setPaymentFlowServices(paymentSettings.getPaymentFlowServices());
                              systemOverview.setFlowConfigurations(paymentSettings.getFlowConfigurations());
                              systemOverview.setNumDevices(devices.size());
                              systemOverview.setFpsSettings(paymentSettings.getFpsSettings());
                              return systemOverview;
                          });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void onClick(FlowConfig config) {
        showJsonView(config.getName(), config.toJson());
    }

    private void showJsonView(String requestType, String json) {
        ((PopupActivity) getActivity()).showJsonFragment(requestType, json);
    }

}
