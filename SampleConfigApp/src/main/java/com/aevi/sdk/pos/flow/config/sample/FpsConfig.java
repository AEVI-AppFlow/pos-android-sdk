package com.aevi.sdk.pos.flow.config.sample;

import android.app.Application;
import android.content.Context;

import com.aevi.sdk.pos.flow.config.sample.dagger.DaggerFpsConfigComponent;
import com.aevi.sdk.pos.flow.config.sample.dagger.FpsConfigComponent;
import com.aevi.sdk.pos.flow.config.sample.dagger.FpsConfigModule;
import com.aevi.sdk.pos.flow.config.sample.flowapps.AppEntityScanningHelper;

import javax.inject.Inject;

public class FpsConfig extends Application {

    protected static FpsConfigComponent fpsComponent;

    @Inject
    AppEntityScanningHelper appEntityScanningHelper;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        setupDagger();
        scanForApps();
    }

    private void scanForApps() {
        appEntityScanningHelper.reScanForPaymentAndFlowApps();
    }

    protected void setupDagger() {
        fpsComponent = DaggerFpsConfigComponent.builder()
                .fpsConfigModule(new FpsConfigModule(this))
                .build();

        fpsComponent.inject(this);
    }

    public static FpsConfigComponent getFpsConfigComponent() {
        return fpsComponent;
    }
}
