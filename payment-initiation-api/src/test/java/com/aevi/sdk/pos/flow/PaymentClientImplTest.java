package com.aevi.sdk.pos.flow;

import android.content.ComponentName;
import android.os.Build;
import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.constants.AppMessageTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowException;
import com.aevi.sdk.flow.model.Request;
import com.aevi.sdk.pos.flow.model.Amounts;
import com.aevi.sdk.pos.flow.model.Payment;
import com.aevi.sdk.pos.flow.model.PaymentBuilder;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

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
    public void initiatePaymentShouldSendPaymentViaRequestCorrectly() {
        pretendFpsIsInstalled();
        Payment payment = new PaymentBuilder().withPaymentFlow("blarp").withAmounts(new Amounts(1000, "GBP")).build();
        String uuid = payment.getId(); // do something with this id here

        paymentClient.initiatePayment(payment).test();

        AppMessage sentAppMessage = callSendAndCaptureMessage();
        Request request = Request.fromJson(sentAppMessage.getMessageData());
        Payment sentPayment = request.getRequestData().getValue(AppMessageTypes.PAYMENT_MESSAGE, Payment.class);
        assertThat(sentPayment).isEqualTo(payment);
        verify(messengerClient).closeConnection();
    }

    @Test
    public void initiatePaymentShouldErrorIfNoFps() {
        Payment payment = new PaymentBuilder().withPaymentFlow("blarp").withAmounts(new Amounts(1000, "GBP")).build();
        TestObserver<Void> test = paymentClient.initiatePayment(payment).test();
        assertThat(test.errors().get(0)).isInstanceOf(FlowException.class);
    }

    private AppMessage callSendAndCaptureMessage() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(messengerClient).sendMessage(captor.capture());
        AppMessage sentAppMessage = AppMessage.fromJson(captor.getValue());
        return sentAppMessage;
    }
}
