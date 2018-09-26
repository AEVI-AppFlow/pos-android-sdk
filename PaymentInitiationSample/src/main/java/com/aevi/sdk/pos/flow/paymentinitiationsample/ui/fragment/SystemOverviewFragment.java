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


import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

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
        }, throwable -> {
            if (throwable instanceof IllegalStateException) {
                Toast.makeText(getContext(), "FPS is not installed on the device", Toast.LENGTH_SHORT).show();
            }
            SystemOverviewAdapter systemOverviewAdapter = new SystemOverviewAdapter(getContext(), new SystemOverview());
            infoItems.setAdapter(systemOverviewAdapter);
        });
    }

    private Single<SystemOverview> createSystemInfo() {
        PaymentClient paymentClient = getSampleContext().getPaymentClient();
        return Single.zip(paymentClient.getPaymentSettings(), paymentClient.getDevices(),
                (paymentSettings, devices) -> {
                    SystemOverview systemOverview = new SystemOverview();
                    systemOverview.setPaymentFlowServices(paymentSettings.getPaymentFlowServices());
                    systemOverview.setNumDevices(devices.size());
                    return systemOverview;
                });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recycler_view;
    }
}
