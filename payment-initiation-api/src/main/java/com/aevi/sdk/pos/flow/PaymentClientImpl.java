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
import com.aevi.android.rxmessenger.MessageException;
import com.aevi.sdk.flow.BaseApiClient;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.model.*;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.model.config.PaymentSettings;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;

public class PaymentClientImpl extends BaseApiClient implements PaymentClient {

    private static final String TAG = PaymentClientImpl.class.getSimpleName();

    PaymentClientImpl(Context context) {
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
        return mapException(paymentInfoMessenger
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
                }));
    }

    @Override
    @NonNull
    public Single<PaymentResponse> initiatePayment(final Payment payment) {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ChannelClient transactionMessenger = getMessengerClient(FLOW_PROCESSING_SERVICE_COMPONENT);

        AdditionalData paymentData = new AdditionalData();
        paymentData.addData(AppMessageTypes.PAYMENT_MESSAGE, payment);
        Request request = new Request(payment.getFlowName(), paymentData);
        request.setDeviceId(payment.getDeviceId());
        AppMessage appMessage = new AppMessage(AppMessageTypes.PAYMENT_MESSAGE, request.toJson(), getInternalData());
        return mapException(transactionMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(new Function<String, PaymentResponse>() {
                    @Override
                    public PaymentResponse apply(String json) throws Exception {
                        Response response = Response.fromJson(json);
                        PaymentResponse paymentResponse = response.getResponseData().getValue(AppMessageTypes.PAYMENT_MESSAGE, PaymentResponse.class);
                        paymentResponse.setOriginatingPayment(payment);
                        return paymentResponse;
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        transactionMessenger.closeConnection();
                    }
                }));
    }
}
