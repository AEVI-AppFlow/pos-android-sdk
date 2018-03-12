package com.aevi.sdk.pos.flow;

import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.constants.FinancialRequestTypes;
import com.aevi.sdk.flow.constants.TransactionTypes;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.AppMessageTypes;
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

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class PaymentClientImplTest extends ApiTestBase {

    private final String REQUEST_MSG_NO_DATA = new AppMessage(AppMessageTypes.REQUEST_MESSAGE, getInternalData()).toJson();
    private PaymentClientImpl paymentClient;

    @Mock
    private ObservableMessengerClient messengerClient;

    public PaymentClientImplTest() {
        super("payment-api.properties");
    }

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
    }

    @Test
    public void getPaymentServicesShouldSendAndReceiveDataCorrectly() throws Exception {
        PaymentServiceInfo testInfo = getPaymentServiceInfo();
        when(messengerClient.sendMessage(anyString())).thenReturn(Observable.just(testInfo.toJson()));

        TestObserver<PaymentServices> testObserver = paymentClient.getPaymentServices().test();

        verify(messengerClient).sendMessage(REQUEST_MSG_NO_DATA);
        PaymentServices result = testObserver.values().get(0);
        assertThat(result.getAllPaymentServices()).hasSize(1);
        assertThat(result.getAllPaymentServices().get(0)).isEqualTo(testInfo);
        verify(messengerClient).closeConnection();
    }

    private PaymentServiceInfo getPaymentServiceInfo() {
        Context context = mock(Context.class);
        when(context.getPackageName()).thenReturn("com.test");
        return new PaymentServiceInfoBuilder()
                .withVendor("Test")
                .withVersion("1.0.0")
                .withDisplayName("PA one")
                .withTerminalId("1234")
                .withMerchantIds("5678")
                .withSupportedRequestTypes("hawk", "snail")
                .withSupportedTransactionTypes("banana", "pear")
                .withSupportedCurrencies("GBP", "AUD")
                .withDefaultCurrency("GBP")
                .build(context);
    }

    @Test
    public void initiatePaymentShouldSendPaymentViaRequestCorrectly() throws Exception {
        Payment payment = new PaymentBuilder().withTransactionType(TransactionTypes.SALE).withAmounts(new Amounts(1000, "GBP")).build();

        paymentClient.initiatePayment(payment).test();

        AppMessage sentAppMessage = callSendAndCaptureMessage();
        Request request = Request.fromJson(sentAppMessage.getMessageData());
        Payment sentPayment = request.getRequestData().getValue(FinancialRequestTypes.PAYMENT, Payment.class);
        assertThat(sentPayment).isEqualTo(payment);
        verify(messengerClient).closeConnection();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentIfTokenAppIdAndPaymentServiceIdMismatch() throws Exception {
        Token token = new Token("123", "card");
        token.setSourceAppId("123");
        Payment payment = new PaymentBuilder().withTransactionType(TransactionTypes.SALE).withAmounts(new Amounts(1000, "GBP"))
                .withCardToken(token).build();

        paymentClient.initiatePayment(payment, "456", "789");
    }

    @Test
    public void generateCardTokenShouldSendCorrectMessage() throws Exception {
        paymentClient.generateCardToken().test();

        AppMessage sentAppMessage = callSendAndCaptureMessage();
        Request sentTokenise = Request.fromJson(sentAppMessage.getMessageData());
        assertThat(sentTokenise).isNotNull();
        assertThat(sentTokenise.getId()).isNotNull();
        verify(messengerClient).closeConnection();
    }

    @Test
    public void generateCardTokenShouldPassPaymentServiceIdCorrectly() throws Exception {
        paymentClient.generateCardToken("123").test();

        AppMessage sentAppMessage = callSendAndCaptureMessage();
        Request sentTokenise = Request.fromJson(sentAppMessage.getMessageData());
        assertThat(sentTokenise.getTargetAppId()).isEqualTo("123");
        verify(messengerClient).closeConnection();
    }

    @Test
    public void subscribeToStatusUpdatesShouldPropagateUpdatesCorrectly() throws Exception {
        paymentClient.subscribeToStatusUpdates("123").test();

        AppMessage sentAppMessage = callSendAndCaptureMessage();
        RequestStatus requestStatus = RequestStatus.fromJson(sentAppMessage.getMessageData());
        assertThat(requestStatus.getStatus()).isEqualTo("123");
        verify(messengerClient).closeConnection();
    }

    private AppMessage callSendAndCaptureMessage() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(messengerClient).sendMessage(captor.capture());
        AppMessage sentAppMessage = AppMessage.fromJson(captor.getValue());
        return sentAppMessage;
    }
}
