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
import com.aevi.sdk.flow.service.BaseServiceInfoProvider;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;

import static com.aevi.sdk.flow.constants.IntentActions.SERVICE_INFO_CHANGE_ACTION;

/**
 * Base class for flow services to provide {@link PaymentFlowServiceInfo} information.
 *
 * @see <a href="https://github.com/AEVI-AppFlow/pos-android-sdk/wiki/implementing-flow-services" target="_blank">Implementing Flow Services</a>
 */
public abstract class BasePaymentFlowServiceInfoProvider extends BaseServiceInfoProvider {

    protected BasePaymentFlowServiceInfoProvider() {
        super(SERVICE_INFO_CHANGE_ACTION);
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
        notifyServiceInfoChange(context, SERVICE_INFO_CHANGE_ACTION);
    }
}
