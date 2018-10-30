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
package com.aevi.sdk.pos.flow.config.sample.dagger;

import android.app.Application;
import android.content.Context;

import com.aevi.sdk.pos.flow.config.sample.DefaultConfigProvider;
import com.aevi.sdk.pos.flow.config.sample.FpsConfig;
import com.aevi.sdk.pos.flow.config.sample.StartUpReceiver;
import com.aevi.sdk.pos.flow.config.sample.flowapps.AppDatabase;
import com.aevi.sdk.pos.flow.config.sample.flowapps.AppEntityScanningHelper;
import com.aevi.sdk.pos.flow.config.sample.ui.ConfigurationActivity;
import com.aevi.sdk.pos.flow.config.sample.ui.FlowConfigurationFragment;
import com.aevi.sdk.pos.flow.config.sample.ui.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {
                FpsConfigModule.class,
        })
@Singleton
public interface FpsConfigComponent {

    Application provideApplication();

    Context provideApplicationContext();

    AppEntityScanningHelper provideAppEntityScanningHelper();

    AppDatabase provideAppDatabase();

    void inject(FlowConfigurationFragment flowConfigurationFragment);

    void inject(SettingsFragment settingsFragment);

    void inject(FpsConfig fpsConfig);

    void inject(DefaultConfigProvider defaultConfigProvider);

    void inject(ConfigurationActivity configurationActivity);

    void inject(StartUpReceiver startUpReceiver);
}
