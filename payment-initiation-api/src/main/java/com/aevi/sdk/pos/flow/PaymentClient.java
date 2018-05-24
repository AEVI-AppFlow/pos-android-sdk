package com.aevi.sdk.pos.flow;


import com.aevi.sdk.flow.FlowClient;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.model.PaymentServices;
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
     * Query for all the installed payment services available on this and connected devices.
     *
     * Returns a single that emits a {@link PaymentServices} model wrapping the list of available payment services.
     *
     * This should be queried each time a selection is required to ensure up to date information.
     *
     * You can subscribe to {@link com.aevi.sdk.flow.FlowClient#subscribeToSystemEvents()} for updates on changes to the available payment services.
     *
     * @return Single emitting a {@link PaymentServices} object
     */
    Single<PaymentServices> getPaymentServices();

    /**
     * Initiate payment processing based on the provided {@link Payment}.
     *
     * In order to receive processing status updates for this payment, please subscribe via {@link #subscribeToStatusUpdates(String)}.
     *
     * @param payment The payment to process
     * @return Single emitting a {@link PaymentResponse} object containing all the details of the processing
     */
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
    Single<PaymentResponse> initiatePayment(Payment payment, String paymentServiceId, String deviceId);

    /**
     * Get the current status of the payment previously initiated via {@link #initiatePayment(Payment)}.
     *
     * Note that this is a synchronous, blocking call. Do not call this on the UI thread.
     *
     * @param paymentId The id of the payment get status for
     * @return A {@link RequestStatus} object containing the current payment status of the payment processing
     */
    RequestStatus getCurrentPaymentStatus(String paymentId);

    /**
     * Subscribe to receive status updates of a particular payment.
     *
     * @param paymentId The id of the payment to listen to status updates for
     * @return Observable emitting a stream of {@link RequestStatus} objects indicating the status of the payment processing
     */
    Observable<RequestStatus> subscribeToStatusUpdates(String paymentId);
}
