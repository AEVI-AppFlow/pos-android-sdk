package com.aevi.sdk.pos.flow.config.sample;

import com.aevi.sdk.pos.flow.config.BaseConfigProviderApplication;

public class SampleConfigProviderApplication extends BaseConfigProviderApplication {

    @Override
    protected void onComponentsReady() {
        getSettingsProvider().updateAutoGenerateConfig(true);
        getSettingsProvider().setOverwriteConfigsOnReinstall(true);
    }

    @Override
    protected int[] getFlowConfigs() {
        return new int[]{R.raw.flow_sale, R.raw.flow_refund, R.raw.flow_reversal, R.raw.flow_tokenisation};
    }
}
