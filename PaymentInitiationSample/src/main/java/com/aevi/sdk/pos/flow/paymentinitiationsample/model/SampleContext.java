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

package com.aevi.sdk.pos.flow.paymentinitiationsample.model;


import android.content.Context;

import com.aevi.sdk.pos.flow.PaymentApi;
import com.aevi.sdk.pos.flow.PaymentClient;
import com.aevi.sdk.pos.flow.model.PaymentResponse;

import java.util.ArrayList;
import java.util.List;

// To keep the sample simple, we use a singleton context (aka service provider) as opposed to dependency injection.
public class SampleContext {

    private static SampleContext INSTANCE;

    public static SampleContext getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SampleContext(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private final PaymentClient paymentClient;
    private final SystemEventHandler systemEventHandler;

    private PaymentResponse lastReceivedPaymentResponse;

    private SampleContext(Context context) {
        this.paymentClient = PaymentApi.getPaymentClient(context);
        this.systemEventHandler = new SystemEventHandler();
        systemEventHandler.subscribeToEvents(paymentClient);
    }

    public PaymentClient getPaymentClient() {
        return paymentClient;
    }

    public SystemEventHandler getSystemEventHandler() {
        return systemEventHandler;
    }

    public List<ApiFunction> getApiChoices() {
        List<ApiFunction> apiFunctions = new ArrayList<>();
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.SYSTEM_OVERVIEW, "System overview", "Overview of configured flows, settings and capabilities "));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.FLOW_SERVICES, "List flow services", "Query the API for information about available flow services"));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.DEVICES, "List devices", "Query the API for a list of available devices"));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.GENERIC_REQUEST, "Initiate a non-payment request", "Initiate a general financial request, such as reversal or tokenisation"));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.INITIATE_PAYMENT, "Initiate a payment", "Choose between a wide range of options to initiate a specific payment"));
        apiFunctions.add(new ApiFunction(ApiFunction.ApiMethod.SUBSCRIBE_EVENTS, "System events", "Display received system events"));
        return apiFunctions;
    }

    public PaymentResponse getLastReceivedPaymentResponse() {
        return lastReceivedPaymentResponse;
    }

    public void setLastReceivedPaymentResponse(PaymentResponse lastReceivedPaymentResponse) {
        this.lastReceivedPaymentResponse = lastReceivedPaymentResponse;
    }

}
