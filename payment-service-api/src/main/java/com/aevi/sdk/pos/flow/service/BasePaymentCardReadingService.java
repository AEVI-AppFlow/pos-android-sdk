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
import com.aevi.sdk.pos.flow.PaymentServiceApi;
import com.aevi.sdk.pos.flow.model.CardResponse;
import com.aevi.sdk.pos.flow.model.TransactionRequest;

/**
 * Base service to extend for payment services that wish to support the separate flow card reading step.
 */
public abstract class BasePaymentCardReadingService extends BaseApiService<TransactionRequest, CardResponse> {

    public BasePaymentCardReadingService() {
        super(TransactionRequest.class, PaymentServiceApi.getApiVersion());
    }
}
