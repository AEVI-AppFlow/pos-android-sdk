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

package com.aevi.sdk.pos.flow;


import android.support.annotation.NonNull;
import com.aevi.sdk.flow.model.*;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.model.config.PaymentSettings;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;

/**
 * Payment client that exposes all the functions supported to query for payment services and initiate payments, etc.
 */
public interface PaymentClient {

    /**
     * Retrieve a snapshot of the current payment settings.
     *
     * This includes system settings, flow configurations, information about flow services, etc.
     *
     * @return Single emitting a {@link PaymentSettings} instance
     */
    @NonNull
    Single<PaymentSettings> getPaymentSettings();

    /**
     * Initiate processing of the given {@link Request}.
     *
     * Returns a single that emits a {@link Response} after processing has completed.
     *
     * If a fatal error occurs during the flow then a {@link FlowException} will be delivered to the `onError` handler. This
     * {@link FlowException} will contain an errorCode that can be used to display information to the user or to handle
     * the error appropriately
     *
     * @param request The request
     * @return Single emitting a {@link Response} model
     */
    @NonNull
    Single<Response> initiateRequest(Request request);

    /**
     * Initiate payment processing based on the provided {@link Payment}.
     *
     * If a fatal error occurs during the flow then a {@link FlowException} will be delivered to the `onError` handler. This
     * {@link FlowException} will contain an errorCode that can be used to display information to the user or to handle
     * the error appropriately
     *
     * @param payment The payment to process
     * @return Single emitting a {@link PaymentResponse} object containing all the details of the processing
     */
    @NonNull
    Single<PaymentResponse> initiatePayment(Payment payment);

    /**
     * Query for devices connected to the processing service, if multi-device is enabled.
     *
     * It is up to the flow processing service configuration if multi-device is enabled or not. See {@link PaymentSettings} for more details.
     *
     * Returns a single that emits a list of currently connected devices.
     *
     * This should be queried each time a selection is required to ensure an up-to-date list.
     *
     * You can subscribe to {@link #subscribeToSystemEvents()} for updates on changes to the available devices.
     *
     * @return Single emitting a list of {@link Device} objects containing basic device info
     */
    @NonNull
    Single<List<Device>> getDevices();

    /**
     * Subscribe to general system events.
     *
     * Examples are when there are changed to devices, applications or system settings.
     * See documentation for a comprehensive list.
     *
     * @return A stream that will emit {@link FlowEvent} items
     */
    @NonNull
    Observable<FlowEvent> subscribeToSystemEvents();
}
