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

import com.aevi.sdk.flow.FlowClient;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentFlowServices;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.model.RequestStatus;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Payment client that exposes all the functions supported to query for payment services and initiate payments, etc.
 *
 * Please see {@link com.aevi.sdk.flow.FlowClient} for more general flow functions.
 */
public interface PaymentClient extends FlowClient {

    /**
     * Retrieve a {@link PaymentFlowServices} that allows you to easily query for data and/or support across all the services.
     *
     * @return Single emitting a {@link PaymentFlowServices} object
     */
    @NonNull
    Single<PaymentFlowServices> getPaymentFlowServices();

    /**
     * Initiate payment processing based on the provided {@link Payment}.
     *
     * In order to receive processing status updates for this payment, please subscribe via {@link #subscribeToStatusUpdates(String)}.
     *
     * @param payment The payment to process
     * @return Single emitting a {@link PaymentResponse} object containing all the details of the processing
     */
    @NonNull
    Single<PaymentResponse> initiatePayment(Payment payment);

    /**
     * Initiate payment processing based on the provided {@link Payment} and send it to the requested payment service and/or device
     *
     * In order to receive processing status updates for this payment, please subscribe via {@link #subscribeToStatusUpdates(String)}.
     *
     * @param payment          The payment to process
     * @param paymentServiceId The id of the payment service that should be used to process the payment request
     * @param deviceId         The id of the device that should be used to process the payment request
     * @return Single emitting a {@link PaymentResponse} object containing all the details of the processing
     */
    @NonNull
    Single<PaymentResponse> initiatePayment(Payment payment, String paymentServiceId, String deviceId);

    /**
     * Get the current status of the payment previously initiated via {@link #initiatePayment(Payment)}.
     *
     * Note that this is a synchronous, blocking call. Do not call this on the UI thread.
     *
     * @param paymentId The id of the payment get status for
     * @return A {@link RequestStatus} object containing the current payment status of the payment processing
     */
    @NonNull
    RequestStatus getCurrentPaymentStatus(String paymentId);

    /**
     * Subscribe to receive status updates of a particular payment.
     *
     * @param paymentId The id of the payment to listen to status updates for
     * @return Observable emitting a stream of {@link RequestStatus} objects indicating the status of the payment processing
     */
    @NonNull
    Observable<RequestStatus> subscribeToStatusUpdates(String paymentId);
}
