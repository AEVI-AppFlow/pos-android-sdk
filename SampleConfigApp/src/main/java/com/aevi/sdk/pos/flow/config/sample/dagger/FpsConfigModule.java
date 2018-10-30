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

import com.aevi.payment.legacy.app.scanning.LegacyPaymentAppScanner;
import com.aevi.sdk.app.scanning.PaymentFlowServiceScanner;
import com.aevi.sdk.pos.flow.config.sample.FpsConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FpsConfigModule {

    private final Application application;

    public FpsConfigModule(FpsConfig application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    PaymentFlowServiceScanner providePaymentFlowServiceScanner() {
        return new PaymentFlowServiceScanner(application);
    }

    @Provides
    @Singleton
    LegacyPaymentAppScanner provideLegacyPaymentAppScanner() {
        return new LegacyPaymentAppScanner(application);
    }
}