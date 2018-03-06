package com.aevi.sdk.pos.flow;


import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.ApiBase;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.AppMessageTypes;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Response;
import com.aevi.sdk.flow.model.Token;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentResponse;
import com.aevi.sdk.pos.flow.model.PaymentServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentServices;
import com.aevi.sdk.pos.flow.model.RequestStatus;
import com.aevi.sdk.pos.flow.model.TokenResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

import static com.aevi.sdk.flow.constants.FinancialRequestTypes.PAYMENT;
import static com.aevi.sdk.flow.constants.FinancialRequestTypes.TOKENISATION;
import static com.aevi.sdk.flow.util.Preconditions.checkArgument;

public class PaymentClientImpl extends ApiBase implements PaymentClient {

    private static final String API_PROPS_FILE = "payment-api.properties";
    private static final String TAG = PaymentClientImpl.class.getSimpleName();

    private final Context context;

    PaymentClientImpl(Context context) {
        super(API_PROPS_FILE);
        this.context = context;
        startFps(context);
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
    public Single<PaymentResponse> initiatePayment(Payment payment) {
        return initiatePayment(payment, null, null);
    }

    @Override
    public Single<PaymentResponse> initiatePayment(Payment payment, String paymentServiceId, String deviceId) {
        Token cardToken = payment.getCardToken();
        if (paymentServiceId != null && cardToken != null) {
            checkArgument(paymentServiceId.equals(cardToken.getSourceAppId()), "paymentServiceId can not be set to a different value than what is set in the Token");
        }

        final ObservableMessengerClient transactionMessenger = getNewMessengerClient(FLOW_PROCESSING_SERVICE_COMPONENT);

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
                        return PaymentResponse.fromJson(json);
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
        final ObservableMessengerClient tokenizeMessenger = getNewMessengerClient(FLOW_PROCESSING_SERVICE_COMPONENT);
        final Request request = new Request(TOKENISATION);
        request.setTargetAppId(paymentServiceId);
        AppMessage appMessage = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, request.toJson(), getInternalData());
        return tokenizeMessenger
                .sendMessage(appMessage.toJson())
                .singleOrError()
                .map(new Function<String, TokenResponse>() {
                    @Override
                    public TokenResponse apply(String json) throws Exception {
                        Response response = Response.fromJson(json);
                        if (response.wasSuccessful()) {
                            return response.getResponseData().getValue(TOKENISATION, TokenResponse.class);
                        }

                        return new TokenResponse(null);
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
    public RequestStatus getCurrentPaymentStatus(String requestId) {
        return subscribeToStatusUpdates(requestId).blockingFirst();
    }

    @Override
    public Observable<RequestStatus> subscribeToStatusUpdates(String paymentId) {
        final ObservableMessengerClient requestStatusMessenger = getNewMessengerClient(REQUEST_STATUS_SERVICE_COMPONENT);
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

    protected ObservableMessengerClient getNewMessengerClient(ComponentName componentName) {
        return new ObservableMessengerClient(context, componentName);
    }
}
