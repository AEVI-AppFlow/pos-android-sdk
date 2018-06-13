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

package com.aevi.sdk.pos.flow.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.aevi.sdk.flow.service.BaseServiceInfoProvider;
import com.aevi.sdk.pos.flow.model.PaymentServiceInfo;

/**
 * ContentProvider base class that should be extended by payment service providers in order to give information as to the capabilities of a payment
 * service implementation. The implementing class will need to provide an implementation of the {@link #getServiceInfo()} method that should return
 * the configuration of the payment service. If this configuration is dynamic and changes then the implementing class should call
 * {@link #notifyServiceInfoChange()} on any changes so that the new configuration can be obtained by the system.
 *
 * The implementation of this class should be added as a provider in the <code>AndroidManifest.xml</code>
 *
 * <pre>
 *     {@code
 *          <provider
 *              android:name=".services.ConfigurationProvider"
 *              android:authorities="com.example.payment.service.config"
 *              android:exported="true"/>
 *     }
 * </pre>
 */
public abstract class BasePaymentServiceInfoProvider extends BaseServiceInfoProvider {

    public static final String ACTION_BROADCAST_PAYMENT_INFO_CHANGE = "com.aevi.intent.action.PAYMENT_SERVICE_INFO_CHANGE";

    protected BasePaymentServiceInfoProvider() {
        super(ACTION_BROADCAST_PAYMENT_INFO_CHANGE);
    }

    @Override
    protected String getServiceInfo() {
        return getPaymentServiceInfo().toJson();
    }

    protected abstract PaymentServiceInfo getPaymentServiceInfo();

    /**
     * Notify the system that the configuration has changed.
     *
     * @param context Android context
     */
    public static void notifyServiceInfoChange(Context context) {
        String pkg = "package:" + context.getPackageName();
        Uri pkgUri = Uri.parse(pkg);
        context.sendBroadcast(new Intent(ACTION_BROADCAST_PAYMENT_INFO_CHANGE).setData(pkgUri));
    }
}