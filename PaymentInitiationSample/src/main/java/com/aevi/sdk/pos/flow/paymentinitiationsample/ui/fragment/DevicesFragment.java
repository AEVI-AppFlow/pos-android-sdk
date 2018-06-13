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


import com.aevi.sdk.flow.model.Device;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
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
                });
    }

    @Override
    public void onItemSelected(Device device) {
    }
}
