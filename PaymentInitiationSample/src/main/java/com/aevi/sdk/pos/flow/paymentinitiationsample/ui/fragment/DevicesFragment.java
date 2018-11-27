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
import android.util.Log;
import android.widget.Toast;
import com.aevi.sdk.flow.model.Device;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.PaymentResultActivity;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.DevicesAdapter;

public class DevicesFragment extends BaseItemFragment<Device> {

    @Override
    protected void setupItems() {
        title.setText(R.string.title_select_device);
        getSampleContext().getPaymentClient().getDevices()
                .subscribe(devices -> {
                    if (devices.isEmpty()) {
                        showNoItemsAvailable(R.string.no_devices_found);
                    } else {
                        DevicesAdapter adapter = new DevicesAdapter(devices, DevicesFragment.this, false);
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
                        Log.e(DevicesFragment.class.getSimpleName(), "Error", throwable);
                        getActivity().finish();
                    }
                });
    }

    @Override
    public void onItemSelected(Device device) {
    }
}
