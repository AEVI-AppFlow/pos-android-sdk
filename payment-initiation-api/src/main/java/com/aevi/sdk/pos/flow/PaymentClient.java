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
import com.aevi.sdk.flow.constants.ErrorConstants;
import com.aevi.sdk.flow.model.Device;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.service.BaseResponseListenerService;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.config.PaymentSettings;
import com.aevi.sdk.pos.flow.service.BasePaymentResponseListenerService;
import io.reactivex.Completable;
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
     * Subscribe to system events via {@link #subscribeToSystemEvents()} for updates when the state changes.
     *
     * @return Single emitting a {@link PaymentSettings} instance
     */
    @NonNull
    Single<PaymentSettings> getPaymentSettings();

    /**
     * Initiate processing of the provided {@link Request}.
     *
     * Due to the nature of Android component lifecycles, AppFlow can not guarantee that your activity/service is still alive when a flow is complete,
     * meaning it may not be able to receive the response via this rx chain. To ensure that your application receives a response in a reliable way,
     * your application must instead implement a {@link BaseResponseListenerService}. See documentation for further info.
     *
     * This method returns a {@link Completable} that will complete successfully if the request is accepted, or send an error if the request is invalid.
     *
     * If your request is rejected or an error occurs during the flow, a {@link FlowException} will be delivered to the `onError` handler. This
     * {@link FlowException} contains an error code that can be mapped to one of the constants in {@link ErrorConstants} and an error message
     * that further describes the problem. These values are not intended to be presented directly to the merchant.
     *
     * @param request The request
     * @return Completable that represents the acceptance of the request
     */
    @NonNull
    Completable initiateRequest(Request request);

    /**
     * Initiate payment processing based on the provided {@link Payment}.
     *
     * Due to the nature of Android component lifecycles, AppFlow can not guarantee that your activity/service is still alive when a flow is complete,
     * meaning it may not be able to receive the response via this rx chain. To ensure that your application receives a response in a reliable way,
     * your application must instead implement a {@link BasePaymentResponseListenerService}. See documentation for further info.
     *
     * This method returns a {@link Completable} that will complete successfully if the request is accepted, or send an error if the request is invalid.
     *
     * If your request is rejected or an error occurs during the flow, a {@link FlowException} will be delivered to the `onError` handler. This
     * {@link FlowException} contains an error code that can be mapped to one of the constants in {@link ErrorConstants} and an error message
     * that further describes the problem. These values are not intended to be presented directly to the merchant.
     *
     * @param payment The payment to process
     * @return Completable that represents the acceptance of the request
     */
    @NonNull
    Completable initiatePayment(Payment payment);

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
