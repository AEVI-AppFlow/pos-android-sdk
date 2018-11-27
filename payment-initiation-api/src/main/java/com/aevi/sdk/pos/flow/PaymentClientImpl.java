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
import android.support.annotation.NonNull;
import android.util.Log;
import com.aevi.android.rxmessenger.ChannelClient;
import com.aevi.sdk.flow.BaseApiClient;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.constants.ResponseMechanisms;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.model.config.PaymentSettings;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class PaymentClientImpl extends BaseApiClient implements PaymentClient {

    private static final String TAG = PaymentClientImpl.class.getSimpleName();

    protected PaymentClientImpl(Context context) {
        super(PaymentInitiationConfig.VERSION, context);
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
                .map(new Function<String, PaymentSettings>() {
                    @Override
                    public PaymentSettings apply(String json) throws Exception {
                        return PaymentSettings.fromJson(json);
                    }
                })
                .singleOrError()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        paymentInfoMessenger.closeConnection();
                    }
                })
                .onErrorResumeNext(new Function<Throwable, SingleSource<? extends PaymentSettings>>() {
                    @Override
                    public SingleSource<? extends PaymentSettings> apply(Throwable throwable) throws Exception {
                        return Single.error(createFlowException(throwable));
                    }
                });
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
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        transactionMessenger.closeConnection();
                    }
                })
                .onErrorResumeNext(new Function<Throwable, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Throwable throwable) throws Exception {
                        return Completable.error(createFlowException(throwable));
                    }
                });
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
                .map(new Function<String, PaymentResponse>() {
                    @Override
                    public PaymentResponse apply(String json) throws Exception {
                        Response response = Response.fromJson(json);
                        return response.getResponseData().getValue(AppMessageTypes.PAYMENT_MESSAGE, PaymentResponse.class);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        transactionMessenger.closeConnection();
                    }
                })
                .onErrorResumeNext(new Function<Throwable, SingleSource<? extends PaymentResponse>>() {
                    @Override
                    public SingleSource<? extends PaymentResponse> apply(Throwable throwable) throws Exception {
                        return Single.error(createFlowException(throwable));
                    }
                });
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
