package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;


import com.aevi.sdk.flow.model.Device;
import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.sdk.pos.flow.paymentinitiationsample.ui.adapter.DevicesAdapter;

public class DevicesFragment extends BaseItemFragment<Device> {

    @Override
    protected void setupItems() {
        title.setText(R.string.title_select_device);
        getSampleContext().getFlowClient().getDevices()
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
