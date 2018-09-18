package com.aevi.sdk.pos.flow;

import android.content.ComponentName;
import android.os.Build;

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.constants.FinancialRequestTypes;
import com.aevi.sdk.flow.constants.TransactionTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.flow.model.Token;
import com.aevi.sdk.pos.flow.model.*;

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
import io.reactivex.observers.TestObserver;

import static com.aevi.sdk.pos.flow.TestEnvironment.pretendFpsIsInstalled;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class PaymentClientImplTest {

    private PaymentClientImpl paymentClient;

    @Mock
    private ObservableMessengerClient messengerClient;

    @Before
    public void setup() {
        ShadowLog.stream = System.out;
        initMocks(this);
        paymentClient = new PaymentClientImpl(RuntimeEnvironment.application) {
            @Override
            protected ObservableMessengerClient getMessengerClient(ComponentName componentName) {
                return messengerClient;
            }
        };
        when(messengerClient.sendMessage(anyString())).thenReturn(Observable.just("{}"));
    }

    @Test
    public void initiatePaymentShouldSendPaymentViaRequestCorrectly() throws Exception {
        pretendFpsIsInstalled();
        Payment payment = new PaymentBuilder().withTransactionType(TransactionTypes.SALE).withAmounts(new Amounts(1000, "GBP")).build();

        paymentClient.initiatePayment(payment).test();

        AppMessage sentAppMessage = callSendAndCaptureMessage();
        Request request = Request.fromJson(sentAppMessage.getMessageData());
        Payment sentPayment = request.getRequestData().getValue(FinancialRequestTypes.PAYMENT, Payment.class);
        assertThat(sentPayment).isEqualTo(payment);
        verify(messengerClient).closeConnection();
    }


    public void initiatePaymentShouldErrorIfNoFps() throws Exception {
        Payment payment = new PaymentBuilder().withTransactionType(TransactionTypes.SALE).withAmounts(new Amounts(1000, "GBP")).build();
        TestObserver<PaymentResponse> testObserver = paymentClient.initiatePayment(payment).test();
        assertThat(testObserver.errors().get(0)).isInstanceOf(IllegalStateException.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfTokenAppIdAndPaymentServiceIdMismatch() throws Exception {
        pretendFpsIsInstalled();
        Token token = new Token("123", "card");
        token.setSourceAppId("123");
        Payment payment = new PaymentBuilder().withTransactionType(TransactionTypes.SALE).withAmounts(new Amounts(1000, "GBP"))
                .withCardToken(token).build();

        paymentClient.initiatePayment(payment, "456", "789");
    }

    @Test
    public void subscribeToStatusUpdatesShouldPropagateUpdatesCorrectly() throws Exception {
        pretendFpsIsInstalled();
        paymentClient.subscribeToStatusUpdates("123").test();

        AppMessage sentAppMessage = callSendAndCaptureMessage();
        RequestStatus requestStatus = RequestStatus.fromJson(sentAppMessage.getMessageData());
        assertThat(requestStatus.getStatus()).isEqualTo("123");
        verify(messengerClient).closeConnection();
    }

    @Test
    public void subscribeToStatusUpdatesShouldErrorIfNoFps() throws Exception {
        TestObserver<RequestStatus> testObserver = paymentClient.subscribeToStatusUpdates("").test();
        assertThat(testObserver.errors().get(0)).isInstanceOf(IllegalStateException.class);
    }

    private AppMessage callSendAndCaptureMessage() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(messengerClient).sendMessage(captor.capture());
        AppMessage sentAppMessage = AppMessage.fromJson(captor.getValue());
        return sentAppMessage;
    }
}
