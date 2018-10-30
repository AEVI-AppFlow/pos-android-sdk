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
package com.aevi.sdk.pos.flow.config.sample.flowapps;

import android.content.Context;

import com.aevi.sdk.app.scanning.listener.AppChangeReceiver;
import com.aevi.sdk.pos.flow.config.sample.FpsConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FlowAppChangeReceiver extends AppChangeReceiver {

    private final AppEntityScanningHelper appEntityScanningHelper;
    private final Context context;

    public FlowAppChangeReceiver() {
        super(FpsConfig.getFpsConfigComponent().provideAppDatabase());
        this.appEntityScanningHelper = FpsConfig.getFpsConfigComponent().provideAppEntityScanningHelper();
        this.context = FpsConfig.getFpsConfigComponent().provideApplicationContext();
    }

    @Inject
    public FlowAppChangeReceiver(AppDatabase appDatabase, AppEntityScanningHelper appEntityScanningHelper, Context context) {
        super(appDatabase);
        this.appEntityScanningHelper = appEntityScanningHelper;
        this.context = context;
    }

    @Override
    protected void onPaymentFlowServiceChange() {
        appEntityScanningHelper.reScanForPaymentAndFlowApps();
    }

    public void registerForBroadcasts() {
        registerForBroadcasts(context, true);
    }
}
