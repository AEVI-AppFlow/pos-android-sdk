package com.aevi.sdk.pos.flow;


import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.ApiBase;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.AppMessageTypes;
import com.aevi.sdk.pos.flow.model.*;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class PaymentClientImpl extends ApiBase implements PaymentClient {

    private static final String API_PROPS_FILE = "payment-api.properties";
    private static final String TAG = PaymentClientImpl.class.getSimpleName();

    private final Context context;

    PaymentClientImpl(Context context) {
        super(API_PROPS_FILE);
        this.context = context;
        startPcs(context);
        Log.i(TAG, "PaymentClient initialised");
    }

    @Override
    public Single<PaymentServices> getPaymentServices() {
        final ObservableMessengerClient paymentInfoMessenger = getNewMessengerClient(PAYMENT_SERVICE_INFO_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, getInternalData());
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
    public Single<Response> initiatePayment(Request request) {
        final ObservableMessengerClient transactionMessenger = getNewMessengerClient(PAYMENT_CONTROL_SERVICE_COMPONENT);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, request.toJson(), getInternalData());
        return transactionMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(String json) throws Exception {
                        return Response.fromJson(json);
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
    public Single<TokenResponse> generateCardToken() {
        return generateCardToken(null);
    }

    @Override
    public Single<TokenResponse> generateCardToken(String paymentServiceId) {
        final ObservableMessengerClient tokenizeMessenger = getNewMessengerClient(TOKENIZE_SERVICE_COMPONENT);
        String requestData = null;
        if (paymentServiceId != null) {
            Request request = new Request.Builder().withTransactionType("token").withPaymentService(paymentServiceId).build();
            requestData = request.toJson();
        }
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, requestData, getInternalData());
        return tokenizeMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(new Function<String, TokenResponse>() {
                    @Override
                    public TokenResponse apply(String json) throws Exception {
                        return TokenResponse.fromJson(json);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        tokenizeMessenger.closeConnection();
                    }
                });
    }

    @Override
    public RequestStatus getCurrentRequestStatus(String requestId) {
        return subscribeToStatusUpdates(requestId).blockingFirst();
    }

    @Override
    public Observable<RequestStatus> subscribeToStatusUpdates(String requestId) {
        final ObservableMessengerClient requestStatusMessenger = getNewMessengerClient(REQUEST_STATUS_SERVICE_COMPONENT);
        RequestStatus requestStatus = new RequestStatus(requestId);
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

    protected ObservableMessengerClient getNewMessengerClient(ComponentName componentName) {
        return new ObservableMessengerClient(context, componentName);
    }
}
