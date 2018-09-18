package com.aevi.sdk.flow;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.model.AdditionalData;
import com.aevi.sdk.flow.model.AppMessage;
import com.aevi.sdk.flow.model.Device;
import com.aevi.sdk.flow.model.FlowEvent;
import com.aevi.sdk.flow.model.config.FlowConfig;
import com.aevi.sdk.flow.model.config.FpsSettings;
import com.aevi.sdk.flow.model.config.SystemSettings;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfoBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static com.aevi.sdk.flow.TestHelper.pretendServiceIsInstalled;
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
        flowClient = new FlowClientImpl(FlowBaseConfig.VERSION, RuntimeEnvironment.application) {
            @Override
            protected ObservableMessengerClient getMessengerClient(ComponentName componentName) {
                return messengerClient;
            }
        };
        when(messengerClient.sendMessage(anyString())).thenReturn(Observable.just("{}"));
    }

    @Test
    public void getDevicesShouldSendCorrectMessage() throws Exception {
        pretendServiceIsInstalled(ApiBase.FLOW_PROCESSING_SERVICE_COMPONENT);
        flowClient.getDevices().test();

        AppMessage appMessage = callSendAndCaptureMessage();
        assertThat(appMessage).isNotNull();
        verify(messengerClient).closeConnection();
    }

    @Test
    public void getDevicesShouldErrorIfNoFps() throws Exception {
        TestObserver<List<Device>> testObserver = flowClient.getDevices().test();
        assertThat(testObserver.assertError(ApiBase.NO_FPS_EXCEPTION));
    }

    @Test
    public void subscribeToSystemEventsShouldSendAndReceiveCorrectly() throws Exception {
        pretendServiceIsInstalled(ApiBase.FLOW_PROCESSING_SERVICE_COMPONENT);
        AdditionalData additionalData = new AdditionalData();
        additionalData.addData("test", "hello");
        FlowEvent flowEvent = new FlowEvent("type", additionalData, "trigger");
        when(messengerClient.sendMessage(anyString())).thenReturn(Observable.just(flowEvent.toJson()));
        TestObserver<FlowEvent> testObserver = flowClient.subscribeToSystemEvents().test();

        AppMessage appMessage = callSendAndCaptureMessage();
        assertThat(appMessage).isNotNull();
        testObserver.assertValueCount(1);
        assertThat(testObserver.values().get(0)).isEqualTo(flowEvent);
        verify(messengerClient).closeConnection();
    }

    @Test
    public void subscribeToSystemEventsShouldErrorIfNoFps() throws Exception {
        TestObserver<FlowEvent> testObserver = flowClient.subscribeToSystemEvents().test();
        assertThat(testObserver.assertError(ApiBase.NO_FPS_EXCEPTION));
    }

    @Test
    public void getSystemSettingsShouldSendAndReceiveCorrectly() throws Exception {
        pretendServiceIsInstalled(ApiBase.FLOW_PROCESSING_SERVICE_COMPONENT);
        SystemSettings systemSettings = new SystemSettings(new ArrayList<FlowConfig>(), new FpsSettings(), new AdditionalData());
        when(messengerClient.sendMessage(anyString())).thenReturn(Observable.just(systemSettings.toJson()));
        TestObserver<SystemSettings> testObserver = flowClient.getSystemSettings().test();

        AppMessage appMessage = callSendAndCaptureMessage();
        assertThat(appMessage).isNotNull();
        testObserver.assertValueCount(1);
        assertThat(testObserver.values().get(0)).isNotNull();
        verify(messengerClient).closeConnection();
    }

    @Test
    public void getSystemSettingsShouldErrorIfNoFps() throws Exception {
        TestObserver<SystemSettings> testObserver = flowClient.getSystemSettings().test();
        assertThat(testObserver.assertError(ApiBase.NO_FPS_EXCEPTION));
    }

    private PaymentFlowServiceInfo getFlowServiceInfo() throws PackageManager.NameNotFoundException {
        Context context = ContextHelper.mockContext("com.test", "1.2.3");
        return new PaymentFlowServiceInfoBuilder()
                .withVendor("Test")
                .withDisplayName("Hello")
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
