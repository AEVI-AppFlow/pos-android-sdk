package com.aevi.payment;

import android.content.ComponentName;
import android.os.Build;

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.pos.flow.PaymentClientImpl;
import com.aevi.sdk.pos.flow.model.Request;
import com.aevi.sdk.pos.flow.model.RequestStatus;
import com.aevi.sdk.pos.flow.model.Response;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.AppMessageTypes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import static com.aevi.sdk.flow.ApiHelper.getInternalData;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class PaymentClientImplTest extends ApiTestBase {

    private static final String REQUEST_MSG_NO_DATA = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, getInternalData()).toJson();
    private com.aevi.sdk.pos.flow.PaymentClientImpl paymentClient;

    @Mock
    private ObservableEmitter<Response> callback;

    @Mock
    private ObservableMessengerClient messengerClient;

    @Before
    public void setup() {
        ShadowLog.stream = System.out;
        initMocks(this);
        paymentClient = new PaymentClientImpl(RuntimeEnvironment.application) {
            @Override
            protected ObservableMessengerClient getNewMessengerClient(ComponentName componentName) {
                return messengerClient;
            }
        };
        when(messengerClient.sendMessage(anyString())).thenReturn(Observable.just("{}"));
        setupMockBoundMessengerService();
    }

    @Test
    public void checkGetPaymentServices() throws Exception {
        paymentClient.getPaymentServices().test();
        verify(messengerClient).sendMessage(REQUEST_MSG_NO_DATA);
        verify(messengerClient).closeConnection();
    }

    @Test
    public void checkInitiatePayment() throws Exception {
        Request request = new Request.Builder().withTransactionType("pay").build();

        paymentClient.initiatePayment(request).test();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(messengerClient).sendMessage(captor.capture());
        AppMessage sentAppMessage = AppMessage.fromJson(captor.getValue());
        Request sentRequest = Request.fromJson(sentAppMessage.getMessageData());
        assertThat(sentRequest).isEqualTo(request);

        verify(messengerClient).closeConnection();
    }

    @Test
    public void checkGenerateCardToken() throws Exception {
        paymentClient.generateCardToken().test();
        verify(messengerClient).sendMessage(REQUEST_MSG_NO_DATA);
        verify(messengerClient).closeConnection();
    }

    @Test
    public void checkGenerateCardTokenWithPaymentServiceId() throws Exception {
        Request request = new Request.Builder().withTransactionType("token").withPaymentService("123").build();

        paymentClient.generateCardToken("123").test();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(messengerClient).sendMessage(captor.capture());
        AppMessage sentAppMessage = AppMessage.fromJson(captor.getValue());
        Request sentRequest = Request.fromJson(sentAppMessage.getMessageData());
        assertThat(sentRequest.getPaymentServiceId()).isEqualTo("123");
        verify(messengerClient).closeConnection();
    }

    @Test
    public void checkSubscribeToStatusUpdates() throws Exception {
        paymentClient.subscribeToStatusUpdates("123").test();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(messengerClient).sendMessage(captor.capture());
        AppMessage sentAppMessage = AppMessage.fromJson(captor.getValue());
        RequestStatus requestStatus = RequestStatus.fromJson(sentAppMessage.getMessageData());
        assertThat(requestStatus.getStatus()).isEqualTo("123");
        verify(messengerClient).closeConnection();
    }
}
