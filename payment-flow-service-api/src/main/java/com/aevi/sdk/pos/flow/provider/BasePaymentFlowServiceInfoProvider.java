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

package com.aevi.sdk.pos.flow.provider;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.aevi.sdk.flow.service.BaseServiceInfoProvider;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;

/**
 * Base class for flow services to provide {@link PaymentFlowServiceInfo} information.
 */
public abstract class BasePaymentFlowServiceInfoProvider extends BaseServiceInfoProvider {

    public static final String ACTION_BROADCAST_FLOW_INFO_CHANGE = "com.aevi.intent.action.PAYMENT_FLOW_SERVICE_INFO_CHANGE";

    protected BasePaymentFlowServiceInfoProvider() {
        super(ACTION_BROADCAST_FLOW_INFO_CHANGE);
    }

    @Override
    protected String getServiceInfo() {
        return getPaymentFlowServiceInfo().toJson();
    }

    protected abstract PaymentFlowServiceInfo getPaymentFlowServiceInfo();

    /**
     * Notify the system that the configuration has changed.
     *
     * @param context The Android context
     */
    public static void notifyServiceInfoChange(Context context) {
        String pkg = "package:" + context.getPackageName();
        Uri pkgUri = Uri.parse(pkg);
        context.sendBroadcast(new Intent(ACTION_BROADCAST_FLOW_INFO_CHANGE).setData(pkgUri));
    }
}
