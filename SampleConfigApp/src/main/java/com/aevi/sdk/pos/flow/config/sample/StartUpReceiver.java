package com.aevi.sdk.pos.flow.config.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aevi.sdk.pos.flow.config.sample.flowapps.FlowAppChangeReceiver;

import javax.inject.Inject;

public class StartUpReceiver extends BroadcastReceiver {

    @Inject
    FlowAppChangeReceiver flowAppChangeReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        FpsConfig.getFpsConfigComponent().inject(this);
        flowAppChangeReceiver.registerForBroadcasts();
    }
}
