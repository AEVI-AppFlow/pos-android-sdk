package com.aevi.sdk.pos.flow.config.sample;

import com.aevi.sdk.pos.flow.config.BaseConfigProviderApplication;

public class SampleConfigProviderApplication extends BaseConfigProviderApplication {

    @Override
    protected int[] getFlowConfigs() {
        return new int[]{R.raw.flow_sale, R.raw.flow_refund, R.raw.flow_sample_reversal, R.raw.flow_sample_tokenisation};
    }
}
