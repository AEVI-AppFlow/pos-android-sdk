package com.aevi.sdk.pos.flow;


import android.os.Build;
import android.os.RemoteException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Ignore
@Config(sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class PaymentApiTest extends ApiTestBase {

    public PaymentApiTest() {
        super("1.2.3");
    }

    @Test
    public void checkIsProcessingServiceInstalledReportsFalse() throws Exception {
        assertThat(PaymentApi.isProcessingServiceInstalled(RuntimeEnvironment.application)).isFalse();
    }

    @Test
    public void checkIsProcessingServiceInstalledFlagReportsTrue() throws RemoteException {
        setupMockBoundMessengerService();

        assertThat(PaymentApi.isProcessingServiceInstalled(RuntimeEnvironment.application)).isTrue();
    }
}
