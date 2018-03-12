package com.aevi.sdk.flow;

import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.FlowServiceInfo;
import com.aevi.sdk.flow.model.FlowServiceInfoBuilder;
import com.aevi.sdk.flow.model.FlowServices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class FlowClientImplTest {

    @Mock
    private ObservableMessengerClient messengerClient;

    private FlowClientImpl flowClient;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        flowClient = new FlowClientImpl(RuntimeEnvironment.application) {
            @Override
            protected ObservableMessengerClient getClient(ComponentName componentName) {
                return messengerClient;
            }
        };
        when(messengerClient.sendMessage(anyString())).thenReturn(Observable.just("{}"));
    }

    @Test
    public void getDevicesShouldSendCorrectMessage() throws Exception {
        flowClient.getDevices().test();

        AppMessage appMessage = callSendAndCaptureMessage();
        assertThat(appMessage).isNotNull();
        verify(messengerClient).closeConnection();
    }

    @Test
    public void getFlowServicesShouldSendAndReceiveCorrectMessages() throws Exception {
        FlowServiceInfo serviceInfo = getFlowServiceInfo();
        when(messengerClient.sendMessage(anyString())).thenReturn(Observable.just(serviceInfo.toJson()));
        TestObserver<FlowServices> testObserver = flowClient.getFlowServices().test();

        AppMessage appMessage = callSendAndCaptureMessage();

        assertThat(appMessage).isNotNull();
        testObserver.assertValueCount(1);
        assertThat(testObserver.values().get(0).getAllFlowServices()).hasSize(1);
        assertThat(testObserver.values().get(0).getAllFlowServices().get(0)).isEqualTo(serviceInfo);
        verify(messengerClient).closeConnection();
    }

    private FlowServiceInfo getFlowServiceInfo() {
        Context context = mock(Context.class);
        when(context.getPackageName()).thenReturn("com.aevi.test");
        return new FlowServiceInfoBuilder()
                .withVendor("Test")
                .withVersion("1.2.3")
                .withDisplayName("Hello")
                .withCapabilities("stuff")
                .withStages("stage 1")
                .withSupportedRequestTypes("tea making")
                .build(context);
    }

    private AppMessage callSendAndCaptureMessage() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(messengerClient).sendMessage(captor.capture());
        AppMessage sentAppMessage = AppMessage.fromJson(captor.getValue());
        return sentAppMessage;
    }
}
