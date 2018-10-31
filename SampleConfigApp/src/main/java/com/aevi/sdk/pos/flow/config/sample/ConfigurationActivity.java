package com.aevi.sdk.pos.flow.config.sample;

import com.aevi.sdk.pos.flow.config.ui.BaseConfigurationActivity;

public class ConfigurationActivity extends BaseConfigurationActivity {

    @Override
    protected int[] getAppColorList() {
        return getResources().getIntArray(R.array.flowAppColorList);
    }

}
