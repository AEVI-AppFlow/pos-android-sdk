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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter;


import android.content.Context;

import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.model.SystemOverview;

public class SystemOverviewAdapter extends BaseServiceInfoAdapter<SystemOverview> {

    public SystemOverviewAdapter(Context context, SystemOverview info) {
        super(context, R.array.system_info_labels, info);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.label.setText(labels[position]);
        String value = "";
        switch (resIds[position]) {
            case R.string.system_info_label_api_version:
                value = PaymentApi.getApiVersion();
                break;
            case R.string.system_info_label_fps_version:
                value = PaymentApi.getProcessingServiceVersion(context);
                break;
            case R.string.system_info_label_num_flow_services:
                value = String.valueOf(info.getNumFlowServices());
                break;
            case R.string.system_info_label_num_devices:
                value = String.valueOf(info.getNumDevices());
                break;
            case R.string.system_info_label_num_payment_services:
                value = String.valueOf(info.getNumPaymentServices());
                break;
            case R.string.system_info_label_all_currencies:
                value = getArrayValue(info.getAllCurrencies());
                break;
            case R.string.system_info_label_all_data_keys:
                value = getArrayValue(info.getAllDataKeys());
                break;
            case R.string.system_info_label_all_payment_methods:
                value = getArrayValue(info.getAllPaymentMethods());
                break;
            case R.string.system_info_label_all_request_types:
                value = getArrayValue(info.getAllRequestTypes());
                break;
            case R.string.system_info_label_all_transaction_types:
                value = getArrayValue(info.getAllTransactionTypes());
                break;
        }
        holder.value.setText(value);
    }
}
