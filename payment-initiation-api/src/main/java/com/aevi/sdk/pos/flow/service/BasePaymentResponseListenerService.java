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

import com.aevi.sdk.flow.service.BaseListenerService;
import com.aevi.sdk.pos.flow.PaymentInitiationConfig;
import com.aevi.sdk.pos.flow.model.PaymentResponse;

/**
 * Extend this service in your application if you want to listen to payment responses initiated by your application,
 * or if you are implementing a flow service and want to listen to the final `PaymentResponse` when the flow is complete.
 *
 * @see <a href="https://github.com/AEVI-AppFlow/pos-android-sdk/wiki/flow-response-listeners" target="_blank">Response listener docs</a>
 */
public abstract class BasePaymentResponseListenerService extends BaseListenerService<PaymentResponse> {

    protected BasePaymentResponseListenerService() {
        super(PaymentResponse.class, PaymentInitiationConfig.VERSION);
    }
}
