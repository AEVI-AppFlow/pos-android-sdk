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


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import butterknife.BindView;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.FlowServiceInfoAdapter;

@SuppressLint("ValidFragment")
public class ServiceInfoFragment extends BaseFragment {

    public static final String ADAPTER_FLOW_SERVICE_INFO = "flowServiceInfo";

    private static final String ARG_ADAPTER = "adapter";
    private static final String ARG_MODEL = "model";

    @BindView(R.id.items)
    RecyclerView infoItems;

    @BindView(R.id.title)
    TextView title;

    private String adapter;
    private String model;

    public static ServiceInfoFragment create(String adapter, String model) {
        ServiceInfoFragment fragment = new ServiceInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADAPTER, adapter);
        args.putString(ARG_MODEL, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        adapter = args.getString(ARG_ADAPTER);
        model = args.getString(ARG_MODEL);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView(infoItems);
        if (adapter.equals(ADAPTER_FLOW_SERVICE_INFO)) {
            PaymentFlowServiceInfo serviceInfo = PaymentFlowServiceInfo.fromJson(model);
            title.setText(serviceInfo.getDisplayName());
            FlowServiceInfoAdapter serviceInfoAdapter = new FlowServiceInfoAdapter(getContext(), serviceInfo);
            infoItems.setAdapter(serviceInfoAdapter);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_recycler_view;
    }
}
