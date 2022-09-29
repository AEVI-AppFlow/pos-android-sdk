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

package com.aevi.sdk.pos.flow.stage;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import com.aevi.sdk.flow.model.InternalData;
import com.aevi.sdk.flow.service.ClientCommunicator;
import com.aevi.sdk.flow.stage.BaseStageModel;
import com.aevi.sdk.pos.flow.model.FlowResponse;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentBuilder;
import com.aevi.sdk.pos.flow.service.ActivityProxyService;
import com.aevi.sdk.pos.flow.service.BasePaymentFlowService;


/**
 * Model for the pre-flow stage that exposes all the data functions and other utilities required for any app to process this stage.
 *
 * See {@link BasePaymentFlowService} for how to retrieve the model from a service context, and {@link ActivityProxyService} for
 * how to proxy the request onto an activity from where this can be instantiated via {@link #fromActivity(Activity)}.
 *
 * To update {@link Payment} data, use {@link #getPaymentBuilder()} to retrieve an instance of {@link PaymentBuilder}. Once finished,
 * call {@link #sendResponse()} to send off the payment data.
 *
 * To cancel the payment flow, call {@link #cancelFlow()}, or to skip/bypass this stage, call {@link #skip()}.
 */
public class PreFlowModel extends BaseStageModel {

    private final Payment payment;
    private final PaymentBuilder paymentBuilder;

    private PreFlowModel(Activity activity, Payment payment) {
        super(activity);
        this.payment = payment;
        this.paymentBuilder = new PaymentBuilder(payment);
    }

    private PreFlowModel(ClientCommunicator clientCommunicator, Payment payment, InternalData senderInternalData) {
        super(clientCommunicator, senderInternalData);
        this.payment = payment;
        this.paymentBuilder = new PaymentBuilder(payment);
    }

    /**
     * Create an instance from an activity context.
     *
     * This assumes that the activity was started via {@link BaseStageModel#processInActivity(Context, Class)},
     * or via the {@link ActivityProxyService}.
     *
     * @param activity The activity that was started via one of the means described above
     * @return An instance of {@link PreFlowModel}
     */
    @NonNull
    public static PreFlowModel fromActivity(Activity activity) {
        return new PreFlowModel(activity, Payment.fromJson(getActivityRequestJson(activity)));
    }

    /**
     * Create an instance from a service context.
     *
     * @param clientCommunicator The client communicator for sending/receiving messages at this point in the flow
     * @param request            The deserialised Payment
     * @param senderInternalData The internal data of the app that started this stage
     * @return An instance of {@link PreFlowModel}
     */
    @NonNull
    public static PreFlowModel fromService(ClientCommunicator clientCommunicator, Payment request, InternalData senderInternalData) {
        return new PreFlowModel(clientCommunicator, request, senderInternalData);
    }

    /**
     * Get the payment model that was used to initiate the flow.
     *
     * @return The payment model used to initiate the flow
     */
    @NonNull
    public Payment getPayment() {
        return payment;
    }

    /**
     * Get the payment builder that can be used to update {@link Payment} data for the flow.
     *
     * The builder has been initialised with the original payment data. Any data can be updated with the exception of
     * {@link PaymentBuilder#withPaymentFlow(String, String)} - the flow name and type are read only at this stage.
     *
     * @return the payment builder that can be used to update {@link Payment} data for the flow
     */
    @NonNull
    public PaymentBuilder getPaymentBuilder() {
        return paymentBuilder;
    }

    /**
     * Send off any updated {@link Payment} as created from the {@link #getPaymentBuilder()}.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void sendResponse() {
        FlowResponse flowResponse = new FlowResponse();
        flowResponse.setUpdatedPayment(paymentBuilder.build());
        doSendResponse(flowResponse.toJson());
    }

    /**
     * Cancel the payment flow.
     */
    public void cancelFlow() {
        FlowResponse flowResponse = new FlowResponse();
        flowResponse.setCancelTransaction(true);
        doSendResponse(flowResponse.toJson());
    }

    /**
     * Call to inform FPS that processing is done and no augmentation is required.
     *
     * Note that this does NOT finish any activity or stop any service. That is down to the activity/service to manage internally.
     */
    public void skip() {
        sendEmptyResponse();
    }

    @Override
    @NonNull
    public String getRequestJson() {
        return payment.toJson();
    }
}
