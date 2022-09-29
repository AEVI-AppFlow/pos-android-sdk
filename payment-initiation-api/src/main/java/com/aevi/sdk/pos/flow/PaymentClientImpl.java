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


import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.aevi.android.rxmessenger.ChannelClient;
import com.aevi.sdk.flow.BaseApiClient;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.constants.ResponseMechanisms;
import com.aevi.sdk.flow.model.*;
import com.aevi.sdk.pos.flow.initiation.BuildConfig;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.model.config.PaymentSettings;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Implementation of payment client.
 *
 * This is an internal class not intended to be used directly by external applications. No guarantees are made of backwards compatibility and the
 * class may be removed without any warning.
 */
public class PaymentClientImpl extends BaseApiClient implements PaymentClient {

    private static final String TAG = PaymentClientImpl.class.getSimpleName();

    protected PaymentClientImpl(Context context) {
        super(BuildConfig.VERSION, context);
        startFps(context);
        Log.i(TAG, "PaymentClient initialised from " + context.getPackageName());
    }

    @NonNull
    @Override
    public Single<PaymentSettings> getPaymentSettings() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient paymentInfoMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.PAYMENT_FLOW_CONFIG_REQUEST, getInternalData());
        return paymentInfoMessenger
                .sendMessage(appMessage.toJson())
                .map(PaymentSettings::fromJson)
                .singleOrError()
                .doFinally(paymentInfoMessenger::closeConnection)
                .onErrorResumeNext(throwable -> Single.error(createFlowException(throwable)));
    }

    @Override
    @NonNull
    public Completable initiatePayment(final Payment payment) {
        if (!isProcessingServiceInstalled(context)) {
            return Completable.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient transactionMessenger = getMessengerClient(FLOW_PROCESSING_SERVICE_COMPONENT);
        AppMessage appMessage = createAppMessageForPayment(payment, ResponseMechanisms.RESPONSE_SERVICE);

        return transactionMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .ignoreElement()
                .doFinally(transactionMessenger::closeConnection)
                .onErrorResumeNext(throwable -> Completable.error(createFlowException(throwable)));
    }

    @Override
    @NonNull
    public Observable<PaymentResponse> queryPaymentResponses(@NonNull ResponseQuery paymentResponseQuery) {
        if (!isProcessingServiceInstalled(context)) {
            return Observable.error(NO_FPS_EXCEPTION);
        }

        paymentResponseQuery.setResponseType(PaymentResponse.class.getName());

        final ChannelClient paymentInfoMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.RESPONSES_REQUEST, paymentResponseQuery.toJson(), getInternalData());
        return paymentInfoMessenger
                .sendMessage(appMessage.toJson())
                .map(PaymentResponse::fromJson)
                .doFinally(paymentInfoMessenger::closeConnection)
                .onErrorResumeNext((Function<Throwable, ObservableSource<? extends PaymentResponse>>) throwable -> Observable
                        .error(createFlowException(throwable)));
    }

    protected Single<PaymentResponse> initiatePaymentDirect(final Payment payment) {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient transactionMessenger = getMessengerClient(FLOW_PROCESSING_SERVICE_COMPONENT);
        AppMessage appMessage = createAppMessageForPayment(payment, ResponseMechanisms.MESSENGER_CONNECTION);
        return transactionMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(json -> {
                    Response response = Response.fromJson(json);
                    return response.getResponseData().getValue(AppMessageTypes.PAYMENT_MESSAGE, PaymentResponse.class);
                })
                .doFinally(transactionMessenger::closeConnection)
                .onErrorResumeNext(throwable -> Single.error(createFlowException(throwable)));
    }

    protected AppMessage createAppMessageForPayment(Payment payment, String responseMechanism) {
        AdditionalData paymentData = new AdditionalData();
        paymentData.addData(AppMessageTypes.PAYMENT_MESSAGE, payment);
        Request request = new Request(payment.getFlowName(), paymentData);
        request.setDeviceId(payment.getDeviceId());
        AppMessage appMessage = new AppMessage(AppMessageTypes.PAYMENT_MESSAGE, request.toJson(), getInternalData());
        appMessage.setResponseMechanism(responseMechanism);
        return appMessage;
    }

}
