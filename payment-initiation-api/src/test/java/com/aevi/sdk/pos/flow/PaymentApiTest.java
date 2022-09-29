package com.aevi.sdk.pos.flow;


import android.os.Build;
import android.os.RemoteException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;

import androidx.test.core.app.ApplicationProvider;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class PaymentApiTest extends ApiTestBase {

    public PaymentApiTest() {
        super("1.2.3");
    }

    @Test
    public void checkIsProcessingServiceInstalledReportsFalse() throws Exception {
        assertThat(PaymentApi.isProcessingServiceInstalled(ApplicationProvider.getApplicationContext())).isFalse();
    }

    @Test
    public void checkIsProcessingServiceInstalledFlagReportsTrue() throws RemoteException {
        setupMockBoundMessengerService();

        assertThat(PaymentApi.isProcessingServiceInstalled(ApplicationProvider.getApplicationContext())).isTrue();
    }
}
