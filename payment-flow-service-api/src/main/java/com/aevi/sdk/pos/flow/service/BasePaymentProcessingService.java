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

package com.aevi.sdk.pos.flow.service;


import com.aevi.sdk.flow.service.BaseApiService;
import com.aevi.sdk.pos.flow.PaymentFlowServiceApi;
import com.aevi.sdk.pos.flow.model.TransactionRequest;
import com.aevi.sdk.pos.flow.model.TransactionResponse;
import com.aevi.sdk.pos.flow.provider.BasePaymentFlowServiceInfoProvider;

/**
 * This abstract service should be extended to provide the core, mandatory payment service implementation to handle payment requests.
 *
 * The implementing class should be added as a service in the <code>AndroidManifest.xml</code> of your application and should also provide some meta-data information.
 *
 * <p><strong>payment-service-label</strong> - Is a string that is used when the name of the payment application should be displayed for selection by a user</p>
 * <p><strong>configuration-authority</strong> - Is the authority name of a corresponding content provider that will provide payment service configuration info</p>
 *
 * <pre>
 *     {@code
 *          <service android:name=".services.PaymentService" android:exported="true">
 *              <intent-filter>
 *                  <action android:name="com.aevi.payment.PROCESS_PAYMENT"/>
 *              </intent-filter>
 *
 *              <meta-data
 *                      android:name="configuration-authority"
 *                      android:value="com.example.payment.service.config"/>
 *              <meta-data
 *                      android:name="payment-service-label"
 *                      android:value="Example Payment Service"/>
 *          </service>
 *
 *          <provider
 *              android:name=".services.ConfigurationProvider"
 *              android:authorities="com.example.payment.service.config"
 *              android:exported="true"/>
 *      }
 * </pre>
 *
 * The configuration provider should extend {@link BasePaymentFlowServiceInfoProvider}
 */
public abstract class BasePaymentProcessingService extends BaseApiService<TransactionRequest, TransactionResponse> {

    public BasePaymentProcessingService() {
        super(TransactionRequest.class, PaymentFlowServiceApi.getApiVersion());
    }
}
