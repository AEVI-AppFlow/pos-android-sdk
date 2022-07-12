package com.aevi.sdk.flow;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.aevi.android.rxmessenger.client.ObservableMessengerClient;
import com.aevi.sdk.flow.model.*;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import static com.aevi.sdk.flow.TestHelper.pretendServiceIsInstalled;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Ignore
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class BaseApiClientTest {

    class TestApiBase extends BaseApiClient {

        public TestApiBase(String propsFile) {
            super(propsFile, RuntimeEnvironment.application);
        }

        @Override
        public InternalData getInternalData() {
            return super.getInternalData();
        }

        public void callStartFps(Context context) {
            startFps(context);
        }

        public ComponentName getFpsComponent() {
            return FLOW_PROCESSING_SERVICE_COMPONENT;
        }
    }

    private TestApiBase apiBase;

    @Mock
    private ObservableMessengerClient messengerClient;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        apiBase = new TestApiBase("1.2.3") {
            @Override
            protected ObservableMessengerClient getMessengerClient(ComponentName componentName) {
                return messengerClient;
            }
        };
        when(messengerClient.sendMessage(anyString())).thenReturn(Observable.just("{}"));
    }

    @Test
    public void shouldParsePropertiesFileAndSetVersion() throws Exception {
        assertThat(apiBase.getInternalData().getSenderApiVersion()).isEqualTo("1.2.3"); // As per test resource
    }

    @Test
    public void callStartFpsShouldSendStartServiceIntent() throws Exception {
        apiBase.callStartFps(RuntimeEnvironment.application);
        Intent intent = ShadowApplication.getInstance().peekNextStartedService();
        assertThat(intent.getComponent()).isEqualTo(apiBase.getFpsComponent());
    }

    @Test
    public void callIsProcessingServiceInstalledShouldReturnFalse() throws Exception {
        assertThat(BaseApiClient.isProcessingServiceInstalled(RuntimeEnvironment.application)).isFalse();
    }

    @Test
    public void callIsProcessingServiceInstalledShouldReturnTrueIfPackageManagerThinksSo() throws Exception {
        pretendServiceIsInstalled(BaseApiClient.FLOW_PROCESSING_SERVICE_COMPONENT);

        assertThat(BaseApiClient.isProcessingServiceInstalled(RuntimeEnvironment.application)).isTrue();
    }

    @Test
    public void getDevicesShouldSendCorrectMessage() throws Exception {
        pretendServiceIsInstalled(BaseApiClient.FLOW_PROCESSING_SERVICE_COMPONENT);
        apiBase.getDevices().test();

        AppMessage appMessage = callSendAndCaptureMessage();
        assertThat(appMessage).isNotNull();
        verify(messengerClient).closeConnection();
    }

    @Test
    public void getDevicesShouldErrorIfNoFps() throws Exception {
        TestObserver<List<Device>> testObserver = apiBase.getDevices().test();
        assertThat(testObserver.assertError(BaseApiClient.NO_FPS_EXCEPTION));
    }

    @Test
    public void subscribeToSystemEventsShouldSendAndReceiveCorrectly() throws Exception {
        pretendServiceIsInstalled(BaseApiClient.FLOW_PROCESSING_SERVICE_COMPONENT);
        AdditionalData additionalData = new AdditionalData();
        additionalData.addData("test", "hello");
        FlowEvent flowEvent = new FlowEvent("type", additionalData, "trigger");
        when(messengerClient.sendMessage(anyString())).thenReturn(Observable.just(flowEvent.toJson()));
        TestObserver<FlowEvent> testObserver = apiBase.subscribeToSystemEvents().test();

        AppMessage appMessage = callSendAndCaptureMessage();
        assertThat(appMessage).isNotNull();
        testObserver.assertValueCount(1);
        assertThat(testObserver.values().get(0)).isEqualTo(flowEvent);
        verify(messengerClient).closeConnection();
    }

    @Test
    public void subscribeToSystemEventsShouldErrorIfNoFps() throws Exception {
        TestObserver<FlowEvent> testObserver = apiBase.subscribeToSystemEvents().test();
        assertThat(testObserver.assertError(BaseApiClient.NO_FPS_EXCEPTION));
    }

    private AppMessage callSendAndCaptureMessage() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(messengerClient).sendMessage(captor.capture());
        AppMessage sentAppMessage = AppMessage.fromJson(captor.getValue());
        return sentAppMessage;
    }
}
