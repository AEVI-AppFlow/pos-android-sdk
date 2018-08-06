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

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.FlowClientImpl;
import com.aevi.sdk.flow.model.*;
import com.aevi.sdk.pos.flow.model.*;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

import static com.aevi.sdk.flow.constants.FinancialRequestTypes.PAYMENT;
import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

public class PaymentClientImpl extends FlowClientImpl implements PaymentClient {

    private static final String TAG = PaymentClientImpl.class.getSimpleName();

    PaymentClientImpl(Context context) {
        super(PaymentInitiationConfig.VERSION, context);
        startFps(context);
        Log.i(TAG, "PaymentClient initialised");
    }

    @Override
    @NonNull
    public Single<PaymentServices> getPaymentServices() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient paymentInfoMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.PAYMENT_SERVICE_INFO_REQUEST, getInternalData());
        return paymentInfoMessenger
                .sendMessage(appMessage.toJson())
                .map(new Function<String, PaymentServiceInfo>() {
                    @Override
                    public PaymentServiceInfo apply(String json) throws Exception {
                        return PaymentServiceInfo.fromJson(json);
                    }
                })
                .toList()
                .map(new Function<List<PaymentServiceInfo>, PaymentServices>() {
                    @Override
                    public PaymentServices apply(List<PaymentServiceInfo> paymentServiceInfoList) throws Exception {
                        return new PaymentServices(paymentServiceInfoList);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        paymentInfoMessenger.closeConnection();
                    }
                });
    }

    @Override
    @NonNull
    public Single<List<String>> getSupportedTransactionTypes() {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient deviceMessenger = getMessengerClient(INFO_PROVIDER_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.SUPPORTED_TRANSACTION_TYPES_REQUEST, getInternalData());
        return deviceMessenger
                .sendMessage(appMessage.toJson())
                .toList()
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        deviceMessenger.closeConnection();
                    }
                });
    }

    @Override
    @NonNull
    public Single<PaymentResponse> initiatePayment(Payment payment) {
        return initiatePayment(payment, null, null);
    }

    @Override
    @NonNull
    public Single<PaymentResponse> initiatePayment(final Payment payment, String paymentServiceId, String deviceId) {
        if (!isProcessingServiceInstalled(context)) {
            return Single.error(NO_FPS_EXCEPTION);
        }
        Token cardToken = payment.getCardToken();
        if (paymentServiceId != null && cardToken != null) {
            checkArgument(paymentServiceId.equals(cardToken.getSourceAppId()), "paymentServiceId can not be set to a different value than what is set in the Token");
        }

        final ObservableMessengerClient transactionMessenger = getMessengerClient(FLOW_PROCESSING_SERVICE_COMPONENT);

        AdditionalData paymentData = new AdditionalData();
        paymentData.addData(PAYMENT, payment);
        Request request = new Request(PAYMENT, paymentData);
        request.setTargetAppId(paymentServiceId);
        request.setDeviceId(deviceId);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, request.toJson(), getInternalData());
        return transactionMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(new Function<String, PaymentResponse>() {
                    @Override
                    public PaymentResponse apply(String json) throws Exception {
                        Response response = Response.fromJson(json);
                        PaymentResponse paymentResponse = response.getResponseData().getValue(PAYMENT, PaymentResponse.class);
                        paymentResponse.setOriginatingPayment(payment);
                        return paymentResponse;
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        transactionMessenger.closeConnection();
                    }
                });
    }

    @Override
    @NonNull
    public RequestStatus getCurrentPaymentStatus(String requestId) {
        return subscribeToStatusUpdates(requestId).blockingFirst();
    }

    @Override
    @NonNull
    public Observable<RequestStatus> subscribeToStatusUpdates(String paymentId) {
        if (!isProcessingServiceInstalled(context)) {
            return Observable.error(NO_FPS_EXCEPTION);
        }
        final ObservableMessengerClient requestStatusMessenger = getMessengerClient(REQUEST_STATUS_SERVICE_COMPONENT);
        RequestStatus requestStatus = new RequestStatus(paymentId);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, requestStatus.toJson(), getInternalData());
        return requestStatusMessenger
                .sendMessage(appMessage.toJson())
                .map(new Function<String, RequestStatus>() {
                    @Override
                    public RequestStatus apply(String json) throws Exception {
                        return RequestStatus.fromJson(json);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        requestStatusMessenger.closeConnection();
                    }
                });
    }

}
